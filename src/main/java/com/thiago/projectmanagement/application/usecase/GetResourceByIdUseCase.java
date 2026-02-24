package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;

public interface GetResourceByIdUseCase {
    ResourceOutputDTO execute(Long id);
}
