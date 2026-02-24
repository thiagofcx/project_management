package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException {
	
	private String errorCode = "INVALID_CREDENTIALS";
	
	public InvalidCredentialsException() {
        super("Invalid credentials");
    }
	
    public InvalidCredentialsException(String message) {
        super(message);
    }
}