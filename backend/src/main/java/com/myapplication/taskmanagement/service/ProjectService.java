package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.ProjectRequest;
import com.myapplication.taskmanagement.dto.response.ProjectResponse;
import com.myapplication.taskmanagement.entity.Project;
import com.myapplication.taskmanagement.entity.Status;
import com.myapplication.taskmanagement.entity.Team;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.ProjectMapper;
import com.myapplication.taskmanagement.repository.*;
import com.myapplication.taskmanagement.utils.SecurityUtils;
import jakarta.persistence.EntityManager;
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
public class ProjectService {
    ProjectRepository projectRepository;
    ProjectMapper projectMapper;
    TeamRepository teamRepository;
    TaskListRepository taskListRepository;
    TaskRepository taskRepository;
    SecurityUtils securityUtils;
    TaskAssignmentRepository taskAssignmentRepository;
    TaskAttachmentRepository taskAttachmentRepository;
    CommentRepository commentRepository;
    NotificationRepository notificationRepository;
    StatusRepository statusRepository;
    EntityManager entityManager;

    @Transactional
    public ProjectResponse createProject(ProjectRequest request){
        Team team = teamRepository.findByIdAndActive(request.getTeamId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_EXISTED));

        securityUtils.checkAdminRole(team.getId());

        Project project = projectMapper.toProject(request);
        project.setTeam(team);
        project.setCreatedAt(LocalDate.now());
        project = projectRepository.save(project);

        Status todo = Status.builder()
                .status("To Do")
                .project(project)
                .build();

        Status complete = Status.builder()
                .status("Complete")
                .project(project)
                .build();

        statusRepository.saveAll(List.of(todo, complete));

        entityManager.flush();
        entityManager.clear();

        project = projectRepository.findByIdAndActive(project.getId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));


        return projectMapper.toProjectResponse(project);
    }

    public List<ProjectResponse> getProjectsByTeamId(String teamId){
        teamRepository.findByIdAndActive(teamId, true)
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_EXISTED));

        securityUtils.checkTeamMember(teamId);

        return projectRepository.findAllByTeamIdAndActive(teamId, true)
                .stream()
                .map(projectMapper::toProjectResponse)
                .toList();
    }

    public ProjectResponse getProjectById(String projectId){
        Project project = projectRepository.findByIdAndActive(projectId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));
        securityUtils.checkTeamMember(project.getTeam().getId());
        return projectMapper.toProjectResponse(project);
    }

    public ProjectResponse updateProject(String projectId, ProjectRequest request){
        Project project = projectRepository.findByIdAndActive(projectId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        securityUtils.checkAdminRole(project.getTeam().getId());

        projectMapper.updateProject(project, request);

        return projectMapper.toProjectResponse(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(String projectId){
        Project project = projectRepository.findByIdAndActive(projectId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        securityUtils.checkAdminRole(project.getTeam().getId());

        project.setActive(false);
        projectRepository.save(project);
        taskListRepository.deactivateAllByProjectId(projectId);
        taskRepository.deactivateAllByProjectId(projectId);
        taskAssignmentRepository.deactivateAllByProjectId(projectId);
        taskAttachmentRepository.deactivateAllByProjectId(projectId);
        commentRepository.deactivateAllByProjectId(projectId);
        notificationRepository.deactivateAllByProjectId(projectId);
        statusRepository.deactivateAllByProjectId(projectId);
    }
}
