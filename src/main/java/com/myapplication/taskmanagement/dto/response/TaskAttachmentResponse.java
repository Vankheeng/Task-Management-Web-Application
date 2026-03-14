package com.myapplication.taskmanagement.dto.response;

import com.myapplication.taskmanagement.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

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
    UserResponse createdBy; //Username
}
