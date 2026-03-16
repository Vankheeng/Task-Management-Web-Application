package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, String> {
    @Query("UPDATE TaskAssignment ta SET ta.active = false WHERE ta.task.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);
}
