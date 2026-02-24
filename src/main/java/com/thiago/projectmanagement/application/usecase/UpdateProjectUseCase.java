package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.ProjectInputDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;

public interface UpdateProjectUseCase {
    ProjectOutputDTO execute(Long id, ProjectInputDTO input);
}
