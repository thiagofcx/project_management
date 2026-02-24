package com.thiago.projectmanagement.presentation.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Pattern(regexp = "^(|PLANNING|IN_PROGRESS|ON_HOLD|COMPLETED)$",
            message = "Invalid status. Allowed values: PLANNING, IN_PROGRESS, ON_HOLD, COMPLETED")
    private String status;

    @AssertTrue(message = "End date must be after or equal to start date")
    public boolean isValidDateRange() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }
}
