package com.thiago.projectmanagement.application.usecase.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.ResourceInputDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;
import com.thiago.projectmanagement.application.usecase.UpdateResourceUseCase;
import com.thiago.projectmanagement.domain.exception.ResourceAlreadyExistsException;
import com.thiago.projectmanagement.domain.exception.ResourceNotFoundException;
import com.thiago.projectmanagement.domain.model.Resource;
import com.thiago.projectmanagement.domain.repository.ResourceRepository;

@Component
public class UpdateResourceUseCaseImpl implements UpdateResourceUseCase {

    private final ResourceRepository resourceRepository;

    public UpdateResourceUseCaseImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public ResourceOutputDTO execute(Long id, ResourceInputDTO input) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        if (resourceRepository.existsByEmailExceptId(input.getEmail(), id)) {
            throw new ResourceAlreadyExistsException("Email already in use");
        }

        Resource toSave = Resource.builder()
                .id(resource.getId())
                .name(input.getName() != null ? input.getName() : resource.getName())
                .email(input.getEmail() != null ? input.getEmail() : resource.getEmail())
                .skills(input.getSkills() != null ? input.getSkills() : resource.getSkills())
                .createdAt(resource.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Resource saved = resourceRepository.save(toSave);

        return new ResourceOutputDTO(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getSkills(),
                saved.getCreatedAt()
        );
    }
}
