package com.myapplication.taskmanagement.dto.request;

import com.myapplication.taskmanagement.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {
    String type;
    String message;
    Boolean isRead;
    String taskId;
    String projectId;
    String userId;
}
