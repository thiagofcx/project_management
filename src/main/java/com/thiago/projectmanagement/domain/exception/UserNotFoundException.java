package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
	
	private String errorCode = "USER_NOT_FOUND";
	
	public UserNotFoundException() {
        super("User already exists");
    }
	
    public UserNotFoundException(String message) {
        super(message);
    }
}