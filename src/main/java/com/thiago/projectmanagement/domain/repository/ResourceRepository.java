package com.thiago.projectmanagement.domain.repository;

import java.util.List;
import java.util.Optional;

import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.Resource;

public interface ResourceRepository {
    Resource save(Resource resource);
    Optional<Resource> findById(Long id);
    List<Resource> findAll();
    PagedResult<Resource> findAll(int page, int size);
    PagedResult<Resource> findAll(int page, int size, String sortBy, String sortOrder, String name, String skills);
    long count();
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByEmailExceptId(String email, Long id);
}
