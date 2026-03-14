package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, String> {
}
