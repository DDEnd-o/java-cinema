package com.cinemabooking.controller.admin;

import com.cinemabooking.entity.Movie;
import com.cinemabooking.entity.Room;
import com.cinemabooking.entity.Showtime;
import com.cinemabooking.service.MovieService;
import com.cinemabooking.service.RoomService;
import com.cinemabooking.service.ShowtimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** FR-10: Quan ly Suat chieu (Admin). */
@Controller
public class AdminShowtimeController {

    private final ShowtimeService showtimeService;
    private final MovieService movieService;
    private final RoomService roomService;

    public AdminShowtimeController(ShowtimeService showtimeService, MovieService movieService, RoomService roomService) {
        this.showtimeService = showtimeService;
        this.movieService = movieService;
        this.roomService = roomService;
    }

    @GetMapping("/admin/showtimes")
    public String list(Model model) {
        model.addAttribute("showtimes", showtimeService.getAll());
        return "admin/showtimes";
    }

    @GetMapping("/admin/showtimes/new")
    public String newForm(Model model) {
        model.addAttribute("movies", movieService.getAll());
        model.addAttribute("rooms", roomService.getAll());
        return "admin/showtime-form";
    }

    @GetMapping("/admin/showtimes/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("showtime", showtimeService.getById(id));
        model.addAttribute("movies", movieService.getAll());
        model.addAttribute("rooms", roomService.getAll());
        return "admin/showtime-form";
    }

    @PostMapping("/admin/showtimes")
    public String create(@RequestParam Integer movieId,
                          @RequestParam Integer roomId,
                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime,
                          @RequestParam BigDecimal price,
                          RedirectAttributes redirectAttributes) {
        saveShowtime(null, movieId, roomId, startTime, endTime, price);
        redirectAttributes.addFlashAttribute("success", "Da them suat chieu moi.");
        return "redirect:/admin/showtimes";
    }

    @PostMapping("/admin/showtimes/{id}")
    public String update(@PathVariable Integer id,
                          @RequestParam Integer movieId,
                          @RequestParam Integer roomId,
                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime,
                          @RequestParam BigDecimal price,
                          RedirectAttributes redirectAttributes) {
        saveShowtime(id, movieId, roomId, startTime, endTime, price);
        redirectAttributes.addFlashAttribute("success", "Da cap nhat suat chieu.");
        return "redirect:/admin/showtimes";
    }

    @PostMapping("/admin/showtimes/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        showtimeService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Da xoa suat chieu (ca ve lien quan cung bi xoa theo).");
        return "redirect:/admin/showtimes";
    }

    private void saveShowtime(Integer id, Integer movieId, Integer roomId,
                               LocalDateTime startTime, LocalDateTime endTime, BigDecimal price) {
        Movie movie = movieService.getById(movieId);
        Room room = roomService.getById(roomId);

        Showtime showtime = id == null ? new Showtime() : showtimeService.getById(id);
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setStartTime(startTime);
        showtime.setEndTime(endTime);
        showtime.setPrice(price);
        showtimeService.save(showtime);
    }
}
