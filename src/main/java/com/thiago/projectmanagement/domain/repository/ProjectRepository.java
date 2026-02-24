package com.thiago.projectmanagement.domain.repository;

import java.util.List;
import java.util.Optional;

import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.Project;
import com.thiago.projectmanagement.domain.model.ProjectStatus;

public interface ProjectRepository {
    Project save(Project project);
    Optional<Project> findById(Long id);
    List<Project> findAll();
    PagedResult<Project> findAll(int page, int size);
    PagedResult<Project> findAll(int page, int size, String sortBy, String sortOrder, String name, ProjectStatus status);
    long count();
    long countByStatus(ProjectStatus status);
    void deleteById(Long id);
}
