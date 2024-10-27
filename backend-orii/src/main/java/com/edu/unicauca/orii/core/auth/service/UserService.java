package com.edu.unicauca.orii.core.auth.service;

import com.edu.unicauca.orii.core.auth.dto.LoginRequest;
import com.edu.unicauca.orii.core.auth.util.JwtUtil;
import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Optional<String> authenticate(LoginRequest loginRequest) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByEmail(loginRequest.getEmail()));
        if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            System.out.println("Generando el token...");
            String token = jwtUtil.generateToken(user.get().getEmail(), user.get().getRole().name());
            return Optional.of(token);
        }
        return Optional.empty();
    }
}
