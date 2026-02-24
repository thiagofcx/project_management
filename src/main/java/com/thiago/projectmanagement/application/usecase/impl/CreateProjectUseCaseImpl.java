package com.thiago.projectmanagement.application.usecase.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.ProjectInputDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;
import com.thiago.projectmanagement.application.usecase.CreateProjectUseCase;
import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.domain.model.ProjectStatus;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;

@Component
public class CreateProjectUseCaseImpl implements CreateProjectUseCase {

    private final ProjectRepository projectRepository;

    public CreateProjectUseCaseImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectOutputDTO execute(ProjectInputDTO input) {
        ProjectStatus status = input.getStatus() != null ? input.getStatus() : ProjectStatus.PLANNING;

        Project project = Project.builder()
                .id(null)
                .name(input.getName())
                .description(input.getDescription())
                .startDate(input.getStartDate())
                .endDate(input.getEndDate())
                .status(status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Project saved = projectRepository.save(project);

        return new ProjectOutputDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getStartDate(),
                saved.getEndDate(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }
}
