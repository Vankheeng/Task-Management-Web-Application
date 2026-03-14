package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedRepository extends JpaRepository<InvalidatedToken, String> {
}
