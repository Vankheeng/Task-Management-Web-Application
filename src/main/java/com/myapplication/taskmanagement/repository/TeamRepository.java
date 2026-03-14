package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String> {
}
