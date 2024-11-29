package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import com.edu.unicauca.orii.core.user.application.service.UserCommandService;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.ForgotPasswordRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper.IUserRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password-reset")
public class PasswordResetController {
    private final UserCommandService userCommandService;
    private final IUserRestMapper userRestMapper;

    @PostMapping("/forgotpassword")
    public ResponseEntity<Boolean> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest){
        boolean result=this.userCommandService.forgotPassword(forgotPasswordRequest.getEmail());
        return  ResponseEntity.ok(result);
    }


}
