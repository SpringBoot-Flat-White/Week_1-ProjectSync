package com.example.ProjectSync.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO for Project responses.
 * Used to send project data back to the client in REST API responses.
 * Never exposes internal JPA entity directly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String status;
    private String team;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
