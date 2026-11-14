package com.cinemabooking.controller.admin;

import com.cinemabooking.entity.Movie;
import com.cinemabooking.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** FR-08: Quan ly Phim (Admin). */
@Controller
public class AdminMovieController {

    private final MovieService movieService;

    public AdminMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/admin/movies")
    public String list(Model model) {
        model.addAttribute("movies", movieService.getAll());
        return "admin/movies";
    }

    @GetMapping("/admin/movies/new")
    public String newForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "admin/movie-form";
    }

    @GetMapping("/admin/movies/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("movie", movieService.getById(id));
        return "admin/movie-form";
    }

    @PostMapping("/admin/movies")
    public String create(@ModelAttribute Movie movie, RedirectAttributes redirectAttributes) {
        movieService.save(movie);
        redirectAttributes.addFlashAttribute("success", "Da them phim moi.");
        return "redirect:/admin/movies";
    }

    @PostMapping("/admin/movies/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Movie movie, RedirectAttributes redirectAttributes) {
        movie.setMovieId(id);
        movieService.save(movie);
        redirectAttributes.addFlashAttribute("success", "Da cap nhat phim.");
        return "redirect:/admin/movies";
    }

    @PostMapping("/admin/movies/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        movieService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Da xoa phim.");
        return "redirect:/admin/movies";
    }
}
