package com.edu.unicauca.orii.core.user.application.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.user.application.ports.input.IEmailConfirmationInput;
import com.edu.unicauca.orii.core.user.application.ports.input.IEmailTokenInput;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailTokenOutput;
import com.edu.unicauca.orii.core.user.domain.model.EmailToken;
import com.edu.unicauca.orii.core.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailConfirmationInput , IEmailTokenInput {

    private final IEmailTokenOutput emailTokenOutput;

    private final IEmailConfirmationOutput emailConfirmationOutput;

    @Override
    @Async
    public void sendConfirmationEmail(User user) {
        System.out.println("Sending email confirmation");
        EmailToken emailToken = emailTokenOutput.generateToken(user.getUserId());
        emailConfirmationOutput.sendConfirmationEmail(user, emailToken);
    }

    @Override
    public EmailToken generateToken(Long idUser) {
        return emailTokenOutput.generateToken(idUser);
    }

    @Override
    public boolean confirmToken(String token) {
        return emailTokenOutput.confirmToken(token);
    }
    
}
