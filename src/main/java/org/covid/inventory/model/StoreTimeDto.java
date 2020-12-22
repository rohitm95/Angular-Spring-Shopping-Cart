package org.covid.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreTimeDto {

	private Integer id;
	
    private String day;

    private String deliveryStartAt;

    private String deliveryEndAt;

    private boolean weaklyOff;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDeliveryStartAt() {
        return deliveryStartAt;
    }

    public void setDeliveryStartAt(String deliveryStartAt) {
        this.deliveryStartAt = deliveryStartAt;
    }

    public String getDeliveryEndAt() {
        return deliveryEndAt;
    }

    public void setDeliveryEndAt(String deliveryEndAt) {
        this.deliveryEndAt = deliveryEndAt;
    }

    public boolean isWeaklyOff() {
        return weaklyOff;
    }

    public void setWeaklyOff(boolean weaklyOff) {
        this.weaklyOff = weaklyOff;
    }

	@Override
	public String toString() {
		return "StoreTimeDto [day=" + day + ", deliveryStartAt=" + deliveryStartAt + ", deliveryEndAt=" + deliveryEndAt
				+ ", weaklyOff=" + weaklyOff + "]";
	}
}
