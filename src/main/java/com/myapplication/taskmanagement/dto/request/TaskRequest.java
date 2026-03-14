package com.myapplication.taskmanagement.dto.request;

import com.myapplication.taskmanagement.enums.Priority;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TaskRequest {
    String title;
    String description;
    Priority priority;
    LocalDate deadline;
    String statusId;
    String listId;
}
