package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.TaskRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.TaskResponse;
import com.myapplication.taskmanagement.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
    TaskService taskService;

    @PostMapping
    APIResponse<TaskResponse> createTask(@RequestBody TaskRequest request){
        return APIResponse.<TaskResponse>builder()
                .result(taskService.createTask(request))
                .build();
    }

    @GetMapping("/task-list/{taskListId}")
    APIResponse<List<TaskResponse>> getTasksByTaskListId(@PathVariable String taskListId){
        return APIResponse.<List<TaskResponse>>builder()
                .result(taskService.getTasksByTaskListId(taskListId))
                .build();
    }

    @PutMapping("/{taskId}")
    APIResponse<TaskResponse> updateTask(@PathVariable String taskId,
                                         @RequestBody TaskRequest request){
        return APIResponse.<TaskResponse>builder()
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