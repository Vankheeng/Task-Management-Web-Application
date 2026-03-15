package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TeamRequest;
import com.myapplication.taskmanagement.dto.response.TeamResponse;
import com.myapplication.taskmanagement.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TeamMemberMapper.class})
public interface TeamMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "teamMembers", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Team toTeam(TeamRequest request);
    TeamResponse toTeamResponse(Team team);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "teamMembers", ignore = true)
    @Mapping(target = "projects", ignore = true)
    void updateTeam(@MappingTarget Team team, TeamRequest request);
}
