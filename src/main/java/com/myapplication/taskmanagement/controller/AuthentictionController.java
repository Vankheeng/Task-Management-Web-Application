package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.AuthenticationRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.AuthenticationResponse;
import com.myapplication.taskmanagement.dto.response.UserResponse;
import com.myapplication.taskmanagement.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthentictionController {
    AuthenticationService authenticationService;

//    @PostMapping("/log-in")
//    APIResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
//        var result = authenticationService.authen
//    }


}
