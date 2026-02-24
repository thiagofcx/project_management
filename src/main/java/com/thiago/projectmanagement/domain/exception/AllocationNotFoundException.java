package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class AllocationNotFoundException extends RuntimeException {

    private final String errorCode = "ALLOCATION_NOT_FOUND";

    public AllocationNotFoundException() {
        super("Allocation not found");
    }

    public AllocationNotFoundException(String message) {
        super(message);
    }
}
