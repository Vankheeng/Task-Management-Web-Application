package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
