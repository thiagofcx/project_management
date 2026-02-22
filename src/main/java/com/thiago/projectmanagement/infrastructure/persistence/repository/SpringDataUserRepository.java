package com.thiago.projectmanagement.infrastructure.persistence.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiago.projectmanagement.infrastructure.persistence.entity.UserJpaEntity;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    Optional<UserJpaEntity> findByEmail(String email);
}
