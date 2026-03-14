package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> {
}
