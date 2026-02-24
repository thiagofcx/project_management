package com.thiago.projectmanagement.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.infrastructure.persistence.entity.ProjectJpaEntity;

@Component
public class ProjectMapper {

    public ProjectJpaEntity toEntity(Project domain) {
        if (domain == null) return null;
        return new ProjectJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getStartDate(),
                domain.getEndDate(),
                domain.getStatus(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    public Project toDomain(ProjectJpaEntity entity) {
        if (entity == null) return null;
        return Project.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
