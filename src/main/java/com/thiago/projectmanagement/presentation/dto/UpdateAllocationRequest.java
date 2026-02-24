package com.thiago.projectmanagement.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAllocationRequest {

    private Long projectId;

    private Long resourceId;
}
