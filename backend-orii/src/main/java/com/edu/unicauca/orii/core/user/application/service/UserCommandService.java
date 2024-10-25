package com.edu.unicauca.orii.core.user.application.service;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.user.application.ports.input.IUserCommandPort;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailTokenOutput;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserCommandPersistencePort;
import com.edu.unicauca.orii.core.user.domain.model.EmailToken;
import com.edu.unicauca.orii.core.user.domain.model.MailData;
import com.edu.unicauca.orii.core.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandService implements IUserCommandPort {

    private final IUserCommandPersistencePort userCommandPersistencePort;
    private final IEmailConfirmationOutput emailConfirmationOutput;
    private final IEmailTokenOutput emailTokenOutput;

    @Override
    public User createUser(User user) {
        User userCreated = userCommandPersistencePort.createUser(user);

        EmailToken emailToken = emailTokenOutput.generateToken(userCreated.getUserId());
        MailData mailData = MailData.builder()
                .to(user.getEmail())
                .subject("Confirm your email")
                .text(emailToken.getToken()) // Token
                .build();
        emailConfirmationOutput.sendConfirmationEmail(mailData);
        return userCreated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateUser(Long id, User user) {
        return userCommandPersistencePort.updateUser(id, user);
    }

    @Override
    public void deleteUser(Long userId) {
        userCommandPersistencePort.deleteUser(userId);
    }

}
