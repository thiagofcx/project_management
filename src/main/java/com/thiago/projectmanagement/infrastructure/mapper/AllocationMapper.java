package com.thiago.projectmanagement.infrastructure.mapper;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.domain.model.Allocation;
import com.thiago.projectmanagement.infrastructure.persistence.entity.AllocationJpaEntity;
import com.thiago.projectmanagement.infrastructure.persistence.entity.ProjectJpaEntity;
import com.thiago.projectmanagement.infrastructure.persistence.entity.ResourceJpaEntity;

@Component
public class AllocationMapper {

    private final EntityManager entityManager;

    public AllocationMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public AllocationJpaEntity toEntity(Allocation domain) {
        if (domain == null) return null;
        AllocationJpaEntity entity = new AllocationJpaEntity();
        entity.setId(domain.getId());
        if (domain.getProjectId() != null) {
            entity.setProject(entityManager.getReference(ProjectJpaEntity.class, domain.getProjectId()));
        }
        if (domain.getResourceId() != null) {
            entity.setResource(entityManager.getReference(ResourceJpaEntity.class, domain.getResourceId()));
        }
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public Allocation toDomain(AllocationJpaEntity entity) {
        if (entity == null) return null;
        return Allocation.builder()
                .id(entity.getId())
                .projectId(entity.getProject().getId())
                .resourceId(entity.getResource().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
