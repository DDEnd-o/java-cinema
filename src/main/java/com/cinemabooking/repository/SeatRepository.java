package com.cinemabooking.repository;

import com.cinemabooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findByRoom_RoomIdOrderBySeatRowAscSeatNumberAsc(Integer roomId);

    long countByRoom_RoomId(Integer roomId);

    void deleteByRoom_RoomId(Integer roomId);
}
