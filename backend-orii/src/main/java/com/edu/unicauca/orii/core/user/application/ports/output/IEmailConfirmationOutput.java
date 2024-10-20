package com.edu.unicauca.orii.core.user.application.ports.output;
import com.edu.unicauca.orii.core.user.domain.model.MailData;

public interface IEmailConfirmationOutput {
    void sendConfirmationEmail(MailData mailData);
}
