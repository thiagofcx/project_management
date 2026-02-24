package com.thiago.projectmanagement.application.usecase.impl;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.dto.dashboard.AllocationsSummaryDTO;
import com.thiago.projectmanagement.application.dto.dashboard.DashboardSummaryDTO;
import com.thiago.projectmanagement.application.dto.dashboard.ProjectsByStatusDTO;
import com.thiago.projectmanagement.application.dto.dashboard.ProjectsSummaryDTO;
import com.thiago.projectmanagement.application.dto.dashboard.ResourcesSummaryDTO;
import com.thiago.projectmanagement.application.usecase.GetDashboardSummaryUseCase;
import com.thiago.projectmanagement.domain.model.ProjectStatus;
import com.thiago.projectmanagement.domain.repository.AllocationRepository;
import com.thiago.projectmanagement.domain.repository.ProjectRepository;
import com.thiago.projectmanagement.domain.repository.ResourceRepository;

@Component
public class GetDashboardSummaryUseCaseImpl implements GetDashboardSummaryUseCase {

    private final ProjectRepository projectRepository;
    private final ResourceRepository resourceRepository;
    private final AllocationRepository allocationRepository;

    public GetDashboardSummaryUseCaseImpl(
            ProjectRepository projectRepository,
            ResourceRepository resourceRepository,
            AllocationRepository allocationRepository) {
        this.projectRepository = projectRepository;
        this.resourceRepository = resourceRepository;
        this.allocationRepository = allocationRepository;
    }

    @Override
    public DashboardSummaryDTO execute() {
        long projectsTotal = projectRepository.count();
        ProjectsByStatusDTO byStatus = ProjectsByStatusDTO.builder()
                .planning(projectRepository.countByStatus(ProjectStatus.PLANNING))
                .inProgress(projectRepository.countByStatus(ProjectStatus.IN_PROGRESS))
                .onHold(projectRepository.countByStatus(ProjectStatus.ON_HOLD))
                .completed(projectRepository.countByStatus(ProjectStatus.COMPLETED))
                .build();
        ProjectsSummaryDTO projects = ProjectsSummaryDTO.builder()
                .total(projectsTotal)
                .byStatus(byStatus)
                .build();

        long resourcesTotal = resourceRepository.count();
        long allocated = allocationRepository.countDistinctResourceIdsWithProjectStatusNotCompleted();
        ResourcesSummaryDTO resources = ResourcesSummaryDTO.builder()
                .total(resourcesTotal)
                .allocated(allocated)
                .unallocated(Math.max(0, resourcesTotal - allocated))
                .build();

        long allocationsTotal = allocationRepository.count();
        AllocationsSummaryDTO allocations = AllocationsSummaryDTO.builder()
                .total(allocationsTotal)
                .build();

        return DashboardSummaryDTO.builder()
                .projects(projects)
                .resources(resources)
                .allocations(allocations)
                .build();
    }
}
