package com.thiago.projectmanagement.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.domain.model.Resource;
import com.thiago.projectmanagement.infrastructure.persistence.entity.ResourceJpaEntity;

@Component
public class ResourceMapper {

    public ResourceJpaEntity toEntity(Resource domain) {
        if (domain == null) return null;
        return new ResourceJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getEmail(),
                domain.getSkills(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    public Resource toDomain(ResourceJpaEntity entity) {
        if (entity == null) return null;
        return Resource.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .skills(entity.getSkills())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
