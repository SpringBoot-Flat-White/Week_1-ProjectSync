/**
 * ProjectSync - Frontend Application
 * Handles all CRUD operations for projects through the REST API
 */

const API_BASE_URL = '/api/projects';
let currentDeleteId = null;
let projects = [];

// ============================================================
// DOM ELEMENTS
// ============================================================

const projectsContainer = document.getElementById('projectsContainer');
const emptyState = document.getElementById('emptyState');
const searchInput = document.getElementById('searchInput');
const statusFilter = document.getElementById('statusFilter');
const refreshBtn = document.getElementById('refreshBtn');
const createBtn = document.getElementById('createBtn');

const projectModal = document.getElementById('projectModal');
const modalTitle = document.getElementById('modalTitle');
const projectForm = document.getElementById('projectForm');
const projectId = document.getElementById('projectId');
const projectName = document.getElementById('projectName');
const projectDescription = document.getElementById('projectDescription');
const projectStatus = document.getElementById('projectStatus');
const projectTeam = document.getElementById('projectTeam');
const closeModalBtn = document.getElementById('closeModalBtn');
const formError = document.getElementById('formError');

const deleteModal = document.getElementById('deleteModal');
const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');

// ============================================================
// EVENT LISTENERS
// ============================================================

document.addEventListener('DOMContentLoaded', () => {
    loadProjects();
});

createBtn.addEventListener('click', openCreateModal);
closeModalBtn.addEventListener('click', closeModal);
projectForm.addEventListener('submit', handleFormSubmit);

refreshBtn.addEventListener('click', loadProjects);
searchInput.addEventListener('input', debounce(handleSearch, 300));
statusFilter.addEventListener('change', handleStatusFilter);

confirmDeleteBtn.addEventListener('click', confirmDelete);
cancelDeleteBtn.addEventListener('click', closeDeleteModal);

// Close modals when clicking outside
projectModal.addEventListener('click', (e) => {
    if (e.target === projectModal) closeModal();
});
deleteModal.addEventListener('click', (e) => {
    if (e.target === deleteModal) closeDeleteModal();
});

// ============================================================
// FETCH OPERATIONS
// ============================================================

/**
 * Load all projects from the API
 */
async function loadProjects() {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error('Failed to load projects');
        projects = await response.json();
        renderProjects(projects);
    } catch (error) {
        console.error('Error loading projects:', error);
        showNotification('Failed to load projects', 'error');
    }
}

/**
 * Search projects by name or description
 */
async function handleSearch(e) {
    const query = e.target.value.trim();
    if (query === '') {
        loadProjects();
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}?q=${encodeURIComponent(query)}`);
        if (!response.ok) throw new Error('Search failed');
        projects = await response.json();
        renderProjects(projects);
    } catch (error) {
        console.error('Error searching projects:', error);
        showNotification('Search failed', 'error');
    }
}

/**
 * Filter projects by status
 */
async function handleStatusFilter(e) {
    const status = e.target.value.trim();
    if (status === '') {
        loadProjects();
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}?status=${encodeURIComponent(status)}`);
        if (!response.ok) throw new Error('Filter failed');
        projects = await response.json();
        renderProjects(projects);
    } catch (error) {
        console.error('Error filtering projects:', error);
        showNotification('Filter failed', 'error');
    }
}

/**
 * Create a new project
 */
async function createProject(data) {
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (!response.ok) {
            const error = await response.json();
            throw error;
        }

        const newProject = await response.json();
        projects.unshift(newProject);
        renderProjects(projects);
        showNotification('Project created successfully!', 'success');
        return true;
    } catch (error) {
        console.error('Error creating project:', error);
        throw error;
    }
}

/**
 * Update an existing project
 */
async function updateProject(id, data) {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (!response.ok) {
            const error = await response.json();
            throw error;
        }

        const updatedProject = await response.json();
        const index = projects.findIndex(p => p.id === id);
        if (index !== -1) {
            projects[index] = updatedProject;
        }
        renderProjects(projects);
        showNotification('Project updated successfully!', 'success');
        return true;
    } catch (error) {
        console.error('Error updating project:', error);
        throw error;
    }
}

/**
 * Delete a project
 */
async function deleteProject(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE',
        });

        if (!response.ok) throw new Error('Failed to delete project');

        projects = projects.filter(p => p.id !== id);
        renderProjects(projects);
        showNotification('Project deleted successfully!', 'success');
        return true;
    } catch (error) {
        console.error('Error deleting project:', error);
        throw error;
    }
}

// ============================================================
// RENDERING
// ============================================================

/**
 * Render projects to the DOM
 */
function renderProjects(projectList) {
    projectsContainer.innerHTML = '';

    if (projectList.length === 0) {
        emptyState.classList.remove('hidden');
        return;
    }

    emptyState.classList.add('hidden');

    projectList.forEach(project => {
        const card = createProjectCard(project);
        projectsContainer.appendChild(card);
    });
}

/**
 * Create a project card element
 */
function createProjectCard(project) {
    const card = document.createElement('div');
    card.className = 'bg-white rounded-lg shadow-md hover:shadow-lg transition overflow-hidden';

    const statusColor = getStatusColor(project.status);
    const formattedDate = new Date(project.updatedAt).toLocaleDateString();

    card.innerHTML = `
        <div class="p-6">
            <div class="flex justify-between items-start mb-3">
                <h3 class="text-xl font-bold text-gray-800 flex-1 pr-2">${escapeHtml(project.name)}</h3>
                <span class="px-3 py-1 rounded-full text-xs font-semibold ${statusColor}">
                    ${project.status}
                </span>
            </div>

            <p class="text-gray-600 text-sm mb-4 line-clamp-2">${escapeHtml(project.description)}</p>

            <div class="space-y-2 text-sm text-gray-500 mb-4">
                <p><strong>Team:</strong> ${escapeHtml(project.team)}</p>
                <p><strong>Updated:</strong> ${formattedDate}</p>
            </div>

            <div class="flex gap-2">
                <button
                    class="flex-1 px-3 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition text-sm"
                    onclick="handleEdit(${project.id})"
                >
                    Edit
                </button>
                <button
                    class="flex-1 px-3 py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg font-medium transition text-sm"
                    onclick="handleDeleteClick(${project.id})"
                >
                    Delete
                </button>
            </div>
        </div>
    `;

    return card;
}

/**
 * Get CSS classes for status badge
 */
function getStatusColor(status) {
    const colors = {
        'PENDING': 'bg-yellow-100 text-yellow-800',
        'IN_PROGRESS': 'bg-blue-100 text-blue-800',
        'COMPLETED': 'bg-green-100 text-green-800',
    };
    return colors[status] || 'bg-gray-100 text-gray-800';
}

// ============================================================
// MODAL OPERATIONS
// ============================================================

/**
 * Open create project modal
 */
function openCreateModal() {
    resetForm();
    modalTitle.textContent = 'Create New Project';
    projectId.value = '';
    projectModal.classList.remove('hidden');
    projectName.focus();
}

/**
 * Open edit project modal
 */
function openEditModal(project) {
    resetForm();
    modalTitle.textContent = 'Edit Project';
    projectId.value = project.id;
    projectName.value = project.name;
    projectDescription.value = project.description;
    projectStatus.value = project.status;
    projectTeam.value = project.team;
    projectModal.classList.remove('hidden');
    projectName.focus();
}

/**
 * Close modal
 */
function closeModal() {
    projectModal.classList.add('hidden');
    resetForm();
}

/**
 * Reset form
 */
function resetForm() {
    projectForm.reset();
    projectId.value = '';
    clearFormErrors();
    formError.classList.add('hidden');
}

/**
 * Clear form validation errors
 */
function clearFormErrors() {
    document.querySelectorAll('[id$="Error"]').forEach(el => {
        el.textContent = '';
        el.classList.add('hidden');
    });
}

// ============================================================
// FORM HANDLING
// ============================================================

/**
 * Handle form submission (create or update)
 */
async function handleFormSubmit(e) {
    e.preventDefault();
    clearFormErrors();
    formError.classList.add('hidden');

    const id = projectId.value;
    const data = {
        name: projectName.value.trim(),
        description: projectDescription.value.trim(),
        status: projectStatus.value,
        team: projectTeam.value.trim(),
    };

    try {
        if (id) {
            await updateProject(id, data);
        } else {
            await createProject(data);
        }
        closeModal();
    } catch (error) {
        handleFormError(error);
    }
}

/**
 * Handle form errors and display them
 */
function handleFormError(error) {
    if (error.errors) {
        // Validation errors
        Object.keys(error.errors).forEach(field => {
            const errorElement = document.getElementById(`${field}Error`);
            if (errorElement) {
                errorElement.textContent = error.errors[field];
                errorElement.classList.remove('hidden');
            }
        });
    } else if (error.message) {
        // General error
        formError.textContent = error.message;
        formError.classList.remove('hidden');
    } else {
        formError.textContent = 'An error occurred. Please try again.';
        formError.classList.remove('hidden');
    }
}

/**
 * Handle edit button click
 */
function handleEdit(id) {
    const project = projects.find(p => p.id === id);
    if (project) {
        openEditModal(project);
    }
}

// ============================================================
// DELETE OPERATIONS
// ============================================================

/**
 * Handle delete button click
 */
function handleDeleteClick(id) {
    currentDeleteId = id;
    deleteModal.classList.remove('hidden');
}

/**
 * Confirm delete
 */
async function confirmDelete() {
    if (currentDeleteId) {
        try {
            await deleteProject(currentDeleteId);
            closeDeleteModal();
        } catch (error) {
            console.error('Delete error:', error);
            showNotification('Failed to delete project', 'error');
        }
    }
}

/**
 * Close delete confirmation modal
 */
function closeDeleteModal() {
    deleteModal.classList.add('hidden');
    currentDeleteId = null;
}

// ============================================================
// UTILITY FUNCTIONS
// ============================================================

/**
 * Debounce function to limit function calls
 */
function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func(...args), delay);
    };
}

/**
 * Escape HTML to prevent XSS
 */
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}

/**
 * Show notification (simple implementation)
 */
function showNotification(message, type = 'info') {
    // This is a simple console notification
    // In a real app, you might use a toast library
    console.log(`[${type.toUpperCase()}] ${message}`);
}
