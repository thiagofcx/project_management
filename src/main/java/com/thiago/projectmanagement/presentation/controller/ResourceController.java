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

import com.thiago.projectmanagement.application.dto.PagedResultDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import com.thiago.projectmanagement.application.dto.ResourceInputDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;
import com.thiago.projectmanagement.application.usecase.CreateResourceUseCase;
import com.thiago.projectmanagement.application.usecase.DeleteResourceUseCase;
import com.thiago.projectmanagement.application.usecase.GetResourceByIdUseCase;
import com.thiago.projectmanagement.application.usecase.ListResourcesUseCase;
import com.thiago.projectmanagement.application.usecase.UpdateResourceUseCase;
import com.thiago.projectmanagement.presentation.dto.CreateResourceRequest;
import com.thiago.projectmanagement.presentation.dto.UpdateResourceRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/resources")
@Validated
public class ResourceController {

    private final CreateResourceUseCase createResourceUseCase;
    private final GetResourceByIdUseCase getResourceByIdUseCase;
    private final ListResourcesUseCase listResourcesUseCase;
    private final UpdateResourceUseCase updateResourceUseCase;
    private final DeleteResourceUseCase deleteResourceUseCase;

    public ResourceController(
            CreateResourceUseCase createResourceUseCase,
            GetResourceByIdUseCase getResourceByIdUseCase,
            ListResourcesUseCase listResourcesUseCase,
            UpdateResourceUseCase updateResourceUseCase,
            DeleteResourceUseCase deleteResourceUseCase) {
        this.createResourceUseCase = createResourceUseCase;
        this.getResourceByIdUseCase = getResourceByIdUseCase;
        this.listResourcesUseCase = listResourcesUseCase;
        this.updateResourceUseCase = updateResourceUseCase;
        this.deleteResourceUseCase = deleteResourceUseCase;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<ResourceOutputDTO> create(@Valid @RequestBody CreateResourceRequest request) {
        ResourceInputDTO input = new ResourceInputDTO(
                request.getName(),
                request.getEmail(),
                request.getSkills()
        );
        ResourceOutputDTO result = createResourceUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<PagedResultDTO<ResourceOutputDTO>> list(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "sort_order", defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String skills) {
        return ResponseEntity.ok(listResourcesUseCase.execute(page, size, sortBy, sortOrder, name, skills));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<ResourceOutputDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getResourceByIdUseCase.execute(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<ResourceOutputDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateResourceRequest request) {
        ResourceInputDTO input = new ResourceInputDTO(
                request.getName(),
                request.getEmail(),
                request.getSkills()
        );
        ResourceOutputDTO result = updateResourceUseCase.execute(id, input);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteResourceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
