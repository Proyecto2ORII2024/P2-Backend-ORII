package com.edu.unicauca.orii.core.auth.service;

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

    public Optional<String> authenticate(String email, String password) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            String token = jwtUtil.generateToken(user.get().getEmail(), user.get().getRole().name());
            return Optional.of(token);
        }
        return Optional.empty();
    }
}
