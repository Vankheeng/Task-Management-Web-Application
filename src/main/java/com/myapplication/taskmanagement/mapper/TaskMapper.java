package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TaskRequest;
import com.myapplication.taskmanagement.dto.response.TaskResponse;
import com.myapplication.taskmanagement.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toTask(TaskRequest request);
    TaskResponse toTaskResponse(Task task);
}
