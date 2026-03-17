package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.ProjectRequest;
import com.myapplication.taskmanagement.dto.response.ProjectResponse;
import com.myapplication.taskmanagement.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {StatusMapper.class, TaskListMapper.class})
public interface ProjectMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "taskLists", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    Project toProject(ProjectRequest request);
    ProjectResponse toProjectResponse(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "taskLists", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    void updateProject(@MappingTarget Project project, ProjectRequest request);

}
