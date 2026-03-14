package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
}
