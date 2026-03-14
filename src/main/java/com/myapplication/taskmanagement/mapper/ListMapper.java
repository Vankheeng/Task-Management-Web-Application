package com.myapplication.taskmanagement.mapper;


import com.myapplication.taskmanagement.dto.request.ListRequest;
import com.myapplication.taskmanagement.dto.response.ListResponse;
import com.myapplication.taskmanagement.entity.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListMapper {
    List toList(ListRequest request);
    ListResponse toListResponse(List list);
}
