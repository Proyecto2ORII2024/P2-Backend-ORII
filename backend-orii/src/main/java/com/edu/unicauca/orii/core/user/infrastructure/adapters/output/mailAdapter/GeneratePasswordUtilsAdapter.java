package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.mailAdapter;

import com.edu.unicauca.orii.core.user.application.ports.output.IGeneratePasswordUtils;

import lombok.AllArgsConstructor;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
@AllArgsConstructor
public class GeneratePasswordUtilsAdapter implements IGeneratePasswordUtils{
    private final PasswordEncoder passwordEncoder;
    private static final String UPPERCASE="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE="abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS="0123456789";
    private static final String SPECIALCHARACTERS="@$!*?&^#()_-+=";
    private static final String ALLCHARACTERS=UPPERCASE+LOWERCASE+DIGITS+SPECIALCHARACTERS;
    private static final  int passwordLenght=12;
    private static final SecureRandom random = new SecureRandom();



    @Override
    public String generatePassword() {
        //passwordLenght=generateNumber();
        ArrayList<Character> passwordChars=new ArrayList<>(passwordLenght);

        passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIALCHARACTERS.charAt(random.nextInt(SPECIALCHARACTERS.length())));

        for(int i=4;i<passwordLenght;i++){
            passwordChars.add(ALLCHARACTERS.charAt(random.nextInt(ALLCHARACTERS.length())));
        }
        Collections.shuffle(passwordChars);

        StringBuilder password=new StringBuilder();
        for(char ch: passwordChars){
            password.append(ch);
        }
        return password.toString();
    
    }

    @Override
    public String encryptionPassword(String password) {
       return passwordEncoder.encode(password);
    }
    public int generateNumber(){
        Random random = new Random();
        return random.nextInt(9) + 8; 
    }
    
}
