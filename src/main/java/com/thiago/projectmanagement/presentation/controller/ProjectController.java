package com.thiago.projectmanagement.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import com.thiago.projectmanagement.application.dto.PagedResultDTO;
import com.thiago.projectmanagement.application.dto.ProjectInputDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;
import com.thiago.projectmanagement.domain.model.ProjectStatus;
import com.thiago.projectmanagement.application.usecase.CreateProjectUseCase;
import com.thiago.projectmanagement.application.usecase.DeleteProjectUseCase;
import com.thiago.projectmanagement.application.usecase.GetProjectByIdUseCase;
import com.thiago.projectmanagement.application.usecase.ListProjectsUseCase;
import com.thiago.projectmanagement.application.usecase.UpdateProjectUseCase;
import com.thiago.projectmanagement.presentation.dto.CreateProjectRequest;
import com.thiago.projectmanagement.presentation.dto.UpdateProjectRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/projects")
@Validated
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final GetProjectByIdUseCase getProjectByIdUseCase;
    private final ListProjectsUseCase listProjectsUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final DeleteProjectUseCase deleteProjectUseCase;

    public ProjectController(
            CreateProjectUseCase createProjectUseCase,
            GetProjectByIdUseCase getProjectByIdUseCase,
            ListProjectsUseCase listProjectsUseCase,
            UpdateProjectUseCase updateProjectUseCase,
            DeleteProjectUseCase deleteProjectUseCase) {
        this.createProjectUseCase = createProjectUseCase;
        this.getProjectByIdUseCase = getProjectByIdUseCase;
        this.listProjectsUseCase = listProjectsUseCase;
        this.updateProjectUseCase = updateProjectUseCase;
        this.deleteProjectUseCase = deleteProjectUseCase;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<ProjectOutputDTO> create(@Valid @RequestBody CreateProjectRequest request) {
        ProjectStatus status = parseStatus(request.getStatus());
        ProjectInputDTO input = new ProjectInputDTO(
                request.getName(),
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate(),
                status
        );
        ProjectOutputDTO result = createProjectUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<PagedResultDTO<ProjectOutputDTO>> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "sort_order", defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status) {
        ProjectStatus projectStatus = parseStatus(status);
        return ResponseEntity.ok(listProjectsUseCase.execute(page, size, sortBy, sortOrder, name, projectStatus));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectOutputDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getProjectByIdUseCase.execute(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectOutputDTO> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody UpdateProjectRequest request) {
        ProjectStatus status = parseStatus(request.getStatus());
        ProjectInputDTO input = new ProjectInputDTO(
                request.getName(),
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate(),
                status
        );
        ProjectOutputDTO result = updateProjectUseCase.execute(id, input);
        return ResponseEntity.ok(result);
    }

    private static ProjectStatus parseStatus(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return ProjectStatus.valueOf(value);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteProjectUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
