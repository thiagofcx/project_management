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
public class AllocationOutputDTO {
    private Long id;
    private ProjectOutputDTO project;
    private ResourceOutputDTO resource;
    private LocalDateTime createdAt;
}
