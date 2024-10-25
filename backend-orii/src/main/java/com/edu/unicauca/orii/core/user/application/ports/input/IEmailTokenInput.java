package com.edu.unicauca.orii.core.user.application.ports.input;

import com.edu.unicauca.orii.core.user.domain.model.EmailToken;

public interface IEmailTokenInput {
    EmailToken generateToken(Long idUser);
    boolean confirmToken(String token);
}
