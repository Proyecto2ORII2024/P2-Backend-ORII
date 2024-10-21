package com.edu.unicauca.orii.core.user.application.ports.input;

import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IUserCommandPort {
    
    public User createUser(User user);

    public User updateUser(Long id, User user);

    public void deleteUser(Long userId);
}
