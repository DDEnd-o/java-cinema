package com.cinemabooking.controller;

import com.cinemabooking.dto.RegisterForm;
import com.cinemabooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute("registerForm") RegisterForm form) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            authService.register(form);
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("username", "duplicate", ex.getMessage());
            return "register";
        }
        redirectAttributes.addFlashAttribute("success", "Tao tai khoan thanh cong, vui long dang nhap.");
        return "redirect:/login";
    }
}
