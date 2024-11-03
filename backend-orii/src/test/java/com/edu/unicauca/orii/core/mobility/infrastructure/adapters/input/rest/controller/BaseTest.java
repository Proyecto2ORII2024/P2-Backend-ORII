package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.controller;

import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.application.service.EmailService;
import org.springframework.boot.test.mock.mockito.MockBean;

public class BaseTest {
    @MockBean
    protected EmailService emailService;

    @MockBean
    protected IEmailConfirmationOutput emailConfirmationOutput;
}
