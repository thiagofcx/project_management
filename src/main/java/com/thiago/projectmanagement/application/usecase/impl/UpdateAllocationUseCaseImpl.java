package com.thiago.projectmanagement.application.usecase.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.AllocationInputDTO;
import com.thiago.projectmanagement.application.dto.AllocationOutputDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;
import com.thiago.projectmanagement.application.usecase.UpdateAllocationUseCase;
import com.thiago.projectmanagement.domain.exception.AllocationNotFoundException;
import com.thiago.projectmanagement.domain.exception.ProjectNotFoundException;
import com.thiago.projectmanagement.domain.exception.ResourceAlreadyAllocatedException;
import com.thiago.projectmanagement.domain.exception.ResourceNotFoundException;
import com.thiago.projectmanagement.domain.model.Allocation;
import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.domain.model.Resource;
import com.thiago.projectmanagement.domain.repository.AllocationRepository;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;
import com.thiago.projectmanagement.domain.repository.ResourceRepository;

@Component
public class UpdateAllocationUseCaseImpl implements UpdateAllocationUseCase {

    private final AllocationRepository allocationRepository;
    private final ProjectRepository projectRepository;
    private final ResourceRepository resourceRepository;

    public UpdateAllocationUseCaseImpl(
            AllocationRepository allocationRepository,
            ProjectRepository projectRepository,
            ResourceRepository resourceRepository) {
        this.allocationRepository = allocationRepository;
        this.projectRepository = projectRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public AllocationOutputDTO execute(Long id, AllocationInputDTO input) {
        Allocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new AllocationNotFoundException("Allocation not found"));

        if (input.getProjectId() != null && projectRepository.findById(input.getProjectId()).isEmpty()) {
            throw new ProjectNotFoundException("Project not found");
        }
        if (input.getResourceId() != null && resourceRepository.findById(input.getResourceId()).isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }

        Long projectId = input.getProjectId() != null ? input.getProjectId() : allocation.getProjectId();
        Long resourceId = input.getResourceId() != null ? input.getResourceId() : allocation.getResourceId();

        if (allocationRepository.existsByResourceIdWithProjectStatusNotCompletedExceptId(resourceId, allocation.getId())) {
            throw new ResourceAlreadyAllocatedException("Resource is already allocated to another project");
        }

        Allocation toSave = Allocation.builder()
                .id(allocation.getId())
                .projectId(projectId)
                .resourceId(resourceId)
                .createdAt(allocation.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Allocation saved = allocationRepository.save(toSave);

        Project project = projectRepository.findById(saved.getProjectId()).orElse(null);
        Resource resource = resourceRepository.findById(saved.getResourceId()).orElse(null);

        return AllocationOutputDTO.builder()
                .id(saved.getId())
                .project(toProjectOutputDTO(project))
                .resource(toResourceOutputDTO(resource))
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private ProjectOutputDTO toProjectOutputDTO(Project project) {
        if (project == null) return null;
        return new ProjectOutputDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                project.getStatus(),
                project.getCreatedAt()
        );
    }

    private ResourceOutputDTO toResourceOutputDTO(Resource resource) {
        if (resource == null) return null;
        return new ResourceOutputDTO(
                resource.getId(),
                resource.getName(),
                resource.getEmail(),
                resource.getSkills(),
                resource.getCreatedAt()
        );
    }
}
