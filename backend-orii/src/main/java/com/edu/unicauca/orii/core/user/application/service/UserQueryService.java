package com.edu.unicauca.orii.core.user.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.user.application.ports.output.IUserQueryPersistencePort;
import com.edu.unicauca.orii.core.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    
    private final IUserQueryPersistencePort userQueryPersistencePort;

    public List<User> getUser() {
        return userQueryPersistencePort.getUser();
    }

    public User getUserById(Long userId) {
        return userQueryPersistencePort.getUserById(userId);
    }

}
