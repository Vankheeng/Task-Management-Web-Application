package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    @Modifying
    @Query("UPDATE Task t SET t.active = false WHERE t.taskList.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE Task t SET t.active = false WHERE t.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);

    @Modifying
    @Query("UPDATE Task t SET t.active = false WHERE t.taskList.id = :taskListId")
    void deactivateAllByTaskListId(@Param("taskListId") String taskListId);

    List<Task> findAllByTaskListIdAndActive(String taskListId, boolean active);
    Optional<Task> findByIdAndActive(String id, boolean active);
    List<Task> findAllByDeadlineBeforeAndActive(LocalDate date, boolean active);
    List<Task> findAllByStatus_IdAndActive(String id, boolean active);
}
