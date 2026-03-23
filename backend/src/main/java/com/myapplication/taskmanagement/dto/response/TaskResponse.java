package com.myapplication.taskmanagement.dto.response;

import com.myapplication.taskmanagement.enums.Priority;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TaskResponse {
    String id;
    String title;
    Priority priority;
    LocalDate deadline;
    LocalDate createdAt;
    StatusResponse status;
    Set<TaskAssignmentResponse> taskAssignments;
}
