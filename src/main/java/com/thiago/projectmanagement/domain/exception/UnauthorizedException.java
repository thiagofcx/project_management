package com.thiago.projectmanagement.domain.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
	
	private String errorCode;
	
	public UnauthorizedException() {
        super("You do not have permission to access this resource");
        this.errorCode = "UNAUTHORIZED";
    }
	
    public UnauthorizedException(String message) {
        super(message);
        this.errorCode = "UNAUTHORIZED";
    }
}