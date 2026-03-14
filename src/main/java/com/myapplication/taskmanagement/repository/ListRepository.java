package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<List, String> {
}
