package com.edu.unicauca.orii.core.user.application.ports.output;

public interface IGeneratePasswordUtils {
    String generatePassword();
    String  encryptionPassword(String password);
    boolean comparePasswords(String encryptedPassword, String plainPassword);

}
