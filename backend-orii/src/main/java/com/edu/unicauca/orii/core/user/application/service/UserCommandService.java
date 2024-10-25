package com.edu.unicauca.orii.core.user.application.service;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.common.formatter.IFormFormatterResultOutputPort;
import com.edu.unicauca.orii.core.user.application.ports.input.IUserCommandPort;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailTokenOutput;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserCommandPersistencePort;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserQueryPersistencePort;
import com.edu.unicauca.orii.core.user.domain.model.EmailToken;
import com.edu.unicauca.orii.core.user.domain.model.MailData;
import com.edu.unicauca.orii.core.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandService implements IUserCommandPort {

    private final IUserCommandPersistencePort userCommandPersistencePort;
    private final IUserQueryPersistencePort userQueryPersistencePort;
    private final IEmailConfirmationOutput emailConfirmationOutput;
    private final IEmailTokenOutput emailTokenOutput;
    private final IFormFormatterResultOutputPort formFormatterResultOutputPort;

    @Override
    public User createUser(User user) {

        User userCreated = userCommandPersistencePort.createUser(user);
        sendConfirmationEmail(userCreated);

        return userCreated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userQueryPersistencePort.getUserById(id);

        if (Boolean.TRUE.equals(existingUser.getEmailVerified())) {
            formFormatterResultOutputPort.returnResponseErrorTeacherRequired("User email is verified, cannot update.");
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());

        return userCommandPersistencePort.updateUser(id, user);
    }

    @Override
    public void deleteUser(Long userId) {
        userCommandPersistencePort.deleteUser(userId);
    }

    private void sendConfirmationEmail(User user) {
        EmailToken emailToken = emailTokenOutput.generateToken(user.getUserId());
        MailData mailData = MailData.builder()
                .to(user.getEmail())
                .subject("Confirm your email")
                .text(emailToken.getToken()) // Token
                .build();
        emailConfirmationOutput.sendConfirmationEmail(mailData);
    }

}
