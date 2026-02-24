package com.thiago.projectmanagement.application.usecase.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.AllocationOutputDTO;
import com.thiago.projectmanagement.application.dto.PagedResultDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;
import com.thiago.projectmanagement.application.usecase.ListAllocationsUseCase;
import com.thiago.projectmanagement.domain.model.Allocation;
import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.domain.model.Resource;
import com.thiago.projectmanagement.domain.repository.AllocationRepository;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;
import com.thiago.projectmanagement.domain.repository.ResourceRepository;

@Component
public class ListAllocationsUseCaseImpl implements ListAllocationsUseCase {

    private final AllocationRepository allocationRepository;
    private final ProjectRepository projectRepository;
    private final ResourceRepository resourceRepository;

    public ListAllocationsUseCaseImpl(
            AllocationRepository allocationRepository,
            ProjectRepository projectRepository,
            ResourceRepository resourceRepository) {
        this.allocationRepository = allocationRepository;
        this.projectRepository = projectRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public PagedResultDTO<AllocationOutputDTO> execute(int page, int size, String sortBy, String sortOrder, String resourceName, String projectName) {
        PagedResult<Allocation> paged = allocationRepository.findAll(page, size, sortBy, sortOrder, resourceName, projectName);
        List<AllocationOutputDTO> content = paged.getContent().stream()
                .map(this::toOutputDTO)
                .collect(Collectors.toList());
        return PagedResultDTO.of(
                content,
                paged.getTotalElements(),
                paged.getTotalPages(),
                paged.getNumber(),
                paged.getSize()
        );
    }

    private AllocationOutputDTO toOutputDTO(Allocation allocation) {
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
