package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.StatusRequest;
import com.myapplication.taskmanagement.dto.response.StatusResponse;
import com.myapplication.taskmanagement.entity.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatusMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    Status toStatus(StatusRequest request);

    StatusResponse toStatusResponse(Status status);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    void updateStatus(@MappingTarget Status status, StatusRequest request);
}