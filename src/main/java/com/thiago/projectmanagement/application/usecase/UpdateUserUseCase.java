package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.UserInputDTO;
import com.thiago.projectmanagement.application.dto.UserOutputDTO;

public interface UpdateUserUseCase {
    UserOutputDTO execute(Long id, UserInputDTO input, Long authenticatedUserId);
}
