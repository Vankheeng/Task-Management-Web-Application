package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.UserRequest;
import com.myapplication.taskmanagement.dto.response.UserResponse;
import com.myapplication.taskmanagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest request);
    UserResponse toUserResponse(User user);
}
