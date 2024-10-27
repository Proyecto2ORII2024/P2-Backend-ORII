package com.edu.unicauca.orii.core.user.application.service;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.common.formatter.IFormFormatterResultOutputPort;
import com.edu.unicauca.orii.core.user.application.ports.input.IEmailConfirmationInput;
import com.edu.unicauca.orii.core.user.application.ports.input.IUserCommandPort;
import com.edu.unicauca.orii.core.user.application.ports.output.IGeneratePasswordUtils;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserCommandPersistencePort;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserQueryPersistencePort;
import com.edu.unicauca.orii.core.user.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandService implements IUserCommandPort {

    private final IUserCommandPersistencePort userCommandPersistencePort;
    private final IUserQueryPersistencePort userQueryPersistencePort;
    private final IEmailConfirmationInput emailConfirmationInput;
    private final IFormFormatterResultOutputPort formFormatterResultOutputPort;
    private final IGeneratePasswordUtils generatePasswordUtils;

    @Override
    public User createUser(User user) {
        String password =this.generatePasswordUtils.generatePassword();
        System.out.println(password);
        user.setPassword(generatePasswordUtils.encryptionPassword(password));
        User userCreated = userCommandPersistencePort.createUser(user);
        
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

}
