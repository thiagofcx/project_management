package com.thiago.projectmanagement.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.domain.model.ProjectStatus;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;
import com.thiago.projectmanagement.infrastructure.mapper.ProjectMapper;
import com.thiago.projectmanagement.infrastructure.persistence.entity.ProjectJpaEntity;
import com.thiago.projectmanagement.infrastructure.persistence.repository.SpringDataProjectRepository;

import jakarta.persistence.criteria.Predicate;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private static final String DEFAULT_SORT = "name";
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "name", "description", "startDate", "endDate", "status", "createdAt");

    private final SpringDataProjectRepository springDataRepository;
    private final ProjectMapper mapper;

    public ProjectRepositoryImpl(
            SpringDataProjectRepository springDataRepository,
            ProjectMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Project save(Project project) {
        ProjectJpaEntity entity = mapper.toEntity(project);
        ProjectJpaEntity saved = springDataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return springDataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Project> findAll() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PagedResult<Project> findAll(int page, int size) {
        return findAll(page, size, DEFAULT_SORT, "ASC", null, null);
    }

    @Override
    public PagedResult<Project> findAll(int page, int size, String sortBy, String sortOrder, String name, ProjectStatus status) {
        String order = "DESC".equalsIgnoreCase(sortOrder != null ? sortOrder : "ASC") ? "DESC" : "ASC";
        String sortField = sortBy != null && !sortBy.isBlank() && ALLOWED_SORT_FIELDS.contains(sortBy) ? sortBy : DEFAULT_SORT;
        Sort sort = "DESC".equals(order) ? Sort.by(Sort.Direction.DESC, sortField) : Sort.by(Sort.Direction.ASC, sortField);
        Specification<ProjectJpaEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new java.util.ArrayList<>();
            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        var pageable = PageRequest.of(page, size, sort);
        var springPage = springDataRepository.findAll(spec, pageable);
        List<Project> content = springPage.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        return PagedResult.of(content, springPage.getTotalElements(), springPage.getNumber(), springPage.getSize());
    }

    @Override
    public long count() {
        return springDataRepository.count();
    }

    @Override
    public long countByStatus(ProjectStatus status) {
        return springDataRepository.countByStatus(status);
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }
}
