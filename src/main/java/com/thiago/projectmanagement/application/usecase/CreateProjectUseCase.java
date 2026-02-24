package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.ProjectInputDTO;
import com.thiago.projectmanagement.application.dto.ProjectOutputDTO;

public interface CreateProjectUseCase {
    ProjectOutputDTO execute(ProjectInputDTO input);
}
