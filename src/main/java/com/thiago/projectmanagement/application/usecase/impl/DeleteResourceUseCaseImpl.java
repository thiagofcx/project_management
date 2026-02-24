package com.thiago.projectmanagement.application.usecase.impl;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.usecase.DeleteResourceUseCase;
import com.thiago.projectmanagement.domain.exception.ResourceNotFoundException;
import com.thiago.projectmanagement.domain.repository.ResourceRepository;

@Component
public class DeleteResourceUseCaseImpl implements DeleteResourceUseCase {

    private final ResourceRepository resourceRepository;

    public DeleteResourceUseCaseImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public void execute(Long id) {
        if (resourceRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        resourceRepository.deleteById(id);
    }
}
