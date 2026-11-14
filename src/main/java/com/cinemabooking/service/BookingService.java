package com.cinemabooking.service;

import com.cinemabooking.dto.SeatView;
import com.cinemabooking.entity.Booking;
import com.cinemabooking.entity.BookingSeat;
import com.cinemabooking.entity.BookingStatus;
import com.cinemabooking.entity.Payment;
import com.cinemabooking.entity.PaymentMethod;
import com.cinemabooking.entity.PaymentStatus;
import com.cinemabooking.entity.Seat;
import com.cinemabooking.entity.Showtime;
import com.cinemabooking.entity.User;
import com.cinemabooking.exception.ResourceNotFoundException;
import com.cinemabooking.exception.SeatAlreadyBookedException;
import com.cinemabooking.repository.BookingRepository;
import com.cinemabooking.repository.BookingSeatRepository;
import com.cinemabooking.repository.PaymentRepository;
import com.cinemabooking.repository.SeatRepository;
import com.cinemabooking.repository.ShowtimeRepository;
import com.cinemabooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public BookingService(BookingRepository bookingRepository,
                           BookingSeatRepository bookingSeatRepository,
                           SeatRepository seatRepository,
                           ShowtimeRepository showtimeRepository,
                           UserRepository userRepository,
                           PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    /** FR-04: so do ghe cua 1 suat chieu - ghe nao da bi dat cho chinh suat chieu nay. */
    public List<SeatView> buildSeatMap(Integer showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay suat chieu #" + showtimeId));

        List<Seat> seats = seatRepository.findByRoom_RoomIdOrderBySeatRowAscSeatNumberAsc(showtime.getRoom().getRoomId());
        Set<Integer> bookedSeatIds = Set.copyOf(bookingSeatRepository.findBookedSeatIdsByShowtimeId(showtimeId));

        return seats.stream()
                .map(seat -> new SeatView(
                        seat.getSeatId(),
                        seat.getSeatRow(),
                        seat.getSeatNumber(),
                        seat.getLabel(),
                        seat.getSeatType().name(),
                        bookedSeatIds.contains(seat.getSeatId())
                ))
                .collect(Collectors.toList());
    }

    /**
     * FR-05 + FR-12: tao don dat ve trong 1 transaction.
     * Rang buoc UNIQUE(ShowtimeID, SeatID) o DB la chot chan cuoi cung chong trung ghe
     * khi 2 request chay song song deu qua duoc buoc kiem tra truoc (TC-04): request
     * nao INSERT truoc thi thanh cong, request con lai se nhan DataIntegrityViolationException
     * khi flush va toan bo transaction (ca Booking vua tao) bi rollback.
     */
    @Transactional
    public Booking createBooking(Integer userId, Integer showtimeId, List<Integer> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Vui long chon it nhat 1 ghe");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay tai khoan"));
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay suat chieu #" + showtimeId));

        List<Seat> seats = seatRepository.findAllById(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new ResourceNotFoundException("Mot so ghe da chon khong ton tai");
        }
        for (Seat seat : seats) {
            if (!seat.getRoom().getRoomId().equals(showtime.getRoom().getRoomId())) {
                throw new IllegalArgumentException("Ghe " + seat.getLabel() + " khong thuoc phong chieu cua suat nay");
            }
        }

        // Kiem tra truoc (UX, khong atomic) de bao loi than thien ngay cho truong hop thong thuong.
        List<BookingSeat> already = bookingSeatRepository.findAlreadyBooked(showtimeId, seatIds);
        if (!already.isEmpty()) {
            String labels = already.stream().map(bs -> bs.getSeat().getLabel()).collect(Collectors.joining(", "));
            throw new SeatAlreadyBookedException("Ghe " + labels + " vua duoc nguoi khac dat, vui long chon ghe khac.");
        }

        BigDecimal totalAmount = showtime.getPrice().multiply(BigDecimal.valueOf(seats.size()));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShowtime(showtime);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.pending);
        booking = bookingRepository.save(booking);

        for (Seat seat : seats) {
            bookingSeatRepository.save(new BookingSeat(booking, seat, showtime));
        }
        // Flush ngay trong transaction nay de buoc MySQL kiem tra UNIQUE(ShowtimeID, SeatID)
        // va nem DataIntegrityViolationException tai day neu co tranh chap, thay vi cho tu Hibernate flush sau.
        bookingSeatRepository.flush();

        return booking;
    }

    /** FR-06: thanh toan gia lap, chuyen trang thai don ve sang paid. */
    @Transactional
    public Payment completePayment(Integer bookingId, Integer userId, PaymentMethod method) {
        Booking booking = getOwnedBooking(bookingId, userId);
        if (booking.getStatus() != BookingStatus.pending) {
            throw new IllegalStateException("Don ve khong o trang thai cho thanh toan");
        }

        booking.setStatus(BookingStatus.paid);
        bookingRepository.save(booking);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(method);
        payment.setStatus(PaymentStatus.success);
        return paymentRepository.save(payment);
    }

    public Booking getOwnedBooking(Integer bookingId, Integer userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay don dat ve #" + bookingId));
        if (!booking.getUser().getUserId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException("Khong co quyen voi don dat ve nay");
        }
        return booking;
    }

    public List<BookingSeat> getSeatsOfBooking(Integer bookingId) {
        return bookingSeatRepository.findByBooking_BookingId(bookingId);
    }

    /** FR-07: lich su ve cua 1 khach hang. */
    public List<Booking> getBookingsOfUser(Integer userId) {
        return bookingRepository.findByUser_UserIdOrderByBookingTimeDesc(userId);
    }

    /** FR-11: danh sach ve da ban (admin). */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByBookingTimeDesc();
    }
}
