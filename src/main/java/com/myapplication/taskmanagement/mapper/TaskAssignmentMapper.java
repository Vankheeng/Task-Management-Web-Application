package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TaskAssignmentRequest;
import com.myapplication.taskmanagement.dto.response.TaskAssignmentResponse;
import com.myapplication.taskmanagement.entity.TaskAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class})
public interface TaskAssignmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "task", ignore = true)
    TaskAssignment toTaskAssignment(TaskAssignmentRequest request);

    TaskAssignmentResponse toTaskAssignmentResponse(TaskAssignment taskAssignment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "task", ignore = true)
    void updateTaskAssignment(@MappingTarget TaskAssignment taskAssignment,
                              TaskAssignmentRequest request);
}