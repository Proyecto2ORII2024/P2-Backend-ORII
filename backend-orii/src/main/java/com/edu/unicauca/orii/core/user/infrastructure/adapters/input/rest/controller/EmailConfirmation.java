package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.unicauca.orii.core.user.application.ports.input.IEmailConfirmationInput;
import com.edu.unicauca.orii.core.user.application.ports.input.IEmailTokenInput;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.MailRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper.IMailRestMapper;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
public class EmailConfirmation {

    private final IEmailConfirmationInput emailConfirmationInput;
    private final IEmailTokenInput emailTokenInput;
    private final IMailRestMapper mailRestMapper;
    
    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody MailRequest mailRequest) {
        emailConfirmationInput.sendConfirmationEmail(mailRestMapper.toRequestMailData(mailRequest));
        
    }

    @GetMapping("/confirmEmail/{token}")
    public void confirmEmail(@PathVariable String token) {
        emailTokenInput.confirmToken(token);
    }


}
