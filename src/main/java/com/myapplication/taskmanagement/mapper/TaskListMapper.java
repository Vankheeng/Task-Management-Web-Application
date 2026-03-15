package com.myapplication.taskmanagement.mapper;


import com.myapplication.taskmanagement.dto.request.TaskListRequest;
import com.myapplication.taskmanagement.dto.response.TaskListResponse;
import com.myapplication.taskmanagement.entity.TaskList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskListMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    TaskList toList(TaskListRequest request);
    TaskListResponse toListResponse(TaskList taskList);
}
