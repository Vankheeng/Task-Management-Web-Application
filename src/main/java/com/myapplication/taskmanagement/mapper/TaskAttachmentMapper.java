package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TaskAttachmentRequest;
import com.myapplication.taskmanagement.dto.response.TaskAttachmentResponse;
import com.myapplication.taskmanagement.entity.TaskAttachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskAttachmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "task", ignore = true)
    TaskAttachment toTaskAttachment(TaskAttachmentRequest request);
    TaskAttachmentResponse toTaskAttachmentResponse(TaskAttachment taskAttachment);
}
