package com.edu.unicauca.orii.core.user.application.service;

import org.springframework.stereotype.Service;
import com.edu.unicauca.orii.core.user.application.ports.input.IEmailConfirmationInput;
import com.edu.unicauca.orii.core.user.application.ports.input.IUserCommandPort;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailTokenOutput;
import com.edu.unicauca.orii.core.user.application.ports.output.IGeneratePasswordUtils;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserCommandPersistencePort;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserQueryPersistencePort;
import com.edu.unicauca.orii.core.user.domain.enums.RoleEnum;
import com.edu.unicauca.orii.core.user.domain.model.MailData;
import com.edu.unicauca.orii.core.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandService implements IUserCommandPort {

    private final IUserCommandPersistencePort userCommandPersistencePort;
    private final IUserQueryPersistencePort userQueryPersistencePort;
    private final IEmailConfirmationInput emailConfirmationInput;
    private final IGeneratePasswordUtils generatePasswordUtils;
    private final IEmailConfirmationOutput emailConfirmationOutput;
    private final IEmailTokenOutput emailTokenOutput;

    @Override
    public User createUser(User user) {
        String password = this.generatePasswordUtils.generatePassword();
        System.out.println(password);
        user.setPassword(generatePasswordUtils.encryptionPassword(password));
        User userCreated = userCommandPersistencePort.createUser(user);

        userCreated.setPassword(password);
        emailConfirmationInput.sendConfirmationEmail(userCreated);

        return userCreated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userQueryPersistencePort.getUserById(id);

        if (Boolean.TRUE.equals(existingUser.getEmailVerified())) {
            existingUser.setRole(user.getRole());
            existingUser.setFaculty(user.getFaculty());
        } else {
            existingUser.setRole(user.getRole());
            existingUser.setFaculty(user.getFaculty());
            if (!existingUser.getEmail().equals(user.getEmail())) {
                //delete token if exists
                emailTokenOutput.deleteToken(id);

                existingUser.setEmail(user.getEmail());
                String password = this.generatePasswordUtils.generatePassword();
                existingUser.setPassword(generatePasswordUtils.encryptionPassword(password));

                User userUpdate= userCommandPersistencePort.updateUser(id, existingUser);
                userUpdate.setPassword(password);

                emailConfirmationInput.sendConfirmationEmail(userUpdate);
            }
        }

        if (RoleEnum.ADMIN.equals(user.getRole())) {
            existingUser.setFaculty(null);
        }

        return userCommandPersistencePort.updateUser(id, existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userCommandPersistencePort.deleteUser(userId);
    }

    private void sendPasswordEmail(String email, String password) {

        MailData mailData = MailData.builder()
                .to(email)
                .subject("reset password")
                .text(password)
                .build();
        emailConfirmationOutput.sendPasswordEmail(mailData);
    }

    @Override
    public boolean forgotPassword(String email) {
        boolean bandera = false;
        if (userCommandPersistencePort.existByEmail(email) != false) {
            String password = this.generatePasswordUtils.generatePassword();
            sendPasswordEmail(email, password);
            bandera = userCommandPersistencePort.forgotPassword(email,
                    generatePasswordUtils.encryptionPassword(password));
        }
        return bandera;
    }

}
