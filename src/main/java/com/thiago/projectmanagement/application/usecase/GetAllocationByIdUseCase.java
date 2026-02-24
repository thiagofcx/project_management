package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.AllocationOutputDTO;

public interface GetAllocationByIdUseCase {
    AllocationOutputDTO execute(Long id);
}
