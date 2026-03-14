package com.myapplication.taskmanagement.mapper;


import com.myapplication.taskmanagement.dto.request.CommentRequest;
import com.myapplication.taskmanagement.dto.response.CommentResponse;
import com.myapplication.taskmanagement.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentRequest request);
    CommentResponse toCommentResponse(Comment comment);
}
