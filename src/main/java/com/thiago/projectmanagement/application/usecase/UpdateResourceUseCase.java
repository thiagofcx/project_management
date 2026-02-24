package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.ResourceInputDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;

public interface UpdateResourceUseCase {
    ResourceOutputDTO execute(Long id, ResourceInputDTO input);
}
