package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.TeamMemberRequest;
import com.myapplication.taskmanagement.dto.request.TeamRequest;
import com.myapplication.taskmanagement.dto.response.TeamResponse;
import com.myapplication.taskmanagement.entity.Team;
import com.myapplication.taskmanagement.entity.TeamMember;
import com.myapplication.taskmanagement.enums.Role;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.TeamMapper;
import com.myapplication.taskmanagement.repository.*;
import com.myapplication.taskmanagement.utils.SecurityUtils;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamService {
    TeamRepository teamRepository;
    TeamMapper teamMapper;
    TeamMemberRepository teamMemberRepository;
    ProjectRepository projectRepository;
    TaskListRepository taskListRepository;
    TaskRepository taskRepository;
    TeamMemberService teamMemberService;
    EntityManager entityManager;
    TaskAssignmentRepository taskAssignmentRepository;
    TaskAttachmentRepository taskAttachmentRepository;
    CommentRepository commentRepository;
    NotificationRepository notificationRepository;
    StatusRepository statusRepository;
    SecurityUtils securityUtils;

    @Transactional
    public TeamResponse createTeam(TeamRequest request){
        Team team = teamMapper.toTeam(request);
        team.setCreatedAt(LocalDate.now());
        team = teamRepository.save(team);

        var userId = SecurityUtils.getCurrentUserId();

        TeamMemberRequest teamMemberRequest = TeamMemberRequest.builder()
                .userId(userId)
                .teamId(team.getId())
                .role(Role.ADMIN)
                .build();

        teamMemberService.createTeamMember(teamMemberRequest);

        entityManager.flush();
        entityManager.clear();

        team = teamRepository.findByIdWithMembers(team.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_EXISTED));

        return teamMapper.toTeamResponse(team);
    }

    public List<TeamResponse> getMyTeams(){
        var userId = SecurityUtils.getCurrentUserId();

        List<TeamMember> teamMembers = teamMemberRepository.findAllByUserIdAndActive(userId, true);
        return teamMembers.stream()
                    .map(TeamMember::getTeam)
                    .distinct()
                    .map(teamMapper::toTeamResponse)
                    .toList();
    }

    public TeamResponse updateTeam(String teamId, TeamRequest request){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_EXISTED));

        String userId = SecurityUtils.getCurrentUserId();

        TeamMember teamMember = teamMemberRepository.findByTeamIdAndUserIdAndActive(teamId, userId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAMMEMBER_NOT_EXISTED));

        if(!teamMember.getRole().equals(Role.ADMIN)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        teamMapper.updateTeam(team, request);

        return teamMapper.toTeamResponse(teamRepository.save(team));
    }

    @Transactional
    public void deleteTeam(String teamId){
        Team team = teamRepository.findByIdAndActive(teamId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_EXISTED));

        securityUtils.checkAdminRole(teamId);

        team.setActive(false);
        teamRepository.save(team);
        teamMemberRepository.deactivateAllByTeamId(teamId);
        projectRepository.deactivateAllByTeamId(teamId);
        taskListRepository.deactivateAllByProjectTeamId(teamId);
        taskRepository.deactivateAllByTeamId(teamId);
        taskAssignmentRepository.deactivateAllByTeamId(teamId);
        taskAttachmentRepository.deactivateAllByTeamId(teamId);
        commentRepository.deactivateAllByTeamId(teamId);
        notificationRepository.deactivateAllByTeamId(teamId);
        statusRepository.deactivateAllByTeamId(teamId);
    }
}
