package com.edu.unicauca.orii.core.user.application.ports.output;

import com.edu.unicauca.orii.core.user.domain.model.EmailToken;

public interface IEmailTokenOutput {
    EmailToken generateToken(Long idUser);
    boolean confirmToken(String token);
    boolean deleteToken(Long idUser);
}
