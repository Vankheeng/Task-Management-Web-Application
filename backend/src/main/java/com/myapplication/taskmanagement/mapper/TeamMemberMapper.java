package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.response.TeamMemberResponse;
import com.myapplication.taskmanagement.entity.TeamMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TeamMemberMapper {
    TeamMemberResponse toTeamMemberResponse(TeamMember teamMember);
}