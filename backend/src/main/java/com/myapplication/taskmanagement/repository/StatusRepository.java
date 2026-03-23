package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, String> {
    List<Status> findAllByProjectIdAndActive(String projectId, boolean active);
    Optional<Status> findByIdAndActive(String id, boolean active);

    @Modifying
    @Query("UPDATE Status s SET s.active = false WHERE s.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE Status s SET s.active = false WHERE s.project.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);

    Optional<Status> findByIdAndProject_IdAndActive(String id, String projectId, boolean active);
    Optional<Status> findByStatusAndProject_IdAndActive(String status, String projectId, boolean active);
}