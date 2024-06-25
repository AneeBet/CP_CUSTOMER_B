package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UnauthorizedAccessException extends Exception{

	private static final long serialVersionUID = 1L;

	public UnauthorizedAccessException(String message){
    	super(message);
    }
}
