package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.AuthenticationRequest;
import com.myapplication.taskmanagement.dto.request.IntrospectRequest;
import com.myapplication.taskmanagement.dto.request.LogoutRequest;
import com.myapplication.taskmanagement.dto.request.RefreshRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.AuthenticationResponse;
import com.myapplication.taskmanagement.dto.response.IntrospectResponse;
import com.myapplication.taskmanagement.dto.response.UserResponse;
import com.myapplication.taskmanagement.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthentictionController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    APIResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return APIResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/verify")
    APIResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return APIResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    APIResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return APIResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    APIResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }


}
