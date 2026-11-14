package com.cinemabooking.service;

import com.cinemabooking.entity.Movie;
import com.cinemabooking.entity.MovieStatus;
import com.cinemabooking.exception.ResourceNotFoundException;
import com.cinemabooking.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getShowingMovies() {
        return movieRepository.findByStatusOrderByReleaseDateDesc(MovieStatus.showing);
    }

    public List<Movie> getUpcomingMovies() {
        return movieRepository.findByStatusOrderByReleaseDateDesc(MovieStatus.upcoming);
    }

    public Movie getById(Integer movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay phim #" + movieId));
    }

    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    @Transactional
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public void delete(Integer movieId) {
        movieRepository.deleteById(movieId);
    }
}
