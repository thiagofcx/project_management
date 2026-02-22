package com.thiago.projectmanagement.application.usecase.impl;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.usecase.DeleteUserUseCase;
import com.thiago.projectmanagement.domain.exception.UnauthorizedException;
import com.thiago.projectmanagement.domain.exception.UserNotFoundException;
import com.thiago.projectmanagement.domain.model.User;
import com.thiago.projectmanagement.domain.repository.UserRepository;

@Component
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void execute(Long id, Long authenticatedUserId) {
        User authenticatedUser = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotFoundException());

        if (!authenticatedUser.isAdmin()) {
            throw new UnauthorizedException("Only administrators can delete users");
        }

        if (!userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }
}
