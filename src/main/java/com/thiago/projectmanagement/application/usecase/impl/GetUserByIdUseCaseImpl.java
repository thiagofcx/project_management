package com.thiago.projectmanagement.application.usecase.impl;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.UserOutputDTO;
import com.thiago.projectmanagement.application.usecase.GetUserByIdUseCase;
import com.thiago.projectmanagement.domain.exception.UserNotFoundException;
import com.thiago.projectmanagement.domain.model.User;
import com.thiago.projectmanagement.domain.repository.UserRepository;

@Component
public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {

    private final UserRepository userRepository;

    public GetUserByIdUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserOutputDTO execute(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserOutputDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
