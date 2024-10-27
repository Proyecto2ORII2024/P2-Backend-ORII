package com.edu.unicauca.orii.core.user.application.ports.output;
import com.edu.unicauca.orii.core.user.domain.model.EmailToken;
import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IEmailConfirmationOutput {
    void sendConfirmationEmail(User user, EmailToken emailToken);
}
