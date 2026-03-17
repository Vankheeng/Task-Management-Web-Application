package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.TaskListRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.TaskListResponse;
import com.myapplication.taskmanagement.service.TaskListService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-lists")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskListController {
    TaskListService taskListService;

    @PostMapping
    APIResponse<TaskListResponse> createTaskList(@RequestBody TaskListRequest request){
        return APIResponse.<TaskListResponse>builder()
                .result(taskListService.createTaskList(request))
                .build();
    }

    @GetMapping("/project/{projectId}")
    APIResponse<List<TaskListResponse>> getTaskListsByProjectId(@PathVariable String projectId){
        return APIResponse.<List<TaskListResponse>>builder()
                .result(taskListService.getTaskListsByProjectId(projectId))
                .build();
    }

    @PutMapping("/{taskListId}")
    APIResponse<TaskListResponse> updateTaskList(@PathVariable String taskListId,
                                                 @RequestBody TaskListRequest request){
        return APIResponse.<TaskListResponse>builder()
                .result(taskListService.updateTaskList(taskListId, request))
                .build();
    }

    @DeleteMapping("/{taskListId}")
    APIResponse<String> deleteTaskList(@PathVariable String taskListId){
        taskListService.deleteTaskList(taskListId);
        return APIResponse.<String>builder()
                .result("Task list  deleted successfully")
                .build();
    }
}