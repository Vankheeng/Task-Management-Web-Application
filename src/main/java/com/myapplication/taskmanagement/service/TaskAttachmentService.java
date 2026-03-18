package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.TaskAttachmentRequest;
import com.myapplication.taskmanagement.dto.response.TaskAttachmentResponse;
import com.myapplication.taskmanagement.entity.Task;
import com.myapplication.taskmanagement.entity.TaskAttachment;
import com.myapplication.taskmanagement.entity.User;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.TaskAttachmentMapper;
import com.myapplication.taskmanagement.repository.TaskAttachmentRepository;
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
public class TaskAttachmentService {
    TaskAttachmentRepository taskAttachmentRepository;
    TaskAttachmentMapper taskAttachmentMapper;
    TaskRepository taskRepository;
    UserRepository userRepository;
    SecurityUtils securityUtils;

    public TaskAttachmentResponse createTaskAttachment(TaskAttachmentRequest request){
        Task task = taskRepository.findByIdAndActive(request.getTaskId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        securityUtils.checkTeamMember(task.getTaskList().getProject().getTeam().getId());

        String userId = SecurityUtils.getCurrentUserId();
        User createdBy = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        TaskAttachment taskAttachment = TaskAttachment.builder()
                .task(task)
                .createdBy(createdBy)
                .fileName(request.getFileName())
                .fileUrl(request.getFileUrl())
                .createdAt(LocalDate.now())
                .build();

        return taskAttachmentMapper.toTaskAttachmentResponse(
                taskAttachmentRepository.save(taskAttachment));
    }

    public List<TaskAttachmentResponse> getTaskAttachmentsByTaskId(String taskId){
        Task task = taskRepository.findByIdAndActive(taskId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        securityUtils.checkTeamMember(task.getTaskList().getProject().getTeam().getId());

        return taskAttachmentRepository.findAllByTask_IdAndActive(taskId, true)
                .stream()
                .map(taskAttachmentMapper::toTaskAttachmentResponse)
                .toList();
    }

    public TaskAttachmentResponse updateTaskAttachment(String taskAttachmentId,
                                                       TaskAttachmentRequest request){
        TaskAttachment taskAttachment = taskAttachmentRepository
                .findByIdAndActive(taskAttachmentId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASKATTACHMENT_NOT_EXISTED));

        securityUtils.checkTeamMember(
                taskAttachment.getTask().getTaskList().getProject().getTeam().getId());

        String userId = SecurityUtils.getCurrentUserId();
        if(!taskAttachment.getCreatedBy().getId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        taskAttachment.setFileName(request.getFileName());
        taskAttachment.setFileUrl(request.getFileUrl());

        return taskAttachmentMapper.toTaskAttachmentResponse(
                taskAttachmentRepository.save(taskAttachment));
    }

    public String deleteTaskAttachment(String taskAttachmentId){
        TaskAttachment taskAttachment = taskAttachmentRepository
                .findByIdAndActive(taskAttachmentId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASKATTACHMENT_NOT_EXISTED));

        securityUtils.checkTeamMember(
                taskAttachment.getTask().getTaskList().getProject().getTeam().getId());

        String userId = SecurityUtils.getCurrentUserId();
        boolean isOwner = taskAttachment.getCreatedBy().getId().equals(userId);
        boolean isAdmin = securityUtils.isAdminOfTeam(
                taskAttachment.getTask().getTaskList().getProject().getTeam().getId(), userId);

        if(!isOwner && !isAdmin){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        taskAttachment.setActive(false);
        taskAttachmentRepository.save(taskAttachment);

        return "Task attachment deleted successfully";
    }
}