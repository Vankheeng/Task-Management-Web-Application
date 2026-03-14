package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAttachmentReposotory extends JpaRepository<TaskAttachment, String> {
}
