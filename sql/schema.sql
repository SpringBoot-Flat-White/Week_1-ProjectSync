-- ProjectSync Database Schema Script
-- This script creates the database and tables for the ProjectSync application

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS projectsync_db;
USE projectsync_db;

-- Drop existing table if it exists (optional, for development)
DROP TABLE IF EXISTS projects;

-- Create projects table
CREATE TABLE projects (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    team VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_team (team),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data for testing
INSERT INTO projects (name, description, status, team) VALUES
('Website Redesign', 'Complete redesign of company website with modern UI and responsive design', 'IN_PROGRESS', 'Frontend Team'),
('Mobile App Development', 'Build cross-platform mobile application for customer engagement', 'PENDING', 'Mobile Team'),
('Database Migration', 'Migrate legacy database to cloud-based MySQL infrastructure', 'COMPLETED', 'Backend Team'),
('API Integration', 'Integrate third-party payment and analytics APIs into main platform', 'IN_PROGRESS', 'Backend Team'),
('Security Audit', 'Comprehensive security audit and vulnerability assessment of all systems', 'PENDING', 'DevOps Team');

-- Verify data insertion
SELECT COUNT(*) as total_projects FROM projects;
