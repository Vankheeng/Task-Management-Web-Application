package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.TaskAssignmentRequest;
import com.myapplication.taskmanagement.dto.response.TaskAssignmentResponse;
import com.myapplication.taskmanagement.entity.Task;
import com.myapplication.taskmanagement.entity.TaskAssignment;
import com.myapplication.taskmanagement.entity.User;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.TaskAssignmentMapper;
import com.myapplication.taskmanagement.repository.TaskAssignmentRepository;
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
public class TaskAssignmentService {
    TaskAssignmentRepository taskAssignmentRepository;
    TaskAssignmentMapper taskAssignmentMapper;
    TaskRepository taskRepository;
    UserRepository userRepository;
    SecurityUtils securityUtils;
    NotificationService notificationService;

    public TaskAssignmentResponse createTaskAssignment(TaskAssignmentRequest request){
        Task task = taskRepository.findByIdAndActive(request.getTaskId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        securityUtils.checkTeamMember(task.getTaskList().getProject().getTeam().getId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        securityUtils.checkTeamMemberByUserId(
                task.getTaskList().getProject().getTeam().getId(),
                request.getUserId());

        if(taskAssignmentRepository.existsByTask_IdAndUser_IdAndActive(
                request.getTaskId(), request.getUserId(), true)){
            throw new AppException(ErrorCode.USER_ALREADY_ASSIGNED);
        }

        TaskAssignment taskAssignment = TaskAssignment.builder()
                .task(task)
                .user(user)
                .createdAt(LocalDate.now())
                .build();

        taskAssignment = taskAssignmentRepository.save(taskAssignment);
        notificationService.notifyTaskAssigned(user, task);

        return taskAssignmentMapper.toTaskAssignmentResponse(taskAssignment);
    }

    public List<TaskAssignmentResponse> getTaskAssignmentsByTaskId(String taskId){
        Task task = taskRepository.findByIdAndActive(taskId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        securityUtils.checkTeamMember(task.getTaskList().getProject().getTeam().getId());

        return taskAssignmentRepository.findAllByTask_IdAndActive(taskId, true)
                .stream()
                .map(taskAssignmentMapper::toTaskAssignmentResponse)
                .toList();
    }

    public List<TaskAssignmentResponse> getMyTaskAssignments(){
        String userId = SecurityUtils.getCurrentUserId();
        return taskAssignmentRepository.findAllByUser_IdAndActive(userId, true)
                .stream()
                .map(taskAssignmentMapper::toTaskAssignmentResponse)
                .toList();
    }

    public List<TaskAssignmentResponse> getMyTaskAssignmentsByTeam(String teamId){
        String userId = SecurityUtils.getCurrentUserId();
        securityUtils.checkTeamMember(teamId);
        return taskAssignmentRepository
                .findAllByUser_IdAndTask_TaskList_Project_Team_IdAndActive(userId, teamId, true)
                .stream()
                .map(taskAssignmentMapper::toTaskAssignmentResponse)
                .toList();
    }

    public TaskAssignmentResponse updateTaskAssignment(String taskAssignmentId,
                                                       TaskAssignmentRequest request){
        TaskAssignment taskAssignment = taskAssignmentRepository
                .findByIdAndActive(taskAssignmentId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASKASSIGNMENT_NOT_EXISTED));

        securityUtils.checkTeamMember(
                taskAssignment.getTask().getTaskList().getProject().getTeam().getId());

        User newUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        User oldUser = userRepository.findById(taskAssignment.getUser().getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        securityUtils.checkTeamMemberByUserId(
                taskAssignment.getTask().getTaskList().getProject().getTeam().getId(),
                request.getUserId());

        if(taskAssignmentRepository.existsByTask_IdAndUser_IdAndActive(
                taskAssignment.getTask().getId(), request.getUserId(), true)){
            throw new AppException(ErrorCode.USER_ALREADY_ASSIGNED);
        }

        taskAssignment.setUser(newUser);
        taskAssignment = taskAssignmentRepository.save(taskAssignment);

        notificationService.notifyTaskUnassigned(oldUser, taskAssignment.getTask());
        notificationService.notifyTaskAssigned(newUser, taskAssignment.getTask());

        return taskAssignmentMapper.toTaskAssignmentResponse(taskAssignment);
    }

    public String deleteTaskAssignment(String taskAssignmentId){
        TaskAssignment taskAssignment = taskAssignmentRepository
                .findByIdAndActive(taskAssignmentId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASKASSIGNMENT_NOT_EXISTED));

        securityUtils.checkTeamMember(
                taskAssignment.getTask().getTaskList().getProject().getTeam().getId());

        taskAssignment.setActive(false);
        taskAssignmentRepository.save(taskAssignment);

        return "Task assignment deleted successfully";
    }
}