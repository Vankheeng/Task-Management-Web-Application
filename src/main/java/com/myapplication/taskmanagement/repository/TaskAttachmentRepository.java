package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, String> {
    @Modifying
    @Query("UPDATE TaskAttachment ta SET ta.active = false WHERE ta.task.taskList.id = :taskListId")
    void deactivateAllByTaskListId(@Param("taskListId") String taskListId);

    @Modifying
    @Query("UPDATE TaskAttachment ta SET ta.active = false WHERE ta.task.taskList.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE TaskAttachment ta SET ta.active = false WHERE ta.task.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);

    @Modifying
    @Query("UPDATE TaskAttachment t SET t.active = false WHERE t.task.id = :taskId")
    void deactivateAllByTaskId(@Param("taskId") String taskId);
}
