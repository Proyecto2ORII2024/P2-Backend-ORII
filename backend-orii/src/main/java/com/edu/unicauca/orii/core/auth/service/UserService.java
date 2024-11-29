package com.edu.unicauca.orii.core.auth.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.auth.dto.LoginRequest;
import com.edu.unicauca.orii.core.auth.util.JwtUtil;
import com.edu.unicauca.orii.core.common.exception.EmailNotVerifiedException;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service for managing user authentication and operations.
 * This service provides methods for user login, token generation, and other user-related operations.
 * 
 * @author JhossefCons 
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

/**
 * Service for managing user authentication and operations.
 * This service provides methods for user login and token generation.
 *
 * @param loginRequest The request object containing user credentials.
 * @return An Optional containing a JWT token if authentication is successful, or an empty Optional if it fails.
 * @throws EmailNotVerifiedException if the user's email address has not been verified.
 */
    public Optional<String> authenticate(LoginRequest loginRequest) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByEmail(loginRequest.getEmail()));

        if (user.isPresent()) {
            if (!user.get().getEmailVerified()) {
                 throw new EmailNotVerifiedException(HttpStatus.UNAUTHORIZED.value(), "El correo electrónico no ha sido verificado.");
            }
            if (passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
                System.out.println("Generando el token...");
                String token = jwtUtil.generateToken(user.get().getEmail(), user.get().getRole().name(), user.get().getUserId());
                return Optional.of(token);
            }
        }
        return Optional.empty();

    }
}


 /*if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            System.out.println("Generando el token...");
            String token = jwtUtil.generateToken(user.get().getEmail(), user.get().getRole().name());
            return Optional.of(token);
        }
        return Optional.empty();*/