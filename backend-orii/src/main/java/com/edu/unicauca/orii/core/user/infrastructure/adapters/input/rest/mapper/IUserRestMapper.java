package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.edu.unicauca.orii.core.user.domain.model.User;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.UserCommonRequest;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.response.UserData;

@Mapper(componentModel = "spring")
public interface IUserRestMapper {

    UserData toUserData(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updatePassword", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "emailToken", ignore = true)
    User toUser(UserCommonRequest userCreateRequest);

    UserCommonRequest toUserCreateRequest(User user);

    List<UserData> toUserDataList(List<User> users);
}
