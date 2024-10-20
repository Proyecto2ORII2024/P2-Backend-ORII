package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.exception.messages.MessageLoader;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.exception.messages.MessagesConstant;
import com.edu.unicauca.orii.core.user.application.ports.output.IUserCommandPersistencePort;
import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.mapper.IUserAdapterMapper;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserCommandJpaAdapter implements IUserCommandPersistencePort {

    private final IUserRepository userRepository;

    private final IUserAdapterMapper userAdapterMapper;

    @Override
    public User createUser(User user) {
        return userAdapterMapper.toUser(userRepository.save(userAdapterMapper.toUserEntity(user)));
    }

    @Override
    public User updateUser(Long id, User user) {
        if(!userRepository.existsById(id)) {
              throw new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                    MessageLoader.getInstance().getMessage(MessagesConstant.EM002, "Agreement", id));
        }

        return userAdapterMapper.toUser(userRepository.save(userAdapterMapper.toUserEntity(user)));
    }

    @Override
    public void deleteUser(Long userId) {
       Optional<UserEntity> userEntity = userRepository.findById(userId);

       if(userEntity.isEmpty()) {
           throw new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                   MessageLoader.getInstance().getMessage(MessagesConstant.EM002, "Agreement", userId));
       }

       userRepository.deleteById(userId);
    }

    
    
}
