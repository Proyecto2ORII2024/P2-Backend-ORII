package com.edu.unicauca.orii.core.user.application.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.user.application.ports.output.IUserQueryPersistencePort;
import com.edu.unicauca.orii.core.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    
    private final IUserQueryPersistencePort userQueryPersistencePort;

    public org.springframework.data.domain.Page<User> getUser(Pageable pageable) {
        return userQueryPersistencePort.getUser(pageable);
    }

    public User getUserById(Long userId) {
        return userQueryPersistencePort.getUserById(userId);
    }

}
