package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.mapper.ProjectMapper;
import com.myapplication.taskmanagement.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectService {
    ProjectRepository projectRepository;
    ProjectMapper projectMapper;


}
