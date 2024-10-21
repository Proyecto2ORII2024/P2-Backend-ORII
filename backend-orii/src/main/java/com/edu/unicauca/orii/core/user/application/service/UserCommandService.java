package com.edu.unicauca.orii.core.user.application.service;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.user.application.ports.input.IUserCommandPort;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserCommandPersistencePort;
import com.edu.unicauca.orii.core.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandService implements IUserCommandPort{

    private final IUserCommandPersistencePort userCommandPersistencePort;

    @Override
    public User createUser(User user) {
        return userCommandPersistencePort.createUser(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        return userCommandPersistencePort.updateUser(id, user);
    }

    @Override
    public void deleteUser(Long userId) {
        userCommandPersistencePort.deleteUser(userId);
    }

    
}
