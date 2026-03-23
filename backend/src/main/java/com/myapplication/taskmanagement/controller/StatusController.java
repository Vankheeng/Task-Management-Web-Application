package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.StatusRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.StatusResponse;
import com.myapplication.taskmanagement.service.StatusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statuses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusController {
    StatusService statusService;

    @PostMapping
    APIResponse<StatusResponse> createStatus(@RequestBody StatusRequest request){
        return APIResponse.<StatusResponse>builder()
                .result(statusService.createStatus(request))
                .build();
    }

    @GetMapping("/project/{projectId}")
    APIResponse<List<StatusResponse>> getStatusesByProjectId(@PathVariable String projectId){
        return APIResponse.<List<StatusResponse>>builder()
                .result(statusService.getStatusesByProjectId(projectId))
                .build();
    }

    @PutMapping("/{statusId}")
    APIResponse<StatusResponse> updateStatus(@PathVariable String statusId,
                                             @RequestBody StatusRequest request){
        return APIResponse.<StatusResponse>builder()
                .result(statusService.updateStatus(statusId, request))
                .build();
    }

    @DeleteMapping("/{statusId}")
    APIResponse<String> deleteStatus(@PathVariable String statusId){
        return APIResponse.<String>builder()
                .result(statusService.deleteStatus(statusId))
                .build();
    }
}