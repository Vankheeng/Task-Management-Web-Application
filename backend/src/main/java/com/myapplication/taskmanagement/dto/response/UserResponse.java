package com.myapplication.taskmanagement.dto.response;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String fullName;
    String email;
    String phoneNumber;
    LocalDate dob;
}
