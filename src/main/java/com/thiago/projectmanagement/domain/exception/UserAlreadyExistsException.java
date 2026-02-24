package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
	
	private String errorCode = "USER_ALREADY_EXISTS";
	
	public UserAlreadyExistsException() {
        super("User already exists");
    }
	
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}