package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.UserOutputDTO;

public interface GetUserByIdUseCase {
    UserOutputDTO execute(Long id);
}
