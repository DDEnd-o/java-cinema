package com.cinemabooking.repository;

import com.cinemabooking.entity.BookingSeat;
import com.cinemabooking.entity.BookingSeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, BookingSeatId> {

    @Query("select bs.seat.seatId from BookingSeat bs where bs.showtime.showtimeId = :showtimeId")
    List<Integer> findBookedSeatIdsByShowtimeId(@Param("showtimeId") Integer showtimeId);

    @Query("select bs from BookingSeat bs where bs.showtime.showtimeId = :showtimeId and bs.seat.seatId in :seatIds")
    List<BookingSeat> findAlreadyBooked(@Param("showtimeId") Integer showtimeId, @Param("seatIds") List<Integer> seatIds);

    List<BookingSeat> findByBooking_BookingId(Integer bookingId);
}
