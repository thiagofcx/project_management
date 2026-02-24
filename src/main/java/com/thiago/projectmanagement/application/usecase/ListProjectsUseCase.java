package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.PagedResultDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;
import com.thiago.projectmanagement.domain.model.ProjectStatus;

public interface ListProjectsUseCase {
    PagedResultDTO<ProjectOutputDTO> execute(int page, int size, String sortBy, String sortOrder, String name, ProjectStatus status);
}
