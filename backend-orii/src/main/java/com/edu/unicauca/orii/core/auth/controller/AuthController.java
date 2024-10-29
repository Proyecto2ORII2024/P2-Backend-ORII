package com.edu.unicauca.orii.core.auth.controller;

import com.edu.unicauca.orii.core.auth.dto.LoginRequest;
import com.edu.unicauca.orii.core.auth.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map; 
import java.util.HashMap;
import java.util.Optional;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<String> token = userService.authenticate(loginRequest);
        
        if (token.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", token.get());
            
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Collections.singletonMap("error", "Invalid credentials"));
    }

}
