package com.thiago.projectmanagement.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Allocation {
    private Long id;
    private Long projectId;
    private Long resourceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
