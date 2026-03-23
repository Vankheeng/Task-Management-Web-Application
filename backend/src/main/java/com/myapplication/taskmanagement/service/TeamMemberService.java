package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.TeamMemberRequest;
import com.myapplication.taskmanagement.dto.request.TeamMemberUpdateRequest;
import com.myapplication.taskmanagement.dto.response.TeamMemberResponse;
import com.myapplication.taskmanagement.entity.*;
import com.myapplication.taskmanagement.enums.Role;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.TeamMemberMapper;
import com.myapplication.taskmanagement.repository.ProjectRepository;
import com.myapplication.taskmanagement.repository.TeamMemberRepository;
import com.myapplication.taskmanagement.repository.TeamRepository;
import com.myapplication.taskmanagement.repository.UserRepository;
import com.myapplication.taskmanagement.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamMemberService {
    TeamMemberRepository teamMemberRepository;
    TeamMemberMapper teamMemberMapper;
    TeamRepository teamRepository;
    UserRepository userRepository;
    NotificationService notificationService;
    SecurityUtils securityUtils;


    public TeamMemberResponse createTeamMember(TeamMemberRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Team team = teamRepository.findByIdAndActive(request.getTeamId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_EXISTED));

        boolean notify = false;
        if(!teamMemberRepository.findAllByTeamIdAndActive(request.getTeamId(), true).isEmpty()){
            if(teamMemberRepository.existsByTeam_IdAndUser_IdAndActive(request.getTeamId(), request.getUserId(), true)){
                throw new AppException(ErrorCode.TEAMMEMBER_EXISTED);
            }
            securityUtils.checkAdminRole(request.getTeamId());
            notify = true;
        }

        TeamMember teamMember = TeamMember.builder()
                .user(user)
                .team(team)
                .role(request.getRole())
                .build();

        teamMember = teamMemberRepository.save(teamMember);
        if(notify){
            notificationService.notifyTeamAdded(user, team);
        }

        return teamMemberMapper.toTeamMemberResponse(teamMember);
    }

    public List<TeamMemberResponse> getTeamMembersByTeamId(String teamId){

        if(!teamRepository.existsByIdAndActive(teamId, true)){
            throw new AppException(ErrorCode.TEAM_NOT_EXISTED);
        }

        securityUtils.checkTeamMember(teamId);

        return teamMemberRepository.findAllByTeamIdAndActive(teamId, true)
                .stream()
                .map(teamMemberMapper::toTeamMemberResponse)
                .toList();
    }

    public TeamMemberResponse updateTeamMember(String teamMemberId, TeamMemberUpdateRequest request){

        TeamMember teamMember = teamMemberRepository.findByIdAndActive(teamMemberId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAMMEMBER_NOT_EXISTED));

        securityUtils.checkAdminRole(teamMember.getTeam().getId());

        long admin_count = teamMemberRepository.findAllByTeamIdAndActive(teamMember.getTeam().getId(), true)
                .stream()
                .filter(member -> member.getRole().equals(Role.ADMIN)).count();

        if(admin_count == 1 && request.getRole() == Role.MEMBER){
            throw new AppException(ErrorCode.LAST_ADMIN_ROLE_CANT_BE_UPDATE);
        }

        teamMember.setRole(request.getRole());

        return teamMemberMapper.toTeamMemberResponse(teamMemberRepository.save(teamMember));
    }

    public void deleteTeamMember(String teamMemberId){
        TeamMember teamMember = teamMemberRepository.findByIdAndActive(teamMemberId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAMMEMBER_NOT_EXISTED));

        securityUtils.checkAdminRole(teamMember.getTeam().getId());

        List<TeamMember> teamMembers = teamMemberRepository
                .findAllByTeamIdAndActive(teamMember.getTeam().getId(), true);

        long admin_count = teamMembers.stream()
                .filter(member -> member.getRole().equals(Role.ADMIN))
                .count();

        if(admin_count == 1 && teamMembers.size() > 1){
            throw new AppException(ErrorCode.LAST_ADMIN_CANT_LEAVE);
        }

        teamMember.setActive(false);
        teamMemberRepository.save(teamMember);
    }

}
