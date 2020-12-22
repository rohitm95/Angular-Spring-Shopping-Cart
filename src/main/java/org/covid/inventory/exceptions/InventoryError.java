package org.covid.inventory.exceptions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InventoryError {

	private Date timestamp = Calendar.getInstance().getTime();
    private Integer status;
    private String error;
    private List<String> message;
    
    public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}
    
}
