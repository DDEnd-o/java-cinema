package com.cinemabooking.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Ghe thuoc don dat ve nao (N-N giua Bookings va Seats).
 * UNIQUE(ShowtimeID, SeatID) la rang buoc then chot chong dat trung ghe (FR-12):
 * neu 2 nguoi cung INSERT ghe nay cho cung suat chieu, giao dich thu 2 se bi
 * MySQL tu choi bang DataIntegrityViolationException du da qua buoc kiem tra truoc do.
 */
@Entity
@Table(name = "BookingSeats",
        uniqueConstraints = @UniqueConstraint(name = "uq_showtime_seat", columnNames = {"ShowtimeID", "SeatID"}))
public class BookingSeat {

    @EmbeddedId
    private BookingSeatId id = new BookingSeatId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookingId")
    @JoinColumn(name = "BookingID")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seatId")
    @JoinColumn(name = "SeatID")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ShowtimeID", nullable = false)
    private Showtime showtime;

    public BookingSeat() {
    }

    public BookingSeat(Booking booking, Seat seat, Showtime showtime) {
        this.booking = booking;
        this.seat = seat;
        this.showtime = showtime;
        this.id = new BookingSeatId(booking.getBookingId(), seat.getSeatId());
    }

    public BookingSeatId getId() {
        return id;
    }

    public void setId(BookingSeatId id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }
}
