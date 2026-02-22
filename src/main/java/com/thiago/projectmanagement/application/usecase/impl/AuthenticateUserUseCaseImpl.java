package com.thiago.projectmanagement.application.usecase.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.AuthRequestDTO;
import com.thiago.projectmanagement.application.dto.AuthResponseDTO;
import com.thiago.projectmanagement.application.usecase.AuthenticateUserUseCase;
import com.thiago.projectmanagement.domain.exception.InvalidCredentialsException;
import com.thiago.projectmanagement.domain.model.User;
import com.thiago.projectmanagement.domain.repository.UserRepository;
import com.thiago.projectmanagement.infrastructure.security.JwtService;

@Component
public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticateUserUseCaseImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDTO execute(AuthRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(
                token,
                user.getEmail(),
                user.getId(),
                user.getRole()
        );
    }
}
