package com.edu.unicauca.orii.core.user.application.ports.output;
import com.edu.unicauca.orii.core.user.domain.model.MailData;
import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IEmailConfirmationOutput {
  
    void sendPasswordEmail(MailData mailData);

    void sendConfirmationEmail(User user);
}
