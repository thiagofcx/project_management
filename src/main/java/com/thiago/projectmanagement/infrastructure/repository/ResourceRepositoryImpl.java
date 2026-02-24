package com.thiago.projectmanagement.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.Resource;
import com.thiago.projectmanagement.domain.repository.ResourceRepository;
import com.thiago.projectmanagement.infrastructure.mapper.ResourceMapper;
import com.thiago.projectmanagement.infrastructure.persistence.entity.ResourceJpaEntity;
import com.thiago.projectmanagement.infrastructure.persistence.repository.SpringDataResourceRepository;

import jakarta.persistence.criteria.Predicate;

@Repository
public class ResourceRepositoryImpl implements ResourceRepository {

    private static final String DEFAULT_SORT = "name";
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "name", "email", "skills", "createdAt");

    private final SpringDataResourceRepository springDataRepository;
    private final ResourceMapper mapper;

    public ResourceRepositoryImpl(
            SpringDataResourceRepository springDataRepository,
            ResourceMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Resource save(Resource resource) {
        ResourceJpaEntity entity = mapper.toEntity(resource);
        ResourceJpaEntity saved = springDataRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Resource> findById(Long id) {
        return springDataRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Resource> findAll() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PagedResult<Resource> findAll(int page, int size) {
        return findAll(page, size, DEFAULT_SORT, "ASC", null, null);
    }

    @Override
    public PagedResult<Resource> findAll(int page, int size, String sortBy, String sortOrder, String name, String skills) {
        String order = "DESC".equalsIgnoreCase(sortOrder != null ? sortOrder : "ASC") ? "DESC" : "ASC";
        String sortField = sortBy != null && !sortBy.isBlank() && ALLOWED_SORT_FIELDS.contains(sortBy) ? sortBy : DEFAULT_SORT;
        Sort sort = "DESC".equals(order) ? Sort.by(Sort.Direction.DESC, sortField) : Sort.by(Sort.Direction.ASC, sortField);
        Specification<ResourceJpaEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new java.util.ArrayList<>();
            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (skills != null && !skills.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("skills")), "%" + skills.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        var pageable = PageRequest.of(page, size, sort);
        var springPage = springDataRepository.findAll(spec, pageable);
        List<Resource> content = springPage.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        return PagedResult.of(content, springPage.getTotalElements(), springPage.getNumber(), springPage.getSize());
    }

    @Override
    public long count() {
        return springDataRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailExceptId(String email, Long id) {
        return springDataRepository.existsByEmailAndIdNot(email, id);
    }
}
