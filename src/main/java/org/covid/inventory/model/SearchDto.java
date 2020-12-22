package org.covid.inventory.model;

import java.util.Date;

public class SearchDto {
	
	private String custName;
	private Integer orderId;
	private Date deliveryDate;
	private String timeSlotFrom;
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getTimeSlotFrom() {
		return timeSlotFrom;
	}
	public void setTimeSlotFrom(String timeSlotFrom) {
		this.timeSlotFrom = timeSlotFrom;
	}

}
