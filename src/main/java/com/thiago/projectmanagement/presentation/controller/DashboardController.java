package com.thiago.projectmanagement.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thiago.projectmanagement.application.dto.dashboard.DashboardSummaryDTO;
import com.thiago.projectmanagement.application.usecase.GetDashboardSummaryUseCase;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final GetDashboardSummaryUseCase getDashboardSummaryUseCase;

    public DashboardController(GetDashboardSummaryUseCase getDashboardSummaryUseCase) {
        this.getDashboardSummaryUseCase = getDashboardSummaryUseCase;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> summary() {
        DashboardSummaryDTO result = getDashboardSummaryUseCase.execute();
        return ResponseEntity.ok(result);
    }
}
