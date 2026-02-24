package com.thiago.projectmanagement.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.thiago.projectmanagement.domain.model.ProjectStatus;
import com.thiago.projectmanagement.infrastructure.persistence.entity.ProjectJpaEntity;

public interface SpringDataProjectRepository extends JpaRepository<ProjectJpaEntity, Long>, JpaSpecificationExecutor<ProjectJpaEntity> {
    long countByStatus(ProjectStatus status);
}
