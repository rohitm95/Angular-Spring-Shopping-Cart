package org.covid.inventory.exceptions;

@SuppressWarnings("serial")
public class BadRequestException extends BaseException {
	Object[] args;

	public BadRequestException(Integer code, String message, Object[] args) {
		super(code, message);
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

}
