package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.TeamMemberRequest;
import com.myapplication.taskmanagement.dto.response.TeamMemberResponse;
import com.myapplication.taskmanagement.entity.Team;
import com.myapplication.taskmanagement.entity.TeamMember;
import com.myapplication.taskmanagement.entity.User;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.TeamMemberMapper;
import com.myapplication.taskmanagement.repository.TeamMemberRepository;
import com.myapplication.taskmanagement.repository.TeamRepository;
import com.myapplication.taskmanagement.repository.UserRepository;
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


    public TeamMemberResponse createTeamMember(TeamMemberRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Team team = teamRepository.findByIdAndActive(request.getTeamId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_EXISTED));

        TeamMember teamMember = TeamMember.builder()
                .user(user)
                .team(team)
                .role(request.getRole())
                .build();

        teamMember = teamMemberRepository.save(teamMember);
        notificationService.notifyTeamAdded(user, team);

        return teamMemberMapper.toTeamMemberResponse(teamMember);
    }

    public List<TeamMemberResponse> getTeamMembersByTeamId(String teamId){

        if(!teamRepository.existsByIdAndActive(teamId, true)){
            throw new AppException(ErrorCode.TEAM_NOT_EXISTED);
        }

        return teamMemberRepository.findAllByTeamIdAndActive(teamId, true)
                .stream()
                .map(teamMemberMapper::toTeamMemberResponse)
                .toList();
    }

    public TeamMemberResponse updateTeamMember(String teamMemberId, TeamMemberRequest request){
        TeamMember teamMember = teamMemberRepository.findByIdAndActive(teamMemberId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAMMEMBER_NOT_EXISTED));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Team team = teamRepository.findByIdAndActive(request.getTeamId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_EXISTED));


        teamMember.setUser(user);
        teamMember.setTeam(team);
        teamMember.setRole(request.getRole());

        return teamMemberMapper.toTeamMemberResponse(teamMemberRepository.save(teamMember));
    }

    public void deleteTeamMember(String teamMemberId){
        TeamMember teamMember = teamMemberRepository.findByIdAndActive(teamMemberId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAMMEMBER_NOT_EXISTED));

        teamMember.setActive(false); // soft delete
        teamMemberRepository.save(teamMember);
    }

}
