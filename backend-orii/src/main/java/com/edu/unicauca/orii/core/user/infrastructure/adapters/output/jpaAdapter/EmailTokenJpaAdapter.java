package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.edu.unicauca.orii.core.common.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.common.exception.messages.MessageLoader;
import com.edu.unicauca.orii.core.common.exception.messages.MessagesConstant;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailTokenOutput;
import com.edu.unicauca.orii.core.user.domain.model.EmailToken;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.EmailTokenEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.mapper.IEmailTokenMapper;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IEmailTokenRepository;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailTokenJpaAdapter implements IEmailTokenOutput{

    private final IEmailTokenRepository emailTokenRepository;
    private final IUserRepository userRepository;
    private final IEmailTokenMapper emailTokenMapper;

    @Override
    public EmailToken generateToken(Long idUser) {

        UserEntity userEntity = userRepository.findById(idUser) .orElseThrow(() -> new BusinessRuleException(HttpStatus.NOT_FOUND.value(), 
        MessageLoader.getInstance().getMessage(MessagesConstant.EM002, "User", idUser)));
        
        EmailTokenEntity emailToken = EmailTokenEntity.builder()
            .token(UUID.randomUUID().toString())
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusDays(1))
            .user(userEntity)
            .build();
        
        return emailTokenMapper.toDomain(emailTokenRepository.save(emailToken));
    }

    @Override
    public void confirmToken(String token) {
        EmailTokenEntity emailToken = emailTokenRepository.findByToken(token);
        UserEntity userEntity = emailToken.getUser();

        if (emailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException(HttpStatus.FORBIDDEN.value(), 
            MessageLoader.getInstance().getMessage(MessagesConstant.EM002, "EmailToken", token));
        }

        userEntity.setEmailVerified(true);
        userRepository.save(userEntity);

        emailTokenRepository.delete(emailToken);
    }
    
}
