package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.request.CommentRequest;
import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.CommentResponse;
import com.myapplication.taskmanagement.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    @PostMapping
    APIResponse<CommentResponse> createComment(@RequestBody CommentRequest request){
        return APIResponse.<CommentResponse>builder()
                .result(commentService.createComment(request))
                .build();
    }

    @GetMapping("/task/{taskId}")
    APIResponse<List<CommentResponse>> getCommentsByTaskId(@PathVariable String taskId){
        return APIResponse.<List<CommentResponse>>builder()
                .result(commentService.getCommentsByTaskId(taskId))
                .build();
    }

    @PutMapping("/{commentId}")
    APIResponse<CommentResponse> updateComment(@PathVariable String commentId,
                                               @RequestBody CommentRequest request){
        return APIResponse.<CommentResponse>builder()
                .result(commentService.updateComment(commentId, request))
                .build();
    }

    @DeleteMapping("/{commentId}")
    APIResponse<String> deleteComment(@PathVariable String commentId){
        return APIResponse.<String>builder()
                .result(commentService.deleteComment(commentId))
                .build();
    }
}