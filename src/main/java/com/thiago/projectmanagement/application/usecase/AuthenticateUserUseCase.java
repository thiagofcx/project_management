package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.AuthRequestDTO;
import com.thiago.projectmanagement.application.dto.AuthResponseDTO;

public interface AuthenticateUserUseCase {
    AuthResponseDTO execute(AuthRequestDTO request);
}
