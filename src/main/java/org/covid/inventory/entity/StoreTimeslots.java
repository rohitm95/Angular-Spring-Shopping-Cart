package org.covid.inventory.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "store_timeslots")
public class StoreTimeslots {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private int storeId;
	
	private int customerId;
	
	private Date slotDate;

	private String slotFrom;
	
	private String slotTo;
	
	private Date insertedOn;
	
	private Date updatedOn;
	
	private char isDeleted;

	private int availableCount;

	public StoreTimeslots() {
		
	}
	
	public StoreTimeslots(Integer id, int storeId, Date slotDate, String slotFrom, String slotTo, Date insterdOn,
			Date updatedOn, char isDeleted, int availableCount, int customerId) {
		super();
		this.id = id;
		this.storeId = storeId;
		this.slotDate = slotDate;
		this.slotFrom = slotFrom;
		this.slotTo = slotTo;
		this.setInsertedOn(insterdOn);
		this.updatedOn = updatedOn;
		this.isDeleted = isDeleted;
		this.availableCount = availableCount;
		this.customerId = customerId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public Date getSlotDate() {
		return slotDate;
	}

	public void setSlotDate(Date slotDate) {
		this.slotDate = slotDate;
	}

	public String getSlotFrom() {
		return slotFrom;
	}

	public void setSlotFrom(String slotFrom) {
		this.slotFrom = slotFrom;
	}

	public String getSlotTo() {
		return slotTo;
	}

	public void setSlotTo(String slotTo) {
		this.slotTo = slotTo;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public Date getInsertedOn() {
		return insertedOn;
	}

	public void setInsertedOn(Date insertedOn) {
		this.insertedOn = insertedOn;
	}

	
}