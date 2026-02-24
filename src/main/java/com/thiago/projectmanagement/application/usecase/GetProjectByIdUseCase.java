package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;

public interface GetProjectByIdUseCase {
    ProjectOutputDTO execute(Long id);
}
