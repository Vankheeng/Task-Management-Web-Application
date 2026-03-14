package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TeamRequest;
import com.myapplication.taskmanagement.dto.response.TeamResponse;
import com.myapplication.taskmanagement.entity.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team toTeam(TeamRequest request);
    TeamResponse toTeamResponse(Team team);
}
