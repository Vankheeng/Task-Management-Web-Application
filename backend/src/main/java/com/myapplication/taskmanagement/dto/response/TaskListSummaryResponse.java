package com.myapplication.taskmanagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskListSummaryResponse {
    String id;
    String name;
    ProjectSummaryResponse project;
}
