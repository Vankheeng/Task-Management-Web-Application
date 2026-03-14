package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TeamMemberRequest;
import com.myapplication.taskmanagement.dto.response.TeamMemberResponse;
import com.myapplication.taskmanagement.entity.TeamMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMemberMapper {
    TeamMember toTeamMember(TeamMemberRequest request);
    TeamMemberResponse toTeamMemberResponse(TeamMember teamMember);
}
