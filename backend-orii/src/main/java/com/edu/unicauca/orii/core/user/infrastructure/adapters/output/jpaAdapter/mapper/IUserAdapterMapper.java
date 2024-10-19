package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface IUserAdapterMapper {
    
    @Mapping(target = "users", ignore = true)
    User toUser(UserEntity userEntity);

    @Mapping(target = "users", ignore = true)
    UserEntity toUserEntity(User user);

    List<User> toUserList(List<UserEntity> userEntities);
}
