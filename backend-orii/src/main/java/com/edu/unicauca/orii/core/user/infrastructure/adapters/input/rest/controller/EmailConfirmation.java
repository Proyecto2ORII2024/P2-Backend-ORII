package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.edu.unicauca.orii.core.user.application.ports.input.IEmailTokenInput;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailConfirmation {

    private final IEmailTokenInput emailTokenInput;

    @GetMapping("/confirmEmail/{token}")
    public String confirmEmail(@PathVariable String token, Model model) {
        model.addAttribute("isValid", emailTokenInput.confirmToken(token));
        return "confirmation";
    }

    //probar plantilla HTML
    @GetMapping("test")
    public String test(Model model) {
        model.addAttribute("isValid", true);
        return "confirmation";
    }


}
