package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
	
	private String errorCode;
	
	public UserAlreadyExistsException() {
        super("User already exists");
        this.errorCode = "USER_ALREADY_EXISTS";
    }
	
    public UserAlreadyExistsException(String message) {
        super(message);
        this.errorCode = "USER_ALREADY_EXISTS";
    }
}