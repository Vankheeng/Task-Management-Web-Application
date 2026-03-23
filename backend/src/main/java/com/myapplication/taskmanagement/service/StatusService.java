package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.request.StatusRequest;
import com.myapplication.taskmanagement.dto.response.StatusResponse;
import com.myapplication.taskmanagement.entity.Project;
import com.myapplication.taskmanagement.entity.Status;
import com.myapplication.taskmanagement.entity.Task;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.StatusMapper;
import com.myapplication.taskmanagement.repository.ProjectRepository;
import com.myapplication.taskmanagement.repository.StatusRepository;
import com.myapplication.taskmanagement.repository.TaskRepository;
import com.myapplication.taskmanagement.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusService {
    StatusRepository statusRepository;
    StatusMapper statusMapper;
    ProjectRepository projectRepository;
    SecurityUtils securityUtils;
    TaskRepository taskRepository;

    public StatusResponse createStatus(StatusRequest request){
        Project project = projectRepository.findByIdAndActive(request.getProjectId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        securityUtils.checkAdminRole(project.getTeam().getId());

        Status status = statusMapper.toStatus(request);
        status.setProject(project);

        return statusMapper.toStatusResponse(statusRepository.save(status));
    }

    public List<StatusResponse> getStatusesByProjectId(String projectId){
        Project project = projectRepository.findByIdAndActive(projectId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));

        securityUtils.checkTeamMember(project.getTeam().getId());

        return statusRepository.findAllByProjectIdAndActive(projectId, true)
                .stream()
                .map(statusMapper::toStatusResponse)
                .toList();
    }

    public StatusResponse updateStatus(String statusId, StatusRequest request){
        Status status = statusRepository.findByIdAndActive(statusId, true)
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_EXISTED));

        securityUtils.checkAdminRole(status.getProject().getTeam().getId());

        statusMapper.updateStatus(status, request);

        return statusMapper.toStatusResponse(statusRepository.save(status));
    }

    public String deleteStatus(String statusId){
        Status status = statusRepository.findByIdAndActive(statusId, true)
                .orElseThrow(() -> new AppException(ErrorCode.STATUS_NOT_EXISTED));

        securityUtils.checkAdminRole(status.getProject().getTeam().getId());

        List<Task> task = taskRepository.findAllByStatus_IdAndActive(statusId, true);
        if(!task.isEmpty()){
            throw new AppException(ErrorCode.STATUS_CANT_DELETE);
        }

        status.setActive(false);
        statusRepository.save(status);

        return "Status deleted successfully";
    }
}