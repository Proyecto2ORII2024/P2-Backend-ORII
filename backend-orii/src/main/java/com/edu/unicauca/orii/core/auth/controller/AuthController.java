package com.edu.unicauca.orii.core.auth.controller;

import com.edu.unicauca.orii.core.auth.dto.LoginRequest;
import com.edu.unicauca.orii.core.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<String> token = userService.authenticate(loginRequest);
        if (token.isPresent()) {
            return ResponseEntity.ok("Bearer " + token.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

}
