package com.myapplication.taskmanagement.dto.response;

import com.myapplication.taskmanagement.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    String id;
    NotificationType type;
    String message;
    Boolean isRead;
    LocalDate createdAt;
    TaskResponse task;
    ProjectResponse project;
}
