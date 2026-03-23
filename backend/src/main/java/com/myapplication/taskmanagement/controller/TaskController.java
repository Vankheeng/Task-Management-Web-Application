package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.TaskRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.TaskDetailResponse;
import com.myapplication.taskmanagement.dto.response.TaskResponse;
import com.myapplication.taskmanagement.dto.response.TaskSummaryResponse;
import com.myapplication.taskmanagement.entity.TaskAssignment;
import com.myapplication.taskmanagement.repository.TaskAssignmentRepository;
import com.myapplication.taskmanagement.service.TaskService;
import com.myapplication.taskmanagement.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
    TaskService taskService;
    SecurityUtils securityUtils;
    TaskAssignmentRepository taskAssignmentRepository;

    @PostMapping
    APIResponse<TaskDetailResponse> createTask(@RequestBody TaskRequest request){
        return APIResponse.<TaskDetailResponse>builder()
                .result(taskService.createTask(request))
                .build();
    }

    @GetMapping("/task-list/{taskListId}")
    APIResponse<List<TaskResponse>> getTasksByTaskListId(@PathVariable String taskListId){
        return APIResponse.<List<TaskResponse>>builder()
                .result(taskService.getTasksByTaskListId(taskListId))
                .build();
    }

    @GetMapping("/my-tasks")
    APIResponse<List<TaskResponse>> getMyTask(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDay,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDay){
        return APIResponse.<List<TaskResponse>>builder()
                .result(taskService.getMyTask(startDay, endDay))
                .build();
    }


    @GetMapping("/{taskId}")
    APIResponse<TaskDetailResponse> getTaskById(@PathVariable String taskId){
        return APIResponse.<TaskDetailResponse>builder()
                .result(taskService.getTaskById(taskId))
                .build();
    }

    @PutMapping("/{taskId}")
    APIResponse<TaskDetailResponse> updateTask(@PathVariable String taskId,
                                         @RequestBody TaskRequest request){
        return APIResponse.<TaskDetailResponse>builder()
                .result(taskService.updateTask(taskId, request))
                .build();
    }

    @DeleteMapping("/{taskId}")
    APIResponse<String> deleteTask(@PathVariable String taskId){
        return APIResponse.<String>builder()
                .result(taskService.deleteTask(taskId))
                .build();
    }
}