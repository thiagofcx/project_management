package com.thiago.projectmanagement.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllocationInputDTO {
    private Long projectId;
    private Long resourceId;
}
