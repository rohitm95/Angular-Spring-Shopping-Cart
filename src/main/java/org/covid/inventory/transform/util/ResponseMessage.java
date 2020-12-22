package org.covid.inventory.transform.util;

public class ResponseMessage<T> {
	int status;
	String statusText;
	T result;
    String Token;
    int totalElements;
    
	public ResponseMessage(int status, String statusText, T result) {
		super();
		this.status = status;
		this.statusText = statusText;
		this.result = result;
	}

	public ResponseMessage(int status, String statusText) {
		super();
		this.status = status;
		this.statusText = statusText;
	}

	   
	public ResponseMessage(int status, String statusText, T result, String token) {
		super();
		this.status = status;
		this.statusText = statusText;
		this.result = result;
		Token = token;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public ResponseMessage() {
		super();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	@Override
	public String toString() {
		return "ResponseMessage [status=" + status + ", statusText=" + statusText + ", result=" + result + "]";
	}

}
