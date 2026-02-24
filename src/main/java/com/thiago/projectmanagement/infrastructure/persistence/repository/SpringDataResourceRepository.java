package com.thiago.projectmanagement.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.thiago.projectmanagement.infrastructure.persistence.entity.ResourceJpaEntity;

public interface SpringDataResourceRepository extends JpaRepository<ResourceJpaEntity, Long>, JpaSpecificationExecutor<ResourceJpaEntity> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
