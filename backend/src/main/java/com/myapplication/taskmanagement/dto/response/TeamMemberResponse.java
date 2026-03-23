package com.myapplication.taskmanagement.dto.response;


import com.myapplication.taskmanagement.entity.User;
import com.myapplication.taskmanagement.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TeamMemberResponse {
    String id;
    UserResponse user;
    Role role;
}
