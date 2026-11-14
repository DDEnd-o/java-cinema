package com.cinemabooking.controller.admin;

import com.cinemabooking.entity.Booking;
import com.cinemabooking.entity.BookingStatus;
import com.cinemabooking.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** FR-11: Xem danh sach ve da ban (Admin). */
@Controller
public class AdminBookingController {

    private final BookingService bookingService;

    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/admin/bookings")
    public String list(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();

        Map<Integer, String> seatLabelsByBooking = new HashMap<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        long paidCount = 0;
        long pendingCount = 0;

        for (Booking booking : bookings) {
            String labels = bookingService.getSeatsOfBooking(booking.getBookingId()).stream()
                    .map(bs -> bs.getSeat().getLabel())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            seatLabelsByBooking.put(booking.getBookingId(), labels);

            if (booking.getStatus() == BookingStatus.paid && booking.getTotalAmount() != null) {
                totalRevenue = totalRevenue.add(booking.getTotalAmount());
                paidCount++;
            } else if (booking.getStatus() == BookingStatus.pending) {
                pendingCount++;
            }
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("seatLabelsByBooking", seatLabelsByBooking);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("paidCount", paidCount);
        model.addAttribute("pendingCount", pendingCount);
        return "admin/bookings";
    }
}
