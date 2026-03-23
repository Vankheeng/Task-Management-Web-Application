package com.myapplication.taskmanagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TaskListResponse {
    String id;
    String name;
    String createdAt;
    Set<TaskSummaryResponse> tasks;
}
