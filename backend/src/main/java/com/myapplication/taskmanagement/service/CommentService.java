package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.CommentRequest;
import com.myapplication.taskmanagement.dto.response.CommentResponse;
import com.myapplication.taskmanagement.entity.BaseEntity;
import com.myapplication.taskmanagement.entity.Comment;
import com.myapplication.taskmanagement.entity.Task;
import com.myapplication.taskmanagement.entity.User;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.CommentMapper;
import com.myapplication.taskmanagement.repository.CommentRepository;
import com.myapplication.taskmanagement.repository.TaskRepository;
import com.myapplication.taskmanagement.repository.UserRepository;
import com.myapplication.taskmanagement.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    TaskRepository taskRepository;
    UserRepository userRepository;
    SecurityUtils securityUtils;
    NotificationService notificationService;

    public CommentResponse createComment(CommentRequest request){
        Task task = taskRepository.findByIdAndActive(request.getTaskId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        securityUtils.checkTeamMember(task.getTaskList().getProject().getTeam().getId());

        String userId = SecurityUtils.getCurrentUserId();
        User createdBy = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Comment comment = commentMapper.toComment(request);
        comment.setTask(task);
        comment.setCreatedBy(createdBy);
        comment.setCreatedAt(LocalDate.now());
        comment = commentRepository.save(comment);

        task.getTaskAssignments().stream()
                .filter(BaseEntity::isActive)
                .filter(ta -> !ta.getUser().getId().equals(userId))
                .forEach(ta -> notificationService.notifyTaskComment(
                        ta.getUser(), task, createdBy.getFullName()));

        return commentMapper.toCommentResponse(comment);
    }

    public List<CommentResponse> getCommentsByTaskId(String taskId){
        Task task = taskRepository.findByIdAndActive(taskId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        securityUtils.checkTeamMember(task.getTaskList().getProject().getTeam().getId());

        return commentRepository.findAllByTask_IdAndActive(taskId, true)
                .stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }

    public CommentResponse updateComment(String commentId, CommentRequest request){
        Comment comment = commentRepository.findByIdAndActive(commentId, true)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));

        securityUtils.checkTeamMember(comment.getTask().getTaskList().getProject().getTeam().getId());

        String userId = SecurityUtils.getCurrentUserId();
        if(!comment.getCreatedBy().getId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        commentMapper.updateComment(comment, request);

        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    public String deleteComment(String commentId){
        Comment comment = commentRepository.findByIdAndActive(commentId, true)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));

        securityUtils.checkTeamMember(comment.getTask().getTaskList().getProject().getTeam().getId());

        String userId = SecurityUtils.getCurrentUserId();
        boolean isOwner = comment.getCreatedBy().getId().equals(userId);
        boolean isAdmin = securityUtils.isAdminOfTeam(
                comment.getTask().getTaskList().getProject().getTeam().getId(), userId);

        if(!isOwner && !isAdmin){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        comment.setActive(false);
        commentRepository.save(comment);

        return "Comment deleted successfully";
    }
}