package com.thiago.projectmanagement.application.usecase.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.PagedResultDTO;
import com.thiago.projectmanagement.application.dto.UserOutputDTO;
import com.thiago.projectmanagement.application.usecase.ListUsersUseCase;
import com.thiago.projectmanagement.domain.model.PagedResult;
import com.thiago.projectmanagement.domain.model.User;
import com.thiago.projectmanagement.domain.repository.UserRepository;

@Component
public class ListUsersUseCaseImpl implements ListUsersUseCase {

    private final UserRepository userRepository;

    public ListUsersUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public PagedResultDTO<UserOutputDTO> execute(int page, int size) {
        PagedResult<User> paged = userRepository.findAll(page, size);
        List<UserOutputDTO> content = paged.getContent().stream()
                .map(this::toOutputDTO)
                .collect(Collectors.toList());
        return PagedResultDTO.of(
                content,
                paged.getTotalElements(),
                paged.getTotalPages(),
                paged.getNumber(),
                paged.getSize()
        );
    }

    private UserOutputDTO toOutputDTO(User user) {
        return new UserOutputDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
