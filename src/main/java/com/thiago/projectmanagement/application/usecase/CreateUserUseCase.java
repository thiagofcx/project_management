package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.UserInputDTO;
import com.thiago.projectmanagement.application.dto.UserOutputDTO;

public interface CreateUserUseCase {
    UserOutputDTO execute(UserInputDTO input, Long authenticatedUserId);
}
