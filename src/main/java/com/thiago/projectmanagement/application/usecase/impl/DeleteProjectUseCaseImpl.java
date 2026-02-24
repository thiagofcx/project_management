package com.thiago.projectmanagement.application.usecase.impl;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.usecase.DeleteProjectUseCase;
import com.thiago.projectmanagement.domain.exception.ProjectNotFoundException;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;

@Component
public class DeleteProjectUseCaseImpl implements DeleteProjectUseCase {

    private final ProjectRepository projectRepository;

    public DeleteProjectUseCaseImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void execute(Long id) {
        if (projectRepository.findById(id).isEmpty()) {
            throw new ProjectNotFoundException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
