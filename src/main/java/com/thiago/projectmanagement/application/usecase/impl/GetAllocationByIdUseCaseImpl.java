package com.thiago.projectmanagement.application.usecase.impl;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.AllocationOutputDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;
import com.thiago.projectmanagement.application.usecase.GetAllocationByIdUseCase;
import com.thiago.projectmanagement.domain.exception.AllocationNotFoundException;
import com.thiago.projectmanagement.domain.model.Allocation;
import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.domain.model.Resource;
import com.thiago.projectmanagement.domain.repository.AllocationRepository;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;
import com.thiago.projectmanagement.domain.repository.ResourceRepository;

@Component
public class GetAllocationByIdUseCaseImpl implements GetAllocationByIdUseCase {

    private final AllocationRepository allocationRepository;
    private final ProjectRepository projectRepository;
    private final ResourceRepository resourceRepository;

    public GetAllocationByIdUseCaseImpl(
            AllocationRepository allocationRepository,
            ProjectRepository projectRepository,
            ResourceRepository resourceRepository) {
        this.allocationRepository = allocationRepository;
        this.projectRepository = projectRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public AllocationOutputDTO execute(Long id) {
        Allocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new AllocationNotFoundException("Allocation not found"));

        Project project = projectRepository.findById(allocation.getProjectId()).orElse(null);
        Resource resource = resourceRepository.findById(allocation.getResourceId()).orElse(null);

        return AllocationOutputDTO.builder()
                .id(allocation.getId())
                .project(toProjectOutputDTO(project))
                .resource(toResourceOutputDTO(resource))
                .createdAt(allocation.getCreatedAt())
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
