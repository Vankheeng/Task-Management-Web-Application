package com.myapplication.taskmanagement.mapper;


import com.myapplication.taskmanagement.dto.request.CommentRequest;
import com.myapplication.taskmanagement.dto.response.CommentResponse;
import com.myapplication.taskmanagement.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "task", ignore = true)
    Comment toComment(CommentRequest request);

    CommentResponse toCommentResponse(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "task", ignore = true)
    void updateComment(@MappingTarget Comment comment, CommentRequest request);
}
