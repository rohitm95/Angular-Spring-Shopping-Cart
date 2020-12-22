package org.covid.inventory.exceptions;

@SuppressWarnings("serial")
public class SessionTimeoutException extends BaseException {

	public SessionTimeoutException(Integer code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public SessionTimeoutException(Integer code, String message) {
		super(code, message);
	}


}
