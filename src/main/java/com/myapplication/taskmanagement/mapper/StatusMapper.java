package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.StatusRequest;
import com.myapplication.taskmanagement.dto.response.StatusResponse;
import com.myapplication.taskmanagement.entity.Status;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    Status toStatus(StatusRequest request);
    StatusResponse toStatusResponse(Status status);
}
