package org.covid.inventory.exceptions;

@SuppressWarnings("serial")
public class EntityNotFoundException extends BaseException {

	public EntityNotFoundException(Integer code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public EntityNotFoundException(Integer code, String message) {
		super(code, message);
	}


}
