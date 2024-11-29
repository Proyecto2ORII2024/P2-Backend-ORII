package com.edu.unicauca.orii.core.user.application.ports.output;

import java.util.List;
import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IUserQueryPersistencePort {

    List<User> getUser();

    User getUserById(Long userId);

    User getUserByEmail(String email);
    
}
