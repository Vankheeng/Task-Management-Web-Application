package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Notification;
import com.myapplication.taskmanagement.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    Page<Notification> findAllByUser_IdAndActive(String userId, boolean active, Pageable pageable);
    Page<Notification> findAllByUser_IdAndIsReadAndActive(String userId, Boolean isRead, boolean active, Pageable pageable);
    Optional<Notification> findByIdAndActive(String id, boolean active);

    List<Notification> findAllByUser_IdAndIsReadAndActive(
            String userId, Boolean isRead, boolean active);

    boolean existsByTask_IdAndUser_IdAndType(
            String taskId, String userId, NotificationType type);

    @Modifying
    @Query("UPDATE Notification n SET n.active = false WHERE n.task.taskList.id = :taskListId")
    void deactivateAllByTaskListId(@Param("taskListId") String taskListId);

    @Modifying
    @Query("UPDATE Notification n SET n.active = false WHERE n.task.taskList.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE Notification n SET n.active = false WHERE n.task.taskList.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);

    @Modifying
    @Query("UPDATE Notification t SET t.active = false WHERE t.task.id = :taskId")
    void deactivateAllByTaskId(@Param("taskId") String taskId);
}
