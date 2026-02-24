package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class ResourceAlreadyAllocatedException extends RuntimeException {

    private final String errorCode = "RESOURCE_ALREADY_ALLOCATED";

    public ResourceAlreadyAllocatedException() {
        super("Resource is already allocated to this project");
    }

    public ResourceAlreadyAllocatedException(String message) {
        super(message);
    }
}
