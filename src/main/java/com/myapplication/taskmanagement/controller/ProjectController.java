package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.ProjectRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.ProjectResponse;
import com.myapplication.taskmanagement.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectController {
    ProjectService projectService;

    @PostMapping
    APIResponse<ProjectResponse> createProject(@RequestBody ProjectRequest request){
        return APIResponse.<ProjectResponse>builder()
                .result(projectService.createProject(request))
                .build();
    }

    @GetMapping("/team/{teamId}")
    APIResponse<List<ProjectResponse>> getProjectsByTeamId(@PathVariable String teamId){
        return APIResponse.<List<ProjectResponse>>builder()
                .result(projectService.getProjectsByTeamId(teamId))
                .build();
    }

    @PutMapping("/{projectId}")
    APIResponse<ProjectResponse> updateProject(@PathVariable String projectId,
                                               @RequestBody ProjectRequest request){
        return APIResponse.<ProjectResponse>builder()
                .result(projectService.updateProject(projectId, request))
                .build();
    }

    @DeleteMapping("/{projectId}")
    APIResponse<String> deleteProject(@PathVariable String projectId){
        projectService.deleteProject(projectId);
        return APIResponse.<String>builder()
                .result("Project deleted successfully")
                .build();
    }
}
