package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.ProjectRequest;
import com.myapplication.taskmanagement.dto.response.ProjectResponse;
import com.myapplication.taskmanagement.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toTeam(ProjectRequest request);
    ProjectResponse toTeamResponse(Project project);
}
