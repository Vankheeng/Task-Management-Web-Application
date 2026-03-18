package com.myapplication.taskmanagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TaskAttachmentResponse {
    String id;
    String fileName;
    String fileUrl;
    LocalDate createdAt;
    UserResponse createdBy;
}
