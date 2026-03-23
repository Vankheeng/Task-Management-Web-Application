package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TaskRequest;
import com.myapplication.taskmanagement.dto.response.TaskDetailResponse;
import com.myapplication.taskmanagement.dto.response.TaskResponse;
import com.myapplication.taskmanagement.dto.response.TaskSummaryResponse;
import com.myapplication.taskmanagement.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {StatusMapper.class, CommentMapper.class,
                TaskAssignmentMapper.class, TaskAttachmentMapper.class})
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskList", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "taskAssignments", ignore = true)
    @Mapping(target = "taskAttachments", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Task toTask(TaskRequest request);

    TaskResponse toTaskResponse(Task task);

    TaskDetailResponse toTaskDetailResponse(Task task);

    TaskSummaryResponse toTaskSummaryResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskList", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "taskAssignments", ignore = true)
    @Mapping(target = "taskAttachments", ignore = true)
    @Mapping(target = "comments", ignore = true)
    void updateTask(@MappingTarget Task task, TaskRequest request);
}
