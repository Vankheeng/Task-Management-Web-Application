package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.TaskAssignmentRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.TaskAssignmentResponse;
import com.myapplication.taskmanagement.service.TaskAssignmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-assignments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskAssignmentController {
    TaskAssignmentService taskAssignmentService;

    @PostMapping
    APIResponse<TaskAssignmentResponse> createTaskAssignment(
            @RequestBody TaskAssignmentRequest request){
        return APIResponse.<TaskAssignmentResponse>builder()
                .result(taskAssignmentService.createTaskAssignment(request))
                .build();
    }

    @GetMapping("/task/{taskId}")
    APIResponse<List<TaskAssignmentResponse>> getTaskAssignmentsByTaskId(
            @PathVariable String taskId){
        return APIResponse.<List<TaskAssignmentResponse>>builder()
                .result(taskAssignmentService.getTaskAssignmentsByTaskId(taskId))
                .build();
    }

    @GetMapping("/my-tasks")
    APIResponse<List<TaskAssignmentResponse>> getMyTaskAssignments(){
        return APIResponse.<List<TaskAssignmentResponse>>builder()
                .result(taskAssignmentService.getMyTaskAssignments())
                .build();
    }

    @GetMapping("/my-tasks/team/{teamId}")
    APIResponse<List<TaskAssignmentResponse>> getMyTaskAssignmentsByTeam(
            @PathVariable String teamId){
        return APIResponse.<List<TaskAssignmentResponse>>builder()
                .result(taskAssignmentService.getMyTaskAssignmentsByTeam(teamId))
                .build();
    }

    @PutMapping("/{taskAssignmentId}")
    APIResponse<TaskAssignmentResponse> updateTaskAssignment(
            @PathVariable String taskAssignmentId,
            @RequestBody TaskAssignmentRequest request){
        return APIResponse.<TaskAssignmentResponse>builder()
                .result(taskAssignmentService.updateTaskAssignment(taskAssignmentId, request))
                .build();
    }

    @DeleteMapping("/{taskAssignmentId}")
    APIResponse<String> deleteTaskAssignment(@PathVariable String taskAssignmentId){
        return APIResponse.<String>builder()
                .result(taskAssignmentService.deleteTaskAssignment(taskAssignmentId))
                .build();
    }
}