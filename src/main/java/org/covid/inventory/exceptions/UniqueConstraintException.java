package org.covid.inventory.exceptions;

@SuppressWarnings("serial")
public class UniqueConstraintException extends BaseException {

	public UniqueConstraintException(Integer code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public UniqueConstraintException(Integer code, String message) {
		super(code, message);
	}


}
