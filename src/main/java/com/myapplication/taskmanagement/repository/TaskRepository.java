package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    // TaskRepository
    @Modifying
    @Query("UPDATE Task t SET t.active = false WHERE t.taskList.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE Task t SET t.active = false WHERE t.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);

    List<Task> findAllByTaskListIdAndActive(String taskListId, boolean active);
}
