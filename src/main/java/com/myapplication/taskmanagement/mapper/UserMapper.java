package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.UserRequest;
import com.myapplication.taskmanagement.dto.request.UserUpdateRequest;
import com.myapplication.taskmanagement.dto.response.UserResponse;
import com.myapplication.taskmanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toUser(UserRequest request);
    UserResponse toUserResponse(User user);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
