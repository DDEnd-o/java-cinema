package com.cinemabooking.repository;

import com.cinemabooking.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {

    @Query("select s from Showtime s where s.movie.movieId = :movieId order by s.startTime asc")
    List<Showtime> findByMovieIdOrderByStartTimeAsc(Integer movieId);

    List<Showtime> findAllByOrderByStartTimeDesc();
}
