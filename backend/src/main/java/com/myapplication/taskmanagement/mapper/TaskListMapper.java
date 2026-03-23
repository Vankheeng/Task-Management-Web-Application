package com.myapplication.taskmanagement.mapper;


import com.myapplication.taskmanagement.dto.request.TaskListRequest;
import com.myapplication.taskmanagement.dto.response.TaskListDetailResponse;
import com.myapplication.taskmanagement.dto.response.TaskListResponse;
import com.myapplication.taskmanagement.entity.TaskList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TaskMapper.class})
public interface TaskListMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    TaskList toTaskList(TaskListRequest request);
    TaskListResponse toTaskListResponse(TaskList taskList);
    TaskListDetailResponse toTaskListDetailResponse(TaskList taskList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    void updateTaskList(@MappingTarget TaskList taskList, TaskListRequest request);
}
