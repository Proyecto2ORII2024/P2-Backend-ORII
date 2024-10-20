package com.edu.unicauca.orii.core.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailToken {
    
    private Long id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    private User user;   
}
