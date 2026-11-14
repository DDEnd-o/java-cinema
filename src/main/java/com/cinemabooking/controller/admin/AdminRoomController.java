package com.cinemabooking.controller.admin;

import com.cinemabooking.entity.Seat;
import com.cinemabooking.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** FR-09: Quan ly Phong chieu & Ghe (Admin). */
@Controller
public class AdminRoomController {

    private final RoomService roomService;

    public AdminRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/admin/rooms")
    public String list(Model model) {
        model.addAttribute("rooms", roomService.getAll());
        return "admin/rooms";
    }

    @GetMapping("/admin/rooms/new")
    public String newForm() {
        return "admin/room-form";
    }

    @PostMapping("/admin/rooms")
    public String create(@RequestParam String roomName,
                          @RequestParam int rows,
                          @RequestParam int seatsPerRow,
                          RedirectAttributes redirectAttributes) {
        roomService.createRoomWithSeats(roomName, rows, seatsPerRow);
        redirectAttributes.addFlashAttribute("success", "Da tao phong chieu va sinh so do ghe.");
        return "redirect:/admin/rooms";
    }

    @GetMapping("/admin/rooms/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("room", roomService.getById(id));
        model.addAttribute("seatsByRow", groupByRow(roomService.getSeats(id)));
        return "admin/room-detail";
    }

    @PostMapping("/admin/rooms/{id}/seats")
    public String updateSeats(@PathVariable Integer id,
                               @RequestParam(name = "vipSeatIds", required = false) Set<Integer> vipSeatIds,
                               RedirectAttributes redirectAttributes) {
        roomService.updateSeatTypes(id, vipSeatIds == null ? Set.of() : vipSeatIds);
        redirectAttributes.addFlashAttribute("success", "Da cap nhat loai ghe.");
        return "redirect:/admin/rooms/" + id;
    }

    @PostMapping("/admin/rooms/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        roomService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Da xoa phong chieu.");
        return "redirect:/admin/rooms";
    }

    private Map<String, List<Seat>> groupByRow(List<Seat> seats) {
        Map<String, List<Seat>> result = new LinkedHashMap<>();
        for (Seat seat : seats) {
            result.computeIfAbsent(seat.getSeatRow(), k -> new java.util.ArrayList<>()).add(seat);
        }
        return result;
    }
}
