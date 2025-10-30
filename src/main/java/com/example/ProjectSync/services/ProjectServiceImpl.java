package com.example.ProjectSync.services;

import com.example.ProjectSync.models.dtos.CreateProjectDTO;
import com.example.ProjectSync.models.dtos.ProjectResponseDTO;
import com.example.ProjectSync.models.dtos.UpdateProjectDTO;
import com.example.ProjectSync.models.entities.Project;
import com.example.ProjectSync.repositories.ProjectRepository;
import com.example.ProjectSync.util.exceptions.BadRequestException;
import com.example.ProjectSync.util.exceptions.DatabaseException;
import com.example.ProjectSync.util.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProjectServiceImpl - implements business logic for Project operations.
 * Handles validation, mapping, and database interactions.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    /**
     * Maps a Project entity to ProjectResponseDTO
     */
    private ProjectResponseDTO mapToResponseDTO(Project project) {
        return new ProjectResponseDTO(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStatus(),
            project.getTeam(),
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            return projects.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new DatabaseException("Failed to retrieve projects: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getProjectsByStatus(String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                throw new BadRequestException("Status parameter is required");
            }
            List<Project> projects = projectRepository.findByStatus(status);
            return projects.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to retrieve projects by status: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> searchProjects(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                throw new BadRequestException("Search query parameter is required");
            }
            List<Project> projects = projectRepository.searchProjects(query);
            return projects.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to search projects: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectById(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new BadRequestException("Project ID must be a positive number");
            }
            Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
            return mapToResponseDTO(project);
        } catch (ResourceNotFoundException | BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to retrieve project: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ProjectResponseDTO createProject(CreateProjectDTO createProjectDTO) {
        try {
            // Validate status
            validateStatus(createProjectDTO.getStatus());

            // Create new project entity
            Project project = new Project();
            project.setName(createProjectDTO.getName());
            project.setDescription(createProjectDTO.getDescription());
            project.setStatus(createProjectDTO.getStatus());
            project.setTeam(createProjectDTO.getTeam());

            // Save to database
            Project savedProject = projectRepository.save(project);
            return mapToResponseDTO(savedProject);
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to create project: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, UpdateProjectDTO updateProjectDTO) {
        try {
            if (id == null || id <= 0) {
                throw new BadRequestException("Project ID must be a positive number");
            }

            // Validate status
            validateStatus(updateProjectDTO.getStatus());

            // Find existing project
            Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

            // Update fields
            project.setName(updateProjectDTO.getName());
            project.setDescription(updateProjectDTO.getDescription());
            project.setStatus(updateProjectDTO.getStatus());
            project.setTeam(updateProjectDTO.getTeam());

            // Save updated project
            Project updatedProject = projectRepository.save(project);
            return mapToResponseDTO(updatedProject);
        } catch (ResourceNotFoundException | BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to update project: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteProject(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new BadRequestException("Project ID must be a positive number");
            }

            // Check if project exists
            if (!projectRepository.existsById(id)) {
                throw new ResourceNotFoundException("Project not found with ID: " + id);
            }

            // Delete project
            projectRepository.deleteById(id);
        } catch (ResourceNotFoundException | BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DatabaseException("Failed to delete project: " + ex.getMessage(), ex);
        }
    }

    /**
     * Validates that the status is one of the allowed values
     */
    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new BadRequestException("Status cannot be empty");
        }
        if (!isValidStatus(status)) {
            throw new BadRequestException("Invalid status. Allowed values: PENDING, IN_PROGRESS, COMPLETED");
        }
    }

    /**
     * Checks if the status is valid
     */
    private boolean isValidStatus(String status) {
        return status.equals("PENDING") || status.equals("IN_PROGRESS") || status.equals("COMPLETED");
    }
}
