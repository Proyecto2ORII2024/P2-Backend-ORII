package com.edu.unicauca.orii.core.user.application.ports.input;

import com.edu.unicauca.orii.core.user.domain.model.MailData;

public interface IEmailConfirmationInput {
    void sendConfirmationEmail(MailData mailData);
}
