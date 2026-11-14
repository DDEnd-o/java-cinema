package com.cinemabooking.controller;

import com.cinemabooking.entity.Booking;
import com.cinemabooking.security.UserPrincipal;
import com.cinemabooking.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AccountController {

    private final BookingService bookingService;

    public AccountController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /** FR-07: lich su ve da dat cua khach hang dang nhap. */
    @GetMapping("/account/bookings")
    public String myBookings(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        List<Booking> bookings = bookingService.getBookingsOfUser(principal.getUserId());

        Map<Integer, String> seatLabelsByBooking = new HashMap<>();
        for (Booking booking : bookings) {
            String labels = bookingService.getSeatsOfBooking(booking.getBookingId()).stream()
                    .map(bs -> bs.getSeat().getLabel())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            seatLabelsByBooking.put(booking.getBookingId(), labels);
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("seatLabelsByBooking", seatLabelsByBooking);
        return "account/bookings";
    }
}
