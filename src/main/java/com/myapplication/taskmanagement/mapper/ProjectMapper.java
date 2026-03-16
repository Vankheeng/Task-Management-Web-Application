package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.ProjectRequest;
import com.myapplication.taskmanagement.dto.response.ProjectResponse;
import com.myapplication.taskmanagement.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "taskLists", ignore = true)
    Project toProject(ProjectRequest request);
    ProjectResponse toProjectResponse(Project project);
}
