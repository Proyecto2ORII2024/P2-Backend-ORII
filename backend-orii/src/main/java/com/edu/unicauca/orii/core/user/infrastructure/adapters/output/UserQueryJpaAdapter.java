package com.edu.unicauca.orii.core.user.infrastructure.adapters.output;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.exception.messages.MessageLoader;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.exception.messages.MessagesConstant;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserQueryPersistencePort;
import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.mapper.IUserAdapterMapper;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserQueryJpaAdapter implements IUserQueryPersistencePort {
    
    private final IUserAdapterMapper userAdapterMapper;
    private final IUserRepository userRepository;

    @Override
    public Page<User> getUser(Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        if(userEntities.getContent().isEmpty()) {
             throw new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                        MessageLoader.getInstance().getMessage(MessagesConstant.EM014,"User" ));
        }

        return userEntities.map(userAdapterMapper::toUser);
    }
    @Override
    public User getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId) .orElseThrow(() -> new BusinessRuleException(HttpStatus.NOT_FOUND.value(), 
        MessageLoader.getInstance().getMessage(MessagesConstant.EM002, "User", userId)));

        return userAdapterMapper.toUser(userEntity);
    }


}
