package com.thiago.projectmanagement.application.usecase.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.PagedResultDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;
import com.thiago.projectmanagement.application.usecase.ListProjectsUseCase;
import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.domain.model.ProjectStatus;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;

@Component
public class ListProjectsUseCaseImpl implements ListProjectsUseCase {

    private final ProjectRepository projectRepository;

    public ListProjectsUseCaseImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public PagedResultDTO<ProjectOutputDTO> execute(int page, int size, String sortBy, String sortOrder, String name, ProjectStatus status) {
        PagedResult<Project> paged = projectRepository.findAll(page, size, sortBy, sortOrder, name, status);
        List<ProjectOutputDTO> content = paged.getContent().stream()
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

    private ProjectOutputDTO toOutputDTO(Project project) {
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
}
