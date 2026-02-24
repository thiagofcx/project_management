package com.thiago.projectmanagement.application.usecase.impl;

import org.springframework.stereotype.Component;

import com.thiago.projectmanagement.application.usecase.DeleteAllocationUseCase;
import com.thiago.projectmanagement.domain.exception.AllocationNotFoundException;
import com.thiago.projectmanagement.domain.repository.AllocationRepository;

@Component
public class DeleteAllocationUseCaseImpl implements DeleteAllocationUseCase {

    private final AllocationRepository allocationRepository;

    public DeleteAllocationUseCaseImpl(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    @Override
    public void execute(Long id) {
        if (allocationRepository.findById(id).isEmpty()) {
            throw new AllocationNotFoundException("Allocation not found");
        }
        allocationRepository.deleteById(id);
    }
}
