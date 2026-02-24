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
public class Resource {
    private Long id;
    private String name;
    private String email;
    private String skills;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
