package com.thiago.projectmanagement.application.usecase.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.UserInputDTO;
import com.thiago.projectmanagement.application.dto.UserOutputDTO;
import com.thiago.projectmanagement.application.usecase.UpdateUserUseCase;
import com.thiago.projectmanagement.domain.exception.UnauthorizedException;
import com.thiago.projectmanagement.domain.exception.UserAlreadyExistsException;
import com.thiago.projectmanagement.domain.exception.UserNotFoundException;
import com.thiago.projectmanagement.domain.model.Role;
import com.thiago.projectmanagement.domain.model.User;
import com.thiago.projectmanagement.domain.repository.UserRepository;

@Component
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserRepository userRepository;

    public UpdateUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserOutputDTO execute(Long id, UserInputDTO input, Long authenticatedUserId) {
        User authenticatedUser = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotFoundException());

        if (!authenticatedUser.isAdmin()) {
            throw new UnauthorizedException("Only administrators can update users");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userRepository.existsByEmailExceptId(input.getEmail(), id)) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        Role role = input.getRole() != null ? input.getRole() : user.getRole();

        User toSave = new User(
                user.getId(),
                input.getName(),
                input.getEmail(),
                user.getPassword(), // password não é alterado no update
                role,
                user.getCreatedAt(),
                LocalDateTime.now()
        );

        User savedUser = userRepository.save(toSave);

        return new UserOutputDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }
}
