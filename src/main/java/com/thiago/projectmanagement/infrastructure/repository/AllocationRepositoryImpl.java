package com.thiago.projectmanagement.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.thiago.projectmanagement.domain.model.Allocation;
import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.ProjectStatus;
import com.thiago.projectmanagement.domain.repository.AllocationRepository;
import com.thiago.projectmanagement.infrastructure.mapper.AllocationMapper;
import com.thiago.projectmanagement.infrastructure.persistence.entity.AllocationJpaEntity;
import com.thiago.projectmanagement.infrastructure.persistence.repository.SpringDataAllocationRepository;

import jakarta.persistence.criteria.Predicate;

@Repository
public class AllocationRepositoryImpl implements AllocationRepository {

    private static final String DEFAULT_SORT = "id";
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "createdAt", "resourceName", "projectName");

    private final SpringDataAllocationRepository springDataRepository;
    private final AllocationMapper mapper;

    public AllocationRepositoryImpl(
            SpringDataAllocationRepository springDataRepository,
            AllocationMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Allocation save(Allocation allocation) {
        AllocationJpaEntity entity = mapper.toEntity(allocation);
        AllocationJpaEntity saved = springDataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Allocation> findById(Long id) {
        return springDataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Allocation> findAll() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Allocation> findByProjectId(Long projectId) {
        return springDataRepository.findByProject_Id(projectId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Allocation> findByResourceId(Long resourceId) {
        return springDataRepository.findByResource_Id(resourceId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByProjectIdAndResourceId(Long projectId, Long resourceId) {
        return springDataRepository.existsByProject_IdAndResource_Id(projectId, resourceId);
    }

    @Override
    public boolean existsByProjectIdAndResourceIdExceptId(Long projectId, Long resourceId, Long exclusionId) {
        return springDataRepository.existsByProject_IdAndResource_IdAndIdNot(projectId, resourceId, exclusionId);
    }

    @Override
    public boolean existsByResourceId(Long resourceId) {
        return springDataRepository.existsByResource_Id(resourceId);
    }

    @Override
    public boolean existsByResourceIdWithProjectStatusNotCompleted(Long resourceId) {
        return springDataRepository.existsByResource_IdAndProject_StatusNot(resourceId, ProjectStatus.COMPLETED);
    }

    @Override
    public boolean existsByResourceIdWithProjectStatusNotCompletedExceptId(Long resourceId, Long exclusionId) {
        return springDataRepository.existsByResource_IdAndProject_StatusNotAndIdNot(resourceId, exclusionId, ProjectStatus.COMPLETED);
    }

    @Override
    public PagedResult<Allocation> findAll(int page, int size) {
        return findAll(page, size, DEFAULT_SORT, "ASC", null, null);
    }

    @Override
    public PagedResult<Allocation> findAll(int page, int size, String sortBy, String sortOrder, String resourceName, String projectName) {
        String order = "DESC".equalsIgnoreCase(sortOrder != null ? sortOrder : "ASC") ? "DESC" : "ASC";
        String sortField = sortBy != null && !sortBy.isBlank() && ALLOWED_SORT_FIELDS.contains(sortBy) ? sortBy : DEFAULT_SORT;
        String entitySortField = "resourceName".equals(sortField) ? "resource.name"
                : "projectName".equals(sortField) ? "project.name"
                : sortField;
        Sort sort = "DESC".equals(order) ? Sort.by(Sort.Direction.DESC, entitySortField) : Sort.by(Sort.Direction.ASC, entitySortField);
        Specification<AllocationJpaEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new java.util.ArrayList<>();
            if (resourceName != null && !resourceName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("resource").get("name")), "%" + resourceName.toLowerCase() + "%"));
            }
            if (projectName != null && !projectName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("project").get("name")), "%" + projectName.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        var pageable = PageRequest.of(page, size, sort);
        var springPage = springDataRepository.findAll(spec, pageable);
        List<Allocation> content = springPage.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        return PagedResult.of(content, springPage.getTotalElements(), springPage.getNumber(), springPage.getSize());
    }

    @Override
    public long count() {
        return springDataRepository.count();
    }

    @Override
    public long countDistinctResourceIdsWithProjectStatusNotCompleted() {
        return springDataRepository.countDistinctResourceIdsByProjectStatusNot(ProjectStatus.COMPLETED);
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }
}
