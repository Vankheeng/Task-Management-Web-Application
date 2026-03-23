package com.myapplication.taskmanagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TaskAttachmentRequest {
    String fileName;
    String fileUrl;
    String taskId;
}
