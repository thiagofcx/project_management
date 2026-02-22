package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException {
	
	private String errorCode;
	
	public InvalidCredentialsException() {
        super("Invalid credentials");
        this.errorCode = "INVALID_CREDENTIALS";
    }
	
    public InvalidCredentialsException(String message) {
        super(message);
        this.errorCode = "INVALID_CREDENTIALS";
    }
}