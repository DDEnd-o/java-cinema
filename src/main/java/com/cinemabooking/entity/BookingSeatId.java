package com.cinemabooking.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookingSeatId implements Serializable {

    private Integer bookingId;
    private Integer seatId;

    public BookingSeatId() {
    }

    public BookingSeatId(Integer bookingId, Integer seatId) {
        this.bookingId = bookingId;
        this.seatId = seatId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingSeatId)) return false;
        BookingSeatId that = (BookingSeatId) o;
        return Objects.equals(bookingId, that.bookingId) && Objects.equals(seatId, that.seatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, seatId);
    }
}
