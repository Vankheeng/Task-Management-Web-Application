package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TaskAssignmentRequest;
import com.myapplication.taskmanagement.dto.response.TaskAssignmentResponse;
import com.myapplication.taskmanagement.entity.TaskAssignment;
import org.mapstruct.Mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskAssignmentMapper {
    TaskAssignment toTaskAssignment(TaskAssignmentRequest request);
    TaskAssignmentResponse toTaskAssignmentResponse(TaskAssignment taskAssignment);
}
