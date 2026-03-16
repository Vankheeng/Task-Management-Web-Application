package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    @Modifying
    @Query("UPDATE Notification n SET n.active = false WHERE n.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);
}
