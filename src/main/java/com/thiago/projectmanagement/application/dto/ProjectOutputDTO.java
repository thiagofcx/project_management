package com.thiago.projectmanagement.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.thiago.projectmanagement.domain.model.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectOutputDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private LocalDateTime createdAt;
}
