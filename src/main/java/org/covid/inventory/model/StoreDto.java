package org.covid.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreDto {

	private Integer id;
	
	private String storeName;
	
	private int slotDuration;
	
	private int deliveryInSlot;

	private Set<StoreTimeDto> storeTimings;

	private Set<BreakTimeDto> breakTimings;

	private Set<StoreHolidayDto> storeHolidays;
	
	private boolean isActive;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the storeName
	 */
	public String getStoreName() {
		return storeName;
	}

	/**
	 * @param storeName the storeName to set
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/**
	 * @return the slotDuration
	 */
	public int getSlotDuration() {
		return slotDuration;
	}

	/**
	 * @param slotDuration the slotDuration to set
	 */
	public void setSlotDuration(int slotDuration) {
		this.slotDuration = slotDuration;
	}

	/**
	 * @return the deliveryInSlot
	 */
	public int getDeliveryInSlot() {
		return deliveryInSlot;
	}

	/**
	 * @param deliveryInSlot the deliveryInSlot to set
	 */
	public void setDeliveryInSlot(int deliveryInSlot) {
		this.deliveryInSlot = deliveryInSlot;
	}

	/**
	 * @return the isActive
	 */

	public Set<StoreTimeDto> getStoreTimings() {
		return storeTimings;
	}

	public void setStoreTimings(Set<StoreTimeDto> storeTimings) {
		this.storeTimings = storeTimings;
	}

	public Set<BreakTimeDto> getBreakTimings() {
		return breakTimings;
	}

	public void setBreakTimings(Set<BreakTimeDto> breakTimings) {
		this.breakTimings = breakTimings;
	}

	public Set<StoreHolidayDto> getStoreHolidays() {
		return storeHolidays;
	}

	public void setStoreHolidays(Set<StoreHolidayDto> storeHolidays) {
		this.storeHolidays = storeHolidays;
	}
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "StoreDto [id=" + id + ", storeName=" + storeName + ", slotDuration=" + slotDuration
				+ ", deliveryInSlot=" + deliveryInSlot + ", storeTimings=" + storeTimings + ", breakTimings="
				+ breakTimings + ", storeHolidays=" + storeHolidays + ", isActive=" + isActive + "]";
	}	
}
