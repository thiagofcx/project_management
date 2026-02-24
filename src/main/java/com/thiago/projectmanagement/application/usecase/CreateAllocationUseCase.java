package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.AllocationInputDTO;
import com.thiago.projectmanagement.application.dto.AllocationOutputDTO;

public interface CreateAllocationUseCase {
    AllocationOutputDTO execute(AllocationInputDTO input);
}
