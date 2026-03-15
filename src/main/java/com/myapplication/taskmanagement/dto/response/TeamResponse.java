package com.myapplication.taskmanagement.dto.response;


import com.myapplication.taskmanagement.entity.Project;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TeamResponse {
    String id;
    String name;
    LocalDate createdAt;
    Set<TeamMemberResponse> teamMembers;
    Set<Project> projects;
}
