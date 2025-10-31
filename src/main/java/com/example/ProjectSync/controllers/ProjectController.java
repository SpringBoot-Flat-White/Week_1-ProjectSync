package com.example.ProjectSync.controllers;

import com.example.ProjectSync.models.dtos.CreateProjectDTO;
import com.example.ProjectSync.models.dtos.ProjectResponseDTO;
import com.example.ProjectSync.models.dtos.UpdateProjectDTO;
import com.example.ProjectSync.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProjectController - REST API endpoints for Project CRUD operations.
 * Handles HTTP requests and delegates to ProjectService for business logic.
 *
 * API Endpoints:
 * - GET    /api/projects              - List all projects
 * - GET    /api/projects?status=X     - Filter by status
 * - GET    /api/projects?q=search     - Search projects
 * - GET    /api/projects/{id}         - Get project by ID
 * - POST   /api/projects              - Create new project
 * - PUT    /api/projects/{id}         - Update project
 * - DELETE /api/projects/{id}         - Delete project
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * GET /api/projects - List all projects or filter by status/search
     *
     * Query Parameters:
     *   - status: Filter by project status (PENDING, IN_PROGRESS, COMPLETED)
     *   - q: Search in project name and description
     *
     * Response Examples:
     * HTTP 200 OK
     * [
     *   {
     *     "id": 1,
     *     "name": "Website Redesign",
     *     "description": "Complete redesign of company website with modern UI",
     *     "status": "IN_PROGRESS",
     *     "team": "Frontend Team",
     *     "createdAt": "2025-10-27T10:30:00",
     *     "updatedAt": "2025-10-27T10:30:00"
     *   }
     * ]
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects(
            @RequestParam(required = false) String status,
            @RequestParam(required = false, name = "q") String query) {

        List<ProjectResponseDTO> projects;

        if (status != null && !status.isEmpty()) {
            projects = projectService.getProjectsByStatus(status);
        } else if (query != null && !query.isEmpty()) {
            projects = projectService.searchProjects(query);
        } else {
            projects = projectService.getAllProjects();
        }

        return ResponseEntity.ok(projects);
    }

    /**
     * GET /api/projects/{id} - Get a single project by ID
     *
     * Path Parameter:
     *   - id: The project ID
     *
     * Response Example:
     * HTTP 200 OK
     * {
     *   "id": 1,
     *   "name": "Website Redesign",
     *   "description": "Complete redesign of company website with modern UI",
     *   "status": "IN_PROGRESS",
     *   "team": "Frontend Team",
     *   "createdAt": "2025-10-27T10:30:00",
     *   "updatedAt": "2025-10-27T10:30:00"
     * }
     *
     * Error Response:
     * HTTP 404 Not Found
     * {
     *   "status": 404,
     *   "message": "Project not found with ID: 999",
     *   "timestamp": "2025-10-27T10:35:00"
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    /**
     * POST /api/projects - Create a new project
     *
     * Request Body:
     * {
     *   "name": "Website Redesign",
     *   "description": "Complete redesign of company website with modern UI",
     *   "status": "PENDING",
     *   "team": "Frontend Team"
     * }
     *
     * Response Example:
     * HTTP 201 Created
     * {
     *   "id": 1,
     *   "name": "Website Redesign",
     *   "description": "Complete redesign of company website with modern UI",
     *   "status": "PENDING",
     *   "team": "Frontend Team",
     *   "createdAt": "2025-10-27T10:30:00",
     *   "updatedAt": "2025-10-27T10:30:00"
     * }
     *
     * Error Response (Validation Error):
     * HTTP 400 Bad Request
     * {
     *   "status": 400,
     *   "message": "Validation failed",
     *   "timestamp": "2025-10-27T10:35:00",
     *   "errors": {
     *     "name": "Project name is required",
     *     "status": "Invalid status. Allowed values: PENDING, IN_PROGRESS, COMPLETED"
     *   }
     * }
     */
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody CreateProjectDTO createProjectDTO) {
        ProjectResponseDTO project = projectService.createProject(createProjectDTO);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    /**
     * PUT /api/projects/{id} - Update an existing project
     *
     * Path Parameter:
     *   - id: The project ID
     *
     * Request Body:
     * {
     *   "name": "Website Redesign v2",
     *   "description": "Complete redesign of company website with modern UI and responsive design",
     *   "status": "IN_PROGRESS",
     *   "team": "Frontend Team"
     * }
     *
     * Response Example:
     * HTTP 200 OK
     * {
     *   "id": 1,
     *   "name": "Website Redesign v2",
     *   "description": "Complete redesign of company website with modern UI and responsive design",
     *   "status": "IN_PROGRESS",
     *   "team": "Frontend Team",
     *   "createdAt": "2025-10-27T10:30:00",
     *   "updatedAt": "2025-10-27T11:00:00"
     * }
     *
     * Error Response:
     * HTTP 404 Not Found
     * {
     *   "status": 404,
     *   "message": "Project not found with ID: 999",
     *   "timestamp": "2025-10-27T10:35:00"
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectDTO updateProjectDTO) {
        ProjectResponseDTO project = projectService.updateProject(id, updateProjectDTO);
        return ResponseEntity.ok(project);
    }

    /**
     * DELETE /api/projects/{id} - Delete a project
     *
     * Path Parameter:
     *   - id: The project ID
     *
     * Response Example:
     * HTTP 204 No Content
     *
     * Error Response:
     * HTTP 404 Not Found
     * {
     *   "status": 404,
     *   "message": "Project not found with ID: 999",
     *   "timestamp": "2025-10-27T10:35:00"
     * }
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
