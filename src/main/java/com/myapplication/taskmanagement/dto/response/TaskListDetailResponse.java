package com.myapplication.taskmanagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TaskListDetailResponse {
    String id;
    String name;
    Set<TaskResponse> tasks;
}
