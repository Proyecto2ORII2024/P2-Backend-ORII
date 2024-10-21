package com.edu.unicauca.orii.core.user.application.service;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.user.application.ports.input.IEmailConfirmationInput;
import com.edu.unicauca.orii.core.user.application.ports.input.IEmailTokenInput;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailTokenOutput;
import com.edu.unicauca.orii.core.user.domain.model.EmailToken;
import com.edu.unicauca.orii.core.user.domain.model.MailData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailConfirmationInput , IEmailTokenInput {

    private final IEmailTokenOutput emailTokenOutput;

    private final IEmailConfirmationOutput emailConfirmationOutput;

    @Override
    public void sendConfirmationEmail(MailData mailData) {
        emailConfirmationOutput.sendConfirmationEmail(mailData);
    }

    @Override
    public EmailToken generateToken(Long idUser) {
        return emailTokenOutput.generateToken(idUser);
    }

    @Override
    public void confirmToken(String token) {
        emailTokenOutput.confirmToken(token);
    }
    
}
