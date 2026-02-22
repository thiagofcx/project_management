package com.thiago.projectmanagement.application.usecase;

public interface DeleteUserUseCase {
    void execute(Long id, Long authenticatedUserId);
}
