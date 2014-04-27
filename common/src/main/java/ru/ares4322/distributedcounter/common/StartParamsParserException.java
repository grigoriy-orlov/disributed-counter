package ru.ares4322.distributedcounter.common;

public class StartParamsParserException extends Exception {

	public StartParamsParserException() {
	}

	public StartParamsParserException(String message) {
		super(message);
	}

	public StartParamsParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public StartParamsParserException(Throwable cause) {
		super(cause);
	}
}
