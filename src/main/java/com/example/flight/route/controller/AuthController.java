package com.example.flight.route.controller;

import com.example.flight.route.model.User;
import com.example.flight.route.security.JwtUtil;
import com.example.flight.route.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private static class InvalidCredentialsException extends RuntimeException {
    }

    @PostMapping("/token")
    public String generateToken(
            @RequestParam @NotBlank String username,
            @RequestParam @NotBlank String password
    ) {
        User user = userService.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        List<String> roles = Collections.singletonList(user.getRole().name());
        return jwtUtil.generateToken(username, roles);
    }
}