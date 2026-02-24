package com.thiago.projectmanagement.application.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummaryDTO {
    private ProjectsSummaryDTO projects;
    private ResourcesSummaryDTO resources;
    private AllocationsSummaryDTO allocations;
}
