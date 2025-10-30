package com.example.ProjectSync.repositories;

import com.example.ProjectSync.models.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProjectRepository - Data Access Object for Project entity.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Find all projects by status.
     * @param status the project status (PENDING, IN_PROGRESS, COMPLETED)
     * @return list of projects with the specified status
     */
    List<Project> findByStatus(String status);

    /**
     * Find projects by name or description containing a search query.
     * @param query the search term
     * @return list of projects matching the search query
     */
    @Query("SELECT p FROM Project p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Project> searchProjects(@Param("query") String query);

    /**
     * Find projects by team name.
     * @param team the team name
     * @return list of projects assigned to the team
     */
    List<Project> findByTeam(String team);
}
