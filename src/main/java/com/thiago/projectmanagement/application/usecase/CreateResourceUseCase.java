package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.ResourceInputDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;

public interface CreateResourceUseCase {
    ResourceOutputDTO execute(ResourceInputDTO input);
}
