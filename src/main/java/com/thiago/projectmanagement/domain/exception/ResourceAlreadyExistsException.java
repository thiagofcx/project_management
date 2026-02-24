package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {

    private final String errorCode = "RESOURCE_ALREADY_EXISTS";

    public ResourceAlreadyExistsException() {
        super("Resource with this email already exists");
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
