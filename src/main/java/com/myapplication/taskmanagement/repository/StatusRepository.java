package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, String> {
}
