package com.cinemabooking.service;

import com.cinemabooking.entity.Room;
import com.cinemabooking.entity.Seat;
import com.cinemabooking.entity.SeatType;
import com.cinemabooking.exception.ResourceNotFoundException;
import com.cinemabooking.repository.RoomRepository;
import com.cinemabooking.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;

    public RoomService(RoomRepository roomRepository, SeatRepository seatRepository) {
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
    }

    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public Room getById(Integer roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay phong chieu #" + roomId));
    }

    public List<Seat> getSeats(Integer roomId) {
        return seatRepository.findByRoom_RoomIdOrderBySeatRowAscSeatNumberAsc(roomId);
    }

    /** Tao phong chieu moi va tu sinh luoi ghe standard theo so hang/so ghe mot hang. */
    @Transactional
    public Room createRoomWithSeats(String roomName, int rows, int seatsPerRow) {
        Room room = new Room(roomName, rows * seatsPerRow);
        room = roomRepository.save(room);

        for (int r = 0; r < rows; r++) {
            char rowLabel = (char) ('A' + r);
            for (int n = 1; n <= seatsPerRow; n++) {
                Seat seat = new Seat();
                seat.setRoom(room);
                seat.setSeatRow(String.valueOf(rowLabel));
                seat.setSeatNumber(n);
                seat.setSeatType(SeatType.standard);
                seatRepository.save(seat);
            }
        }
        return room;
    }

    /** Cap nhat loai ghe (standard/vip) cho tung ghe cua 1 phong theo danh sach SeatID la VIP. */
    @Transactional
    public void updateSeatTypes(Integer roomId, Set<Integer> vipSeatIds) {
        List<Seat> seats = getSeats(roomId);
        for (Seat seat : seats) {
            seat.setSeatType(vipSeatIds.contains(seat.getSeatId()) ? SeatType.vip : SeatType.standard);
        }
        seatRepository.saveAll(seats);
    }

    @Transactional
    public void delete(Integer roomId) {
        roomRepository.deleteById(roomId);
    }
}
