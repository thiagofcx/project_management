package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class ProjectNotFoundException extends RuntimeException {

    private final String errorCode = "PROJECT_NOT_FOUND";

    public ProjectNotFoundException() {
        super("Project not found");
    }

    public ProjectNotFoundException(String message) {
        super(message);
    }
}
