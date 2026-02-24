package com.thiago.projectmanagement.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceInputDTO {
    private String name;
    private String email;
    private String skills;
}
