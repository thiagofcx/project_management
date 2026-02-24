package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
	
	private String errorCode= "UNAUTHORIZED";
	
	public UnauthorizedException() {
        super("You do not have permission to access this resource");
    }
	
    public UnauthorizedException(String message) {
        super(message);
    }
}