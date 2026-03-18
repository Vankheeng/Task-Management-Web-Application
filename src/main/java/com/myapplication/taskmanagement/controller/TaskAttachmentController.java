package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.TaskAttachmentRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.TaskAttachmentResponse;
import com.myapplication.taskmanagement.service.TaskAttachmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-attachments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskAttachmentController {
    TaskAttachmentService taskAttachmentService;

    @PostMapping
    APIResponse<TaskAttachmentResponse> createTaskAttachment(
            @RequestBody TaskAttachmentRequest request){
        return APIResponse.<TaskAttachmentResponse>builder()
                .result(taskAttachmentService.createTaskAttachment(request))
                .build();
    }

    @GetMapping("/task/{taskId}")
    APIResponse<List<TaskAttachmentResponse>> getTaskAttachmentsByTaskId(
            @PathVariable String taskId){
        return APIResponse.<List<TaskAttachmentResponse>>builder()
                .result(taskAttachmentService.getTaskAttachmentsByTaskId(taskId))
                .build();
    }

    @PutMapping("/{taskAttachmentId}")
    APIResponse<TaskAttachmentResponse> updateTaskAttachment(
            @PathVariable String taskAttachmentId,
            @RequestBody TaskAttachmentRequest request){
        return APIResponse.<TaskAttachmentResponse>builder()
                .result(taskAttachmentService.updateTaskAttachment(taskAttachmentId, request))
                .build();
    }

    @DeleteMapping("/{taskAttachmentId}")
    APIResponse<String> deleteTaskAttachment(@PathVariable String taskAttachmentId){
        return APIResponse.<String>builder()
                .result(taskAttachmentService.deleteTaskAttachment(taskAttachmentId))
                .build();
    }
}