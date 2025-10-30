package com.example.ProjectSync.services;

import com.example.ProjectSync.models.dtos.CreateProjectDTO;
import com.example.ProjectSync.models.dtos.ProjectResponseDTO;
import com.example.ProjectSync.models.dtos.UpdateProjectDTO;

import java.util.List;

/**
 * ProjectService interface - defines business logic operations for projects.
 * Decouples the controller from the implementation.
 */
public interface ProjectService {

    /**
     * Retrieve all projects.
     * @return list of all projects
     */
    List<ProjectResponseDTO> getAllProjects();

    /**
     * Retrieve all projects filtered by status.
     * @param status the project status
     * @return list of projects with the specified status
     */
    List<ProjectResponseDTO> getProjectsByStatus(String status);

    /**
     * Search projects by name or description.
     * @param query the search term
     * @return list of projects matching the query
     */
    List<ProjectResponseDTO> searchProjects(String query);

    /**
     * Retrieve a single project by ID.
     * @param id the project ID
     * @return the project details
     * @throws ResourceNotFoundException if project not found
     */
    ProjectResponseDTO getProjectById(Long id);

    /**
     * Create a new project.
     * @param createProjectDTO the project data
     * @return the created project
     * @throws BadRequestException if validation fails
     */
    ProjectResponseDTO createProject(CreateProjectDTO createProjectDTO);

    /**
     * Update an existing project.
     * @param id the project ID
     * @param updateProjectDTO the updated project data
     * @return the updated project
     * @throws ResourceNotFoundException if project not found
     * @throws BadRequestException if validation fails
     */
    ProjectResponseDTO updateProject(Long id, UpdateProjectDTO updateProjectDTO);

    /**
     * Delete a project.
     * @param id the project ID
     * @throws ResourceNotFoundException if project not found
     */
    void deleteProject(Long id);
}
