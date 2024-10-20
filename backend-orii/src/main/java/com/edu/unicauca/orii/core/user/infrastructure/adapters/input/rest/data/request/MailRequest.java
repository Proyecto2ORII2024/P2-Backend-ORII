package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MailRequest {
    private String to;
    private String subject;
    private String text;
}
