package com.cinemabooking.service;

import com.cinemabooking.entity.Showtime;
import com.cinemabooking.exception.ResourceNotFoundException;
import com.cinemabooking.repository.ShowtimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    public List<Showtime> getAll() {
        return showtimeRepository.findAllByOrderByStartTimeDesc();
    }

    public List<Showtime> getByMovieId(Integer movieId) {
        return showtimeRepository.findByMovieIdOrderByStartTimeAsc(movieId);
    }

    public Showtime getById(Integer showtimeId) {
        return showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay suat chieu #" + showtimeId));
    }

    @Transactional
    public Showtime save(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    @Transactional
    public void delete(Integer showtimeId) {
        showtimeRepository.deleteById(showtimeId);
    }
}
