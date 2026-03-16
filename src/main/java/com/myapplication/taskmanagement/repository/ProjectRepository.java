package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    @Modifying
    @Query("UPDATE Project p SET p.active = false WHERE p.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);

    List<Project> findAllByTeamIdAndActive(String teamId, boolean active);
}
