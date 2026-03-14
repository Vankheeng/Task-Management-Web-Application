package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TaskAttachmentRequest;
import com.myapplication.taskmanagement.dto.response.TaskAttachmentResponse;
import com.myapplication.taskmanagement.entity.TaskAttachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskAttachmentMapper {
    TaskAttachment toTaskAttachment(TaskAttachmentRequest request);
    TaskAttachmentResponse toTaskAttachmentResponse(TaskAttachment taskAttachment);
}
