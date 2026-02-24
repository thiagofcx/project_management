package com.thiago.projectmanagement.application.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectsByStatusDTO {
    private long planning;
    private long inProgress;
    private long onHold;
    private long completed;
}
