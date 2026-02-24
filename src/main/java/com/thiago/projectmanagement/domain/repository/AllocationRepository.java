package com.thiago.projectmanagement.domain.repository;

import java.util.List;
import java.util.Optional;

import com.thiago.projectmanagement.domain.model.Allocation;
import com.thiago.projectmanagement.domain.model.PagedResult;

public interface AllocationRepository {
    Allocation save(Allocation allocation);
    Optional<Allocation> findById(Long id);
    List<Allocation> findAll();
    List<Allocation> findByProjectId(Long projectId);
    List<Allocation> findByResourceId(Long resourceId);
    boolean existsByProjectIdAndResourceId(Long projectId, Long resourceId);
    boolean existsByProjectIdAndResourceIdExceptId(Long projectId, Long resourceId, Long exclusionId);
    boolean existsByResourceId(Long resourceId);
    /** Verifica se o recurso possui alocação em projeto com status diferente de COMPLETED. */
    boolean existsByResourceIdWithProjectStatusNotCompleted(Long resourceId);
    /** Verifica se o recurso possui alocação em projeto com status diferente de COMPLETED, excluindo a alocação com o id dado. */
    boolean existsByResourceIdWithProjectStatusNotCompletedExceptId(Long resourceId, Long exclusionId);
    PagedResult<Allocation> findAll(int page, int size);
    PagedResult<Allocation> findAll(int page, int size, String sortBy, String sortOrder, String resourceName, String projectName);
    long count();
    /** Quantidade de recursos distintos que possuem ao menos uma alocação em projeto com status diferente de COMPLETED. */
    long countDistinctResourceIdsWithProjectStatusNotCompleted();
    void deleteById(Long id);
}
