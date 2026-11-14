package com.cinemabooking.repository;

import com.cinemabooking.entity.Movie;
import com.cinemabooking.entity.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByStatusOrderByReleaseDateDesc(MovieStatus status);
}
