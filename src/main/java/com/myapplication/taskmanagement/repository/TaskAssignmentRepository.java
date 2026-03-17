package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, String> {
    @Modifying
    @Query("UPDATE TaskAssignment ta SET ta.active = false WHERE ta.task.taskList.id = :taskListId")
    void deactivateAllByTaskListId(@Param("taskListId") String taskListId);

    @Modifying
    @Query("UPDATE TaskAssignment ta SET ta.active = false WHERE ta.task.taskList.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE TaskAssignment t SET t.active = false WHERE t.task.id = :taskId")
    void deactivateAllByTaskId(@Param("taskId") String taskId);

    @Modifying
    @Query("UPDATE TaskAssignment ta SET ta.active = false WHERE ta.task.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);
}
