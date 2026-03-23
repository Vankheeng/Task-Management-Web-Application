package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.TeamMemberRequest;
import com.myapplication.taskmanagement.dto.request.TeamMemberUpdateRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.TeamMemberResponse;
import com.myapplication.taskmanagement.service.TeamMemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team-members")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamMemberController {
    TeamMemberService teamMemberService;

    @PostMapping
    APIResponse<TeamMemberResponse> createTeamMember(@RequestBody TeamMemberRequest request) {
        return APIResponse.<TeamMemberResponse>builder()
                .result(teamMemberService.createTeamMember(request))
                .build();
    }

    @GetMapping("/team/{teamId}")
    APIResponse<List<TeamMemberResponse>> getTeamMemberByTeamId(@PathVariable String teamId){
        return APIResponse.<List<TeamMemberResponse>>builder()
                .result(teamMemberService.getTeamMembersByTeamId(teamId))
                .build();
    }

    @PutMapping("/{teamMemberId}")
    APIResponse<TeamMemberResponse> updateTeam(
            @PathVariable("teamMemberId") String teamMemberId ,@RequestBody TeamMemberUpdateRequest request) {
        return APIResponse.<TeamMemberResponse>builder()
                .result(teamMemberService.updateTeamMember(teamMemberId, request))
                .build();
    }

    @DeleteMapping("/{teamMemberId}")
    APIResponse<String> deleteMemberTeam(@PathVariable("teamMemberId") String teamMemberId) {
        teamMemberService.deleteTeamMember(teamMemberId);

        return APIResponse.<String>builder()
                .result("Team member deleted successfully")
                .build();
    }
}
