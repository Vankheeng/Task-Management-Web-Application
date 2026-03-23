package com.myapplication.taskmanagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class ProjectResponse {
    String id;
    String name;
    String description;
    LocalDate createdAt;
    Set<TaskListResponse> taskLists;
    Set<StatusResponse> statuses;
}
