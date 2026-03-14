package com.myapplication.taskmanagement.dto.response;

import com.myapplication.taskmanagement.enums.Priority;
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
public class TaskResponse {
    String id;
    String title;
    String description;
    Priority priority;
    LocalDate deadline;
    LocalDate createdAt;
    StatusResponse status;
    Set<CommentResponse> comments;
    Set<TaskAssignmentResponse> taskAssignments;
    Set<TaskAttachmentResponse> taskAttachments;
}
