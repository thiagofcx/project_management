package com.thiago.projectmanagement.application.usecase.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.PagedResultDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;
import com.thiago.projectmanagement.application.usecase.ListResourcesUseCase;
import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.Resource;
import com.thiago.projectmanagement.domain.repository.ResourceRepository;

@Component
public class ListResourcesUseCaseImpl implements ListResourcesUseCase {

    private final ResourceRepository resourceRepository;

    public ListResourcesUseCaseImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public PagedResultDTO<ResourceOutputDTO> execute(int page, int size, String sortBy, String sortOrder, String name, String skills) {
        PagedResult<Resource> paged = resourceRepository.findAll(page, size, sortBy, sortOrder, name, skills);
        List<ResourceOutputDTO> content = paged.getContent().stream()
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

    private ResourceOutputDTO toOutputDTO(Resource resource) {
        return new ResourceOutputDTO(
                resource.getId(),
                resource.getName(),
                resource.getEmail(),
                resource.getSkills(),
                resource.getCreatedAt()
        );
    }
}
