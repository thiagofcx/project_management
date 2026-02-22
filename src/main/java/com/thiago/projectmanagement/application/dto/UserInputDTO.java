package com.thiago.projectmanagement.application.dto;

import com.thiago.projectmanagement.domain.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
}
