package com.thiago.projectmanagement.presentation.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thiago.projectmanagement.application.dto.AuthRequestDTO;
import com.thiago.projectmanagement.application.dto.AuthResponseDTO;
import com.thiago.projectmanagement.application.usecase.AuthenticateUserUseCase;
import com.thiago.projectmanagement.presentation.dto.AuthRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping()
    public ResponseEntity<AuthResponseDTO> auth(@Valid @RequestBody AuthRequest request) {
        AuthRequestDTO dto = new AuthRequestDTO(
                request.getEmail(),
                request.getPassword()
        );
        
        AuthResponseDTO response = authenticateUserUseCase.execute(dto);
        return ResponseEntity.ok(response);
    }
}