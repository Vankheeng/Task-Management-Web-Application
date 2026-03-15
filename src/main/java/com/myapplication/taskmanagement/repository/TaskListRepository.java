package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskList, String> {
}
