package com.edu.unicauca.orii.core.user.application.ports.input;

import java.util.List;
import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IUserQueryPort {

    List<User> getUser();

    User getUserById(Long userId);
    
}
