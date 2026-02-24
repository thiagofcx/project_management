package com.thiago.projectmanagement.presentation.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import com.thiago.projectmanagement.application.dto.PagedResultDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import com.thiago.projectmanagement.application.dto.UserInputDTO;
import com.thiago.projectmanagement.application.dto.UserOutputDTO;
import com.thiago.projectmanagement.application.usecase.CreateUserUseCase;
import com.thiago.projectmanagement.application.usecase.DeleteUserUseCase;
import com.thiago.projectmanagement.application.usecase.GetCurrentUserUseCase;
import com.thiago.projectmanagement.application.usecase.GetUserByIdUseCase;
import com.thiago.projectmanagement.application.usecase.ListUsersUseCase;
import com.thiago.projectmanagement.application.usecase.UpdateUserUseCase;
import com.thiago.projectmanagement.infrastructure.security.JwtService;
import com.thiago.projectmanagement.presentation.dto.CreateUserRequest;
import com.thiago.projectmanagement.presentation.dto.UpdateUserRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Validated
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final JwtService jwtService;

    public UserController(
            CreateUserUseCase createUserUseCase,
            GetCurrentUserUseCase getCurrentUserUseCase,
            GetUserByIdUseCase getUserByIdUseCase,
            ListUsersUseCase listUsersUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            JwtService jwtService) {
        this.createUserUseCase = createUserUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<UserOutputDTO> create(
            @Valid @RequestBody CreateUserRequest request,
            HttpServletRequest httpRequest) {
        Long authenticatedUserId = extractUserIdFromRequest(httpRequest);
        UserInputDTO input = new UserInputDTO(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );
        
        UserOutputDTO result = createUserUseCase.execute(input, authenticatedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/me")
    public ResponseEntity<UserOutputDTO> getCurrentUser(HttpServletRequest httpRequest) {
        Long userId = extractUserIdFromRequest(httpRequest);
        return ResponseEntity.ok(getCurrentUserUseCase.execute(userId));
    }

    @GetMapping
    public ResponseEntity<PagedResultDTO<UserOutputDTO>> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return ResponseEntity.ok(listUsersUseCase.execute(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getUserByIdUseCase.execute(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserOutputDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest) {
        Long authenticatedUserId = extractUserIdFromRequest(httpRequest);
        UserInputDTO input = new UserInputDTO(
                request.getName(),
                request.getEmail(),
                null, 
                request.getRole()
        );
        UserOutputDTO result = updateUserUseCase.execute(id, input, authenticatedUserId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long authenticatedUserId = extractUserIdFromRequest(httpRequest);
        deleteUserUseCase.execute(id, authenticatedUserId);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromRequest(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7) : null;
        return token != null ? jwtService.extractUserId(token) : null;
    }
}
