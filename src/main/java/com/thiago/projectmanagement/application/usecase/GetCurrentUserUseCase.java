package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.UserOutputDTO;

public interface GetCurrentUserUseCase {
    UserOutputDTO execute(Long userId);
}