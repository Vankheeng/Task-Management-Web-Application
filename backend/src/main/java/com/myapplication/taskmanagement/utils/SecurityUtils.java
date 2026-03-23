package com.myapplication.taskmanagement.utils;


import com.myapplication.taskmanagement.entity.TeamMember;
import com.myapplication.taskmanagement.enums.Role;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    final TeamMemberRepository teamMemberRepository;

    public static String getCurrentUserId() {
        try{
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    public void checkTeamMember(String teamId){
        String userId = getCurrentUserId();
        teamMemberRepository
                .findByTeamIdAndUserIdAndActive(teamId, userId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAMMEMBER_NOT_EXISTED));
    }

    public void checkAdminRole(String teamId){
        String userId = getCurrentUserId();
        TeamMember teamMember = teamMemberRepository
                .findByTeamIdAndUserIdAndActive(teamId, userId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAMMEMBER_NOT_EXISTED));

        if(!teamMember.getRole().equals(Role.ADMIN)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    public boolean isAdminOfTeam(String teamId, String userId){
        return teamMemberRepository
                .findByTeamIdAndUserIdAndActive(teamId, userId, true)
                .map(tm -> tm.getRole().equals(Role.ADMIN))
                .orElse(false);
    }

    public void checkTeamMemberByUserId(String teamId, String userId){
        teamMemberRepository
                .findByTeamIdAndUserIdAndActive(teamId, userId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAMMEMBER_NOT_EXISTED));
    }

}