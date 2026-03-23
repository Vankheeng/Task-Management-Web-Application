package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllByTask_IdAndActive(String taskId, boolean active);
    Optional<Comment> findByIdAndActive(String id, boolean active);

    @Modifying
    @Query("UPDATE Comment c SET c.active = false WHERE c.task.id = :taskId")
    void deactivateAllByTaskId(@Param("taskId") String taskId);

    @Modifying
    @Query("UPDATE Comment c SET c.active = false WHERE c.task.taskList.id = :taskListId")
    void deactivateAllByTaskListId(@Param("taskListId") String taskListId);

    @Modifying
    @Query("UPDATE Comment c SET c.active = false WHERE c.task.taskList.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE Comment c SET c.active = false WHERE c.task.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);
}