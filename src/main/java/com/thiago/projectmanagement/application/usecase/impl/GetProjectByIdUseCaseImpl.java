package com.thiago.projectmanagement.application.usecase.impl;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;
import com.thiago.projectmanagement.application.usecase.GetProjectByIdUseCase;
import com.thiago.projectmanagement.domain.exception.ProjectNotFoundException;
import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;

@Component
public class GetProjectByIdUseCaseImpl implements GetProjectByIdUseCase {

    private final ProjectRepository projectRepository;

    public GetProjectByIdUseCaseImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectOutputDTO execute(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

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
