package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, String> {
    @Modifying
    @Query("UPDATE TaskList tl SET tl.active = false WHERE tl.project.id = :projectId")
    void deactivateAllByProjectId(@Param("projectId") String projectId);

    @Modifying
    @Query("UPDATE TaskList tl SET tl.active = false WHERE tl.project.team.id = :teamId")
    void deactivateAllByProjectTeamId(@Param("teamId") String teamId);

    List<TaskList> findAllByProjectIdAndActive(String projectId, boolean active);
    Optional<TaskList> findByIdAndActive(String id, boolean active);

}
