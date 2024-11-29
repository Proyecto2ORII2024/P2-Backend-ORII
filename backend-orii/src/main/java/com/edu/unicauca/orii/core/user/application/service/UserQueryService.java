package com.edu.unicauca.orii.core.user.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.user.application.ports.input.IUserQueryPort;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserQueryPersistencePort;
import com.edu.unicauca.orii.core.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserQueryService implements IUserQueryPort {
    
    private final IUserQueryPersistencePort userQueryPersistencePort;

    @Override
    public List<User> getUser() {
        return userQueryPersistencePort.getUser();
    }

    @Override
    public User getUserById(Long userId) {
        return userQueryPersistencePort.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userQueryPersistencePort.getUserByEmail(email);
    }

}
