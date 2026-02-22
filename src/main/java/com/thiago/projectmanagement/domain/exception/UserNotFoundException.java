package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
	
	private String errorCode;
	
	public UserNotFoundException() {
        super("User already exists");
        this.errorCode = "USER_NOT_FOUND";
    }
	
    public UserNotFoundException(String message) {
        super(message);
        this.errorCode = "USER_NOT_FOUND";
    }
}