package com.edu.unicauca.orii.core.user.application.ports.input;

import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IEmailConfirmationInput {
    void sendConfirmationEmail(User user);
}
