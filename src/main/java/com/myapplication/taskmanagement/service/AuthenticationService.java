package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.AuthenticationRequest;
import com.myapplication.taskmanagement.dto.response.AuthenticationResponse;
import com.myapplication.taskmanagement.entity.User;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.repository.InvalidatedRepository;
import com.myapplication.taskmanagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
//    UserRepository userRepository;
//    InvalidatedRepository invalidatedRepository;
//
//    @Value("${jwt.valid-duration}")
//    long VALID_DURATION;
//
//    @Value("${jwt.signerKey}")
//    String SIGNED_KEY;

//    public AuthenticationResponse authenticate(AuthenticationRequest request){
//        var user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
//
//        if(!authenticated){
//            throw new AppException(ErrorCode.UNAUTHENTICATED);
//        }
//
//       var token =
//    }

//    private String generateToken(User user){
//        JWSHeader
//    }

}
