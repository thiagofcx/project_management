package com.thiago.projectmanagement.application.usecase.impl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.UserInputDTO;
import com.thiago.projectmanagement.application.dto.UserOutputDTO;
import com.thiago.projectmanagement.application.usecase.CreateUserUseCase;
import com.thiago.projectmanagement.domain.exception.UnauthorizedException;
import com.thiago.projectmanagement.domain.exception.UserAlreadyExistsException;
import com.thiago.projectmanagement.domain.exception.UserNotFoundException;
import com.thiago.projectmanagement.domain.model.Role;
import com.thiago.projectmanagement.domain.model.User;
import com.thiago.projectmanagement.domain.repository.UserRepository;

@Component
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCaseImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserOutputDTO execute(UserInputDTO input, Long authenticatedUserId) {
        User authenticatedUser = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> 
                    new UserNotFoundException());

        if (!authenticatedUser.isAdmin()) {
            throw new UnauthorizedException("Only administrators can register users");
        }

        if (userRepository.existsByEmail(input.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        Role role = input.getRole() != null ? input.getRole() : Role.USER;

        User user = new User(
                null,
                input.getName(),
                input.getEmail(),
                passwordEncoder.encode(input.getPassword()),
                role,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        User savedUser = userRepository.save(user);

        return new UserOutputDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }
}