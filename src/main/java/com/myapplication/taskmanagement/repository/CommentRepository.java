package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    @Modifying
    @Query("UPDATE Comment c SET c.active = false WHERE c.task.taskList.id = :taskListId")
    void deactivateAllByTaskListId(@Param("taskListId") String taskListId);

    @Modifying
    @Query("UPDATE Comment c SET c.active = false WHERE c.task.taskList.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE Comment c SET c.active = false WHERE c.task.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);

    @Modifying
    @Query("UPDATE Comment t SET t.active = false WHERE t.task.id = :taskId")
    void deactivateAllByTaskId(@Param("taskId") String taskId);
}
