package com.cinemabooking.controller;

import com.cinemabooking.entity.Movie;
import com.cinemabooking.entity.Showtime;
import com.cinemabooking.service.MovieService;
import com.cinemabooking.service.ShowtimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final ShowtimeService showtimeService;

    public MovieController(MovieService movieService, ShowtimeService showtimeService) {
        this.movieService = movieService;
        this.showtimeService = showtimeService;
    }

    /** FR-02: trang chu, danh sach phim dang chieu / sap chieu + lich chieu nhanh. */
    @GetMapping("/")
    public String home(Model model) {
        List<Movie> showingMovies = movieService.getShowingMovies();
        List<Movie> upcomingMovies = movieService.getUpcomingMovies();

        Map<Integer, List<Showtime>> showtimesByMovie = new LinkedHashMap<>();
        List<Showtime> allShowingShowtimes = new ArrayList<>();
        for (Movie m : showingMovies) {
            List<Showtime> sts = showtimeService.getByMovieId(m.getMovieId());
            showtimesByMovie.put(m.getMovieId(), sts);
            allShowingShowtimes.addAll(sts);
        }

        model.addAttribute("showingMovies", showingMovies);
        model.addAttribute("upcomingMovies", upcomingMovies);
        model.addAttribute("showtimesByMovie", showtimesByMovie);
        model.addAttribute("allShowingShowtimes", allShowingShowtimes);
        return "index";
    }

    /** FR-03: chi tiet phim va danh sach suat chieu. */
    @GetMapping("/movies/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        Movie movie = movieService.getById(id);
        List<Showtime> showtimes = showtimeService.getByMovieId(id);

        Map<LocalDate, List<Showtime>> showtimesByDate = new LinkedHashMap<>();
        for (Showtime st : showtimes) {
            LocalDate date = st.getStartTime().toLocalDate();
            showtimesByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(st);
        }

        List<Movie> relatedMovies = movieService.getShowingMovies().stream()
                .filter(m -> !m.getMovieId().equals(id))
                .limit(4)
                .collect(Collectors.toList());

        model.addAttribute("movie", movie);
        model.addAttribute("showtimes", showtimes);
        model.addAttribute("showtimesByDate", showtimesByDate);
        model.addAttribute("relatedMovies", relatedMovies);
        return "movie-detail";
    }
}
