package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.TaskRequest;
import com.myapplication.taskmanagement.dto.response.TaskDetailResponse;
import com.myapplication.taskmanagement.dto.response.TaskResponse;
import com.myapplication.taskmanagement.entity.*;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.TaskMapper;
import com.myapplication.taskmanagement.repository.*;
import com.myapplication.taskmanagement.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {
    TaskRepository taskRepository;
    TaskMapper taskMapper;
    TaskListRepository taskListRepository;
    StatusRepository statusRepository;
    TaskAssignmentRepository taskAssignmentRepository;
    TaskAttachmentRepository taskAttachmentRepository;
    CommentRepository commentRepository;
    NotificationRepository notificationRepository;
    SecurityUtils securityUtils;
    NotificationService notificationService;
    UserRepository userRepository;

    public TaskDetailResponse createTask(TaskRequest request){
        TaskList taskList = taskListRepository.findByIdAndActive(request.getTaskListId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.TASKLIST_NOT_EXISTED));

        securityUtils.checkTeamMember(taskList.getProject().getTeam().getId());

        if(request.getDeadline() != null && request.getDeadline().isBefore(LocalDate.now())){
            throw new AppException(ErrorCode.INVALID_DEADLINE);
        }

        Task task = taskMapper.toTask(request);
        task.setTaskList(taskList);
        task.setCreatedAt(LocalDate.now());

        Status status;
        if(request.getStatusId() != null && !request.getStatusId().isEmpty()){
            status = statusRepository.findByIdAndProject_IdAndActive(
                            request.getStatusId(), taskList.getProject().getId(), true)
                    .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_EXISTED));
        } else {
            status = statusRepository
                    .findByStatusAndProject_IdAndActive("To Do", taskList.getProject().getId(), true)
                    .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_EXISTED));
        }

        task.setStatus(status);

        return taskMapper.toTaskDetailResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getTasksByTaskListId(String taskListId){
        TaskList taskList = taskListRepository.findByIdAndActive(taskListId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASKLIST_NOT_EXISTED));

        securityUtils.checkTeamMember(taskList.getProject().getTeam().getId());

        return taskRepository.findAllByTaskListIdAndActive(taskListId, true)
                .stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    public TaskDetailResponse getTaskById(String taskId){
        Task task = taskRepository.findByIdAndActive(taskId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        securityUtils.checkTeamMember(task.getTaskList().getProject().getTeam().getId());

        return taskMapper.toTaskDetailResponse(task);
    }

    public TaskDetailResponse updateTask(String taskId, TaskRequest request){
        Task task = taskRepository.findByIdAndActive(taskId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        securityUtils.checkTeamMember(task.getTaskList().getProject().getTeam().getId());

        if(request.getDeadline() != null && request.getDeadline().isBefore(LocalDate.now())){
            throw new AppException(ErrorCode.INVALID_DEADLINE);
        }

        Status oldStatus = task.getStatus() != null ? task.getStatus() : null;
        boolean statusChanged = false;
        Status newStatus = null;

        if(request.getStatusId() != null && !request.getStatusId().isEmpty()){
            newStatus = statusRepository.findByIdAndProject_IdAndActive(
                            request.getStatusId(), task.getTaskList().getProject().getId(), true)
                    .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_EXISTED));
            if(oldStatus == null || !oldStatus.equals(newStatus)){
                statusChanged = true;
                task.setStatus(newStatus);
            }
        }

        taskMapper.updateTask(task, request);
        Task saved = taskRepository.save(task);
        List<TaskAssignment> assignments = taskAssignmentRepository
                .findAllByTask_IdAndActive(taskId, true);

        User createdBy = userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        final Status finalNewStatus = newStatus;
        if(statusChanged && newStatus != null){
            assignments.stream()
                    .filter(ta -> !ta.getUser().getId().equals(createdBy.getId()))
                    .forEach(ta -> notificationService.notifyTaskStatusUpdated(
                            ta.getUser(), saved, finalNewStatus.getStatus()));
        }

        return taskMapper.toTaskDetailResponse(saved);
    }

    @Transactional
    public String deleteTask(String taskId){
        Task task = taskRepository.findByIdAndActive(taskId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        securityUtils.checkTeamMember(task.getTaskList().getProject().getTeam().getId());

        task.setActive(false);
        taskRepository.save(task);
        taskAssignmentRepository.deactivateAllByTaskId(taskId);
        taskAttachmentRepository.deactivateAllByTaskId(taskId);
        commentRepository.deactivateAllByTaskId(taskId);
        notificationRepository.deactivateAllByTaskId(taskId);

        return "Task deleted successfully";
    }
}