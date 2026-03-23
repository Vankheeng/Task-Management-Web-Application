package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.TaskAttachmentRequest;
import com.myapplication.taskmanagement.dto.response.TaskAttachmentResponse;
import com.myapplication.taskmanagement.entity.TaskAttachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class})
public interface TaskAttachmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "task", ignore = true)
    TaskAttachment toTaskAttachment(TaskAttachmentRequest request);

    TaskAttachmentResponse toTaskAttachmentResponse(TaskAttachment taskAttachment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "task", ignore = true)
    void updateTaskAttachment(@MappingTarget TaskAttachment taskAttachment,
                              TaskAttachmentRequest request);
}