package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAttachmentReposotory extends JpaRepository<TaskAttachment, String> {
    @Modifying
    @Query("UPDATE TaskAttachment ta SET ta.active = false WHERE ta.task.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);
}
