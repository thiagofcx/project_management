package com.thiago.projectmanagement.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import com.thiago.projectmanagement.application.dto.AllocationInputDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import com.thiago.projectmanagement.application.dto.AllocationOutputDTO;
import com.thiago.projectmanagement.application.dto.PagedResultDTO;
import com.thiago.projectmanagement.application.usecase.CreateAllocationUseCase;
import com.thiago.projectmanagement.application.usecase.DeleteAllocationUseCase;
import com.thiago.projectmanagement.application.usecase.GetAllocationByIdUseCase;
import com.thiago.projectmanagement.application.usecase.ListAllocationsUseCase;
import com.thiago.projectmanagement.application.usecase.UpdateAllocationUseCase;
import com.thiago.projectmanagement.presentation.dto.CreateAllocationRequest;
import com.thiago.projectmanagement.presentation.dto.UpdateAllocationRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/allocations")
@Validated
public class AllocationController {

    private final CreateAllocationUseCase createAllocationUseCase;
    private final GetAllocationByIdUseCase getAllocationByIdUseCase;
    private final ListAllocationsUseCase listAllocationsUseCase;
    private final UpdateAllocationUseCase updateAllocationUseCase;
    private final DeleteAllocationUseCase deleteAllocationUseCase;

    public AllocationController(
            CreateAllocationUseCase createAllocationUseCase,
            GetAllocationByIdUseCase getAllocationByIdUseCase,
            ListAllocationsUseCase listAllocationsUseCase,
            UpdateAllocationUseCase updateAllocationUseCase,
            DeleteAllocationUseCase deleteAllocationUseCase) {
        this.createAllocationUseCase = createAllocationUseCase;
        this.getAllocationByIdUseCase = getAllocationByIdUseCase;
        this.listAllocationsUseCase = listAllocationsUseCase;
        this.updateAllocationUseCase = updateAllocationUseCase;
        this.deleteAllocationUseCase = deleteAllocationUseCase;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<AllocationOutputDTO> create(@Valid @RequestBody CreateAllocationRequest request) {
        AllocationInputDTO input = AllocationInputDTO.builder()
                .projectId(request.getProjectId())
                .resourceId(request.getResourceId())
                .build();
        AllocationOutputDTO result = createAllocationUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<PagedResultDTO<AllocationOutputDTO>> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "sort_order", defaultValue = "ASC") String sortOrder,
            @RequestParam(value = "resource_name", required = false) String resourceName,
            @RequestParam(value = "project_name", required = false) String projectName) {
        return ResponseEntity.ok(listAllocationsUseCase.execute(page, size, sortBy, sortOrder, resourceName, projectName));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<AllocationOutputDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getAllocationByIdUseCase.execute(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<AllocationOutputDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAllocationRequest request) {
        AllocationInputDTO input = AllocationInputDTO.builder()
                .projectId(request.getProjectId())
                .resourceId(request.getResourceId())
                .build();
        AllocationOutputDTO result = updateAllocationUseCase.execute(id, input);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteAllocationUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
