package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.TeamRequest;
import com.myapplication.taskmanagement.dto.response.TeamResponse;
import com.myapplication.taskmanagement.entity.Team;
import com.myapplication.taskmanagement.entity.TeamMember;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.TeamMapper;
import com.myapplication.taskmanagement.repository.TeamMemberRepository;
import com.myapplication.taskmanagement.repository.TeamRepository;
import com.myapplication.taskmanagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamService {
    TeamRepository teamRepository;
    TeamMapper teamMapper;
    UserRepository userRepository;
    TeamMemberRepository teamMemberRepository;

    public TeamResponse createTeam(TeamRequest request){
        Team team = teamMapper.toTeam(request);
        team.setCreatedAt(LocalDate.now());

        return teamMapper.toTeamResponse(teamRepository.save(team));
    }

//    public List<TeamResponse> getMyTeams(){
//        var context = SecurityContextHolder.getContext();
//        String userId = context.getAuthentication().getName();
//
//        if(!userRepository.existsById(userId)){
//            throw new AppException(ErrorCode.USER_NOT_EXISTED);
//        }
//
//        return teamMemberRepository.findAllByUser_Id(userId)
//                .stream()
//                .map(TeamMember::getTeam)
//                .map(teamMapper::toTeamResponse)
//                .toList();
//    }

    public List<TeamResponse> getMyTeams(){
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        List<TeamMember> teamMembers = teamMemberRepository.findAllByUserId(userId);

        try {
            return teamMembers.stream()
                    .map(TeamMember::getTeam)
                    .distinct()
                    .map(teamMapper::toTeamResponse)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public TeamResponse updateTeam(String teamId, TeamRequest request){
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_EXISTED));

        teamMapper.updateTeam(team, request);

        return teamMapper.toTeamResponse(teamRepository.save(team));
    }

    public void deleteTeam(String teamId){
        if(!teamRepository.existsById(teamId)){
            throw new AppException(ErrorCode.TEAM_NOT_EXISTED);
        }
        teamRepository.deleteById(teamId);
    }
}
