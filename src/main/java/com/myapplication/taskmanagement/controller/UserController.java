package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.PasswordUpdateRequest; // You'll need this DTO
import com.myapplication.taskmanagement.dto.request.UserRequest;
import com.myapplication.taskmanagement.dto.request.UserUpdateRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.UserResponse;
import com.myapplication.taskmanagement.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    APIResponse<UserResponse> createUser(@RequestBody UserRequest request) {
        return APIResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping("/{userId}")
    APIResponse<UserResponse> getUser(@PathVariable String userId) {
        return APIResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }

    @GetMapping("/username/{username}")
    APIResponse<UserResponse> getUserByUsername(@PathVariable String username) {
        return APIResponse.<UserResponse>builder()
                .result(userService.getUserByUsername(username))
                .build();
    }

    @GetMapping("/email/{email}")
    APIResponse<UserResponse> getUserByEmail(@PathVariable String email) {
        return APIResponse.<UserResponse>builder()
                .result(userService.getUserByEmail(email))
                .build();
    }

    @GetMapping("/phone/{phoneNumber}")
    APIResponse<UserResponse> getUserByPhone(@PathVariable String phoneNumber) {
        return APIResponse.<UserResponse>builder()
                .result(userService.getUserByPhoneNumber(phoneNumber))
                .build();
    }

    @PutMapping()
    APIResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {
        return APIResponse.<UserResponse>builder()
                .result(userService.updateUser(request))
                .build();
    }

    // 5. Update Password
    @PutMapping("/password")
    APIResponse<String> updatePassword(@RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(request);
        return APIResponse.<String>builder()
                .result("Password updated successfully")
                .build();
    }

    @GetMapping("/my-infor")
    APIResponse<UserResponse> getMyInfor() {
        return APIResponse.<UserResponse>builder()
                .result(userService.getMyInfor())
                .build();
    }

}