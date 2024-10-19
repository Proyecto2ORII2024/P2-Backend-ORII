package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.mapper;


import java.util.List;

import org.mapstruct.Mapper;

import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface IUserAdapterMapper {
    
    User toUser(UserEntity userEntity);

    UserEntity toUserEntity(User user);

    List<User> toUserList(List<UserEntity> userEntities);
}
