package com.thiago.projectmanagement.application.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesSummaryDTO {
    private long total;
    private long allocated;
    private long unallocated;
}
