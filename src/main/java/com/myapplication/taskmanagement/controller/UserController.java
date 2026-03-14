package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.UserRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.UserResponse;
import com.myapplication.taskmanagement.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    APIResponse<UserResponse> createUser(@RequestBody UserRequest request){
        return APIResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

}
