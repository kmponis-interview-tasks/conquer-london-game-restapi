package com.location.game.restapi.exception;

public class ConsumeRestapiException extends RuntimeException {
	private static final long serialVersionUID = 2198138535345798157L;

	public ConsumeRestapiException(String errorMessage) {
		super(errorMessage);
	}
}
