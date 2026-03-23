package com.myapplication.taskmanagement.dto.response;

import com.myapplication.taskmanagement.enums.Priority;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDetailResponse {
    String id;
    String title;
    String description;
    Priority priority;
    LocalDate deadline;
    LocalDate createdAt;
    StatusResponse status;
    Set<TaskAssignmentResponse> taskAssignments;
    Set<TaskAttachmentResponse> taskAttachments;
    Set<CommentResponse> comments;
    TaskListSummaryResponse taskList;
}