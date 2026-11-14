package com.cinemabooking.service;

import com.cinemabooking.dto.RegisterForm;
import com.cinemabooking.entity.Role;
import com.cinemabooking.entity.User;
import com.cinemabooking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterForm form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Ten dang nhap da ton tai");
        }
        if (form.getEmail() != null && userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email da duoc su dung");
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setRole(Role.customer);
        return userRepository.save(user);
    }
}
