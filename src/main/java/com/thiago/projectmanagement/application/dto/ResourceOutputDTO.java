package com.thiago.projectmanagement.application.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceOutputDTO {
    private Long id;
    private String name;
    private String email;
    private String skills;
    private LocalDateTime createdAt;
}
