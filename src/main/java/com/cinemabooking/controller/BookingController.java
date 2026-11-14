package com.cinemabooking.controller;

import com.cinemabooking.dto.SeatView;
import com.cinemabooking.entity.Booking;
import com.cinemabooking.entity.BookingSeat;
import com.cinemabooking.entity.BookingStatus;
import com.cinemabooking.entity.PaymentMethod;
import com.cinemabooking.entity.Showtime;
import com.cinemabooking.exception.SeatAlreadyBookedException;
import com.cinemabooking.security.UserPrincipal;
import com.cinemabooking.service.BookingService;
import com.cinemabooking.service.ShowtimeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final ShowtimeService showtimeService;

    public BookingController(BookingService bookingService, ShowtimeService showtimeService) {
        this.bookingService = bookingService;
        this.showtimeService = showtimeService;
    }

    /** FR-04: so do ghe cua 1 suat chieu. */
    @GetMapping("/booking/{showtimeId}/seats")
    public String seatSelection(@PathVariable Integer showtimeId, Model model) {
        Showtime showtime = showtimeService.getById(showtimeId);
        List<SeatView> seats = bookingService.buildSeatMap(showtimeId);

        Map<String, List<SeatView>> seatsByRow = new LinkedHashMap<>();
        long bookedCount = 0;
        for (SeatView seat : seats) {
            seatsByRow.computeIfAbsent(seat.getSeatRow(), k -> new java.util.ArrayList<>()).add(seat);
            if (seat.isBooked()) {
                bookedCount++;
            }
        }

        List<Showtime> movieShowtimes = showtimeService.getByMovieId(showtime.getMovie().getMovieId());

        model.addAttribute("showtime", showtime);
        model.addAttribute("seatsByRow", seatsByRow);
        model.addAttribute("movieShowtimes", movieShowtimes);
        model.addAttribute("totalSeatsCount", seats.size());
        model.addAttribute("bookedSeatsCount", bookedCount);
        model.addAttribute("availableSeatsCount", seats.size() - bookedCount);
        return "booking/seat-select";
    }

    /** FR-05 + FR-12: tao don dat ve. */
    @PostMapping("/booking")
    public String createBooking(@RequestParam Integer showtimeId,
                                 @RequestParam(name = "seatIds", required = false) List<Integer> seatIds,
                                 @AuthenticationPrincipal UserPrincipal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            Booking booking = bookingService.createBooking(principal.getUserId(), showtimeId, seatIds);
            return "redirect:/booking/" + booking.getBookingId() + "/payment";
        } catch (SeatAlreadyBookedException | DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("error",
                    "Ghe vua duoc nguoi khac dat, vui long chon ghe khac.");
            return "redirect:/booking/" + showtimeId + "/seats";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/booking/" + showtimeId + "/seats";
        }
    }

    /** FR-06: trang xac nhan thanh toan gia lap. */
    @GetMapping("/booking/{id}/payment")
    public String paymentPage(@PathVariable("id") Integer id,
                               @AuthenticationPrincipal UserPrincipal principal,
                               Model model) {
        Booking booking = bookingService.getOwnedBooking(id, principal.getUserId());
        List<BookingSeat> seats = bookingService.getSeatsOfBooking(id);
        model.addAttribute("booking", booking);
        model.addAttribute("bookingSeats", seats);
        return "booking/payment";
    }

    @PostMapping("/booking/{id}/payment")
    public String pay(@PathVariable("id") Integer id,
                       @RequestParam PaymentMethod method,
                       @AuthenticationPrincipal UserPrincipal principal,
                       RedirectAttributes redirectAttributes) {
        try {
            bookingService.completePayment(id, principal.getUserId(), method);
            return "redirect:/booking/" + id + "/confirmation";
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/booking/" + id + "/payment";
        }
    }

    @GetMapping("/booking/{id}/confirmation")
    public String confirmation(@PathVariable("id") Integer id,
                                @AuthenticationPrincipal UserPrincipal principal,
                                Model model) {
        Booking booking = bookingService.getOwnedBooking(id, principal.getUserId());
        if (booking.getStatus() != BookingStatus.paid) {
            return "redirect:/booking/" + id + "/payment";
        }
        List<BookingSeat> seats = bookingService.getSeatsOfBooking(id);
        model.addAttribute("booking", booking);
        model.addAttribute("bookingSeats", seats);
        return "booking/confirmation";
    }
}
