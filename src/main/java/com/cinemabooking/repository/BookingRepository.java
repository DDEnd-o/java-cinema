package com.cinemabooking.repository;

import com.cinemabooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUser_UserIdOrderByBookingTimeDesc(Integer userId);

    List<Booking> findAllByOrderByBookingTimeDesc();
}
