package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class InvalidProjectDatesException extends RuntimeException {

    private final String errorCode = "INVALID_PROJECT_DATES";

    public InvalidProjectDatesException() {
        super("End date must be after or equal to start date");
    }

    public InvalidProjectDatesException(String message) {
        super(message);
    }
}
