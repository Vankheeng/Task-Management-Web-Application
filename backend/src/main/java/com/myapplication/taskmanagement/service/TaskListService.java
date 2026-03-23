package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.TaskListRequest;
import com.myapplication.taskmanagement.dto.response.TaskListDetailResponse;
import com.myapplication.taskmanagement.dto.response.TaskListResponse;
import com.myapplication.taskmanagement.entity.Project;
import com.myapplication.taskmanagement.entity.TaskList;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.TaskListMapper;
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
public class TaskListService {
    TaskListRepository taskListRepository;
    TaskListMapper taskListMapper;
    ProjectRepository projectRepository;
    TaskRepository taskRepository;
    SecurityUtils securityUtils;
    TaskAssignmentRepository taskAssignmentRepository;
    TaskAttachmentRepository taskAttachmentRepository;
    CommentRepository commentRepository;
    NotificationRepository notificationRepository;



    public TaskListResponse createTaskList(TaskListRequest request){
        Project project = projectRepository.findByIdAndActive(request.getProjectId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        securityUtils.checkTeamMember(project.getTeam().getId());

        TaskList taskList = taskListMapper.toTaskList(request);
        taskList.setProject(project);
        taskList.setCreatedAt(LocalDate.now());

        return taskListMapper.toTaskListResponse(taskListRepository.save(taskList));
    }

    public List<TaskListResponse> getTaskListsByProjectId(String projectId){
        Project project = projectRepository.findByIdAndActive(projectId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        securityUtils.checkTeamMember(project.getTeam().getId());

        return taskListRepository.findAllByProjectIdAndActive(projectId, true)
                .stream()
                .map(taskListMapper::toTaskListResponse)
                .toList();
    }

    public TaskListDetailResponse getTaskListById(String taskListId){
        TaskList taskList = taskListRepository.findByIdAndActive(taskListId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASKLIST_NOT_EXISTED));
        securityUtils.checkTeamMember(taskList.getProject().getTeam().getId());
        return taskListMapper.toTaskListDetailResponse(taskList);
    }

    public TaskListDetailResponse updateTaskList(String taskListId, TaskListRequest request){
        TaskList taskList = taskListRepository.findByIdAndActive(taskListId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASKLIST_NOT_EXISTED));

        securityUtils.checkTeamMember(taskList.getProject().getTeam().getId());

        taskListMapper.updateTaskList(taskList, request);

        return taskListMapper.toTaskListDetailResponse(taskListRepository.save(taskList));
    }

    @Transactional
    public void deleteTaskList(String taskListId){
        TaskList taskList = taskListRepository.findByIdAndActive(taskListId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TASKLIST_NOT_EXISTED));

        securityUtils.checkTeamMember(taskList.getProject().getTeam().getId());

        taskList.setActive(false);
        taskListRepository.save(taskList);
        taskRepository.deactivateAllByTaskListId(taskListId);
        taskAssignmentRepository.deactivateAllByTaskListId(taskListId);
        taskAttachmentRepository.deactivateAllByTaskListId(taskListId);
        commentRepository.deactivateAllByTaskListId(taskListId);
        notificationRepository.deactivateAllByTaskListId(taskListId);
    }
}