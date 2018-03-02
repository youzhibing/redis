package com.lee.redis.exception;

public class LocalException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public LocalException(String message) {
        super(message);
    }
}
