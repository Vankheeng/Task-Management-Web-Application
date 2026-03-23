package com.myapplication.taskmanagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class UserRequest {
    String username;
    String password;
    String fullName;
    String email;
    String phoneNumber;
    LocalDate dob;
}
