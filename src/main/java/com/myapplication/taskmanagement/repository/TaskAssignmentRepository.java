package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, String> {
    List<TaskAssignment> findAllByTask_IdAndActive(String taskId, boolean active);
    Optional<TaskAssignment> findByIdAndActive(String id, boolean active);

    List<TaskAssignment> findAllByUser_IdAndActive(String userId, boolean active);
    List<TaskAssignment> findAllByUser_IdAndTask_TaskList_Project_Team_IdAndActive(
            String userId, String teamId, boolean active);

    boolean existsByTask_IdAndUser_IdAndActive(String taskId, String userId, boolean active);

    @Modifying
    @Query("UPDATE TaskAssignment ta SET ta.active = false WHERE ta.task.id = :taskId")
    void deactivateAllByTaskId(@Param("taskId") String taskId);

    @Modifying
    @Query("UPDATE TaskAssignment ta SET ta.active = false WHERE ta.task.taskList.id = :taskListId")
    void deactivateAllByTaskListId(@Param("taskListId") String taskListId);

    @Modifying
    @Query("UPDATE TaskAssignment ta SET ta.active = false WHERE ta.task.taskList.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE TaskAssignment ta SET ta.active = false WHERE ta.task.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);
}