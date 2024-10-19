package com.edu.unicauca.orii.core.user.application.ports.output;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IUserQueryPersistencePort {

    Page<User> getUser(Pageable pageable);

    User getUserById(Long userId);
}
