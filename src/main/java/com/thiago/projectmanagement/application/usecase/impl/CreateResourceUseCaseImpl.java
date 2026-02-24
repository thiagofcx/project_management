package com.thiago.projectmanagement.application.usecase.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.ResourceInputDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;
import com.thiago.projectmanagement.application.usecase.CreateResourceUseCase;
import com.thiago.projectmanagement.domain.exception.ResourceAlreadyExistsException;
import com.thiago.projectmanagement.domain.model.Resource;
import com.thiago.projectmanagement.domain.repository.ResourceRepository;

@Component
public class CreateResourceUseCaseImpl implements CreateResourceUseCase {

    private final ResourceRepository resourceRepository;

    public CreateResourceUseCaseImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public ResourceOutputDTO execute(ResourceInputDTO input) {
        if (resourceRepository.existsByEmail(input.getEmail())) {
            throw new ResourceAlreadyExistsException();
        }

        Resource resource = Resource.builder()
                .id(null)
                .name(input.getName())
                .email(input.getEmail())
                .skills(input.getSkills())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Resource saved = resourceRepository.save(resource);

        return new ResourceOutputDTO(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getSkills(),
                saved.getCreatedAt()
        );
    }
}
