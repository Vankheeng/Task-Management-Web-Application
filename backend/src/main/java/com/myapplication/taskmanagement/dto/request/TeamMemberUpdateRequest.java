package com.myapplication.taskmanagement.dto.request;

import com.myapplication.taskmanagement.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TeamMemberUpdateRequest {
    Role role;
}
