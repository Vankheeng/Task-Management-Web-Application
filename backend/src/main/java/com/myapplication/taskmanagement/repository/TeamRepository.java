package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    @EntityGraph(attributePaths = {"teamMembers", "teamMembers.user"})
    @Query("SELECT t FROM Team t WHERE t.id = :teamId and t.active = true")
    Optional<Team> findByIdAndActiveWithMembers(@Param("teamId") String teamId);

    boolean existsByIdAndActive(String id, boolean active);
    Optional<Team> findByIdAndActive(String id, boolean active);



}