package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, String> {
}
