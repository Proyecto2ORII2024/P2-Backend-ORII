package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UserCreateRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UserUpdateRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response.UserData;

@Mapper(componentModel = "spring")
public interface IUserRestMapper {

    UserData toUserData(User user);

    @Mapping(target = "updatePassword", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(UserCreateRequest userCreateRequest);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "updatePassword", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    User toUser(UserUpdateRequest userUpdateRequest);

    UserCreateRequest toUserCreateRequest(User user);

    List<UserData> toUserDataList(List<User> users);

    List<User> toUserList(List<UserData> userDataList);
    
}
