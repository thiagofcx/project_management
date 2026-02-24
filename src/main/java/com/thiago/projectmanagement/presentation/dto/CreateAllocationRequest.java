package com.thiago.projectmanagement.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAllocationRequest {

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Resource ID is required")
    private Long resourceId;
}
