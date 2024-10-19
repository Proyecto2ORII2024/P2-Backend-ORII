package com.edu.unicauca.orii.core.user.application.ports.input;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IUserQueryPort {

    Page<User> getUser(Pageable pageable);

    User getUserById(Long userId);
    
}
