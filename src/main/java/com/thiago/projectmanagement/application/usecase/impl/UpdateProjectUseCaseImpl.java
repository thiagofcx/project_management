package com.thiago.projectmanagement.application.usecase.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.ProjectInputDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;
import com.thiago.projectmanagement.application.usecase.UpdateProjectUseCase;
import com.thiago.projectmanagement.domain.exception.ProjectNotFoundException;
import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.domain.model.ProjectStatus;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;

@Component
public class UpdateProjectUseCaseImpl implements UpdateProjectUseCase {

    private final ProjectRepository projectRepository;

    public UpdateProjectUseCaseImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectOutputDTO execute(Long id, ProjectInputDTO input) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        LocalDate effectiveStart = input.getStartDate() != null ? input.getStartDate() : project.getStartDate();
        LocalDate effectiveEnd = input.getEndDate() != null ? input.getEndDate() : project.getEndDate();

        ProjectStatus status = input.getStatus() != null ? input.getStatus() : project.getStatus();

        Project toSave = Project.builder()
                .id(project.getId())
                .name(input.getName() != null ? input.getName() : project.getName())
                .description(input.getDescription() != null ? input.getDescription() : project.getDescription())
                .startDate(effectiveStart)
                .endDate(effectiveEnd)
                .status(status)
                .createdAt(project.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Project saved = projectRepository.save(toSave);

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
