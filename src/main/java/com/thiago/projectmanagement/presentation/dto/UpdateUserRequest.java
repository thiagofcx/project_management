package com.thiago.projectmanagement.presentation.dto;

import com.thiago.projectmanagement.domain.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    private Role role;
}
