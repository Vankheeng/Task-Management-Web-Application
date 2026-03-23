package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.TeamRequest;
import com.myapplication.taskmanagement.dto.request.UserRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.TeamResponse;
import com.myapplication.taskmanagement.dto.response.UserResponse;
import com.myapplication.taskmanagement.service.TeamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamController {
    TeamService teamService;
    @PostMapping
    APIResponse<TeamResponse> createTeam(@RequestBody TeamRequest request) {
        return APIResponse.<TeamResponse>builder()
                .result(teamService.createTeam(request))
                .build();
    }

    @GetMapping
    APIResponse<List<TeamResponse>> getMyTeams(){
        return APIResponse.<List<TeamResponse>>builder()
                .result(teamService.getMyTeams())
                .build();
    }

    @PutMapping("/{teamId}")
    APIResponse<TeamResponse> updateTeam(@PathVariable("teamId") String teamId ,@RequestBody TeamRequest request) {
        return APIResponse.<TeamResponse>builder()
                .result(teamService.updateTeam(teamId, request))
                .build();
    }

    @DeleteMapping("/{teamId}")
    APIResponse<String> deleteTeam(@PathVariable("teamId") String teamId) {
        teamService.deleteTeam(teamId);

        return APIResponse.<String>builder()
                .result("Team deleted successfully")
                .build();
    }
}
