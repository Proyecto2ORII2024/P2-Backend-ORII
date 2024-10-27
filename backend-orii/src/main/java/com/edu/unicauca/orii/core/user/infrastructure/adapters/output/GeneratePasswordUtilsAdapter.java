package com.edu.unicauca.orii.core.user.infrastructure.adapters.output;

import com.edu.unicauca.orii.core.user.application.ports.output.IGeneratePasswordUtils;

import lombok.AllArgsConstructor;

import java.security.SecureRandom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
@AllArgsConstructor
public class GeneratePasswordUtilsAdapter implements IGeneratePasswordUtils{
    private final PasswordEncoder passwordEncoder;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();



    @Override
    public String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    @Override
    public String encryptionPassword(String password) {
       return passwordEncoder.encode(password);
    }
    
}
