package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UserCreateRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response.UserData;

@Mapper(componentModel = "spring")
public interface IUserRestMapper {

    @Mapping(target = "users", ignore = true)
    UserData toUserData(User user);

    User toUser(UserCreateRequest userCreateRequest);

    User toUser(UserData userData);

    UserCreateRequest toUserCreateRequest(User user);

    List<UserData> toUserDataList(List<User> users);

    List<User> toUserList(List<UserData> userDataList);


    
}
