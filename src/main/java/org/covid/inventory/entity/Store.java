package org.covid.inventory.entity;

import javax.persistence.*;

import org.covid.inventory.audit.Auditable;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Store extends Auditable<String> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "store_name")
	private String storeName;
	
	@Column(name = "slot_duration")
	private int slotDuration;
	
	@Column(name = "delivery_in_slot")
	private int deliveryInSlot;
	
	@Column(nullable = false, name = "is_active")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isActive;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<StoreTimings> storeTimings;

	@OneToMany(mappedBy = "store",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<BreakTimings> breakTimings;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<StoreHoliday> storeHolidays;

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
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Set<StoreTimings> getStoreTimings() {
		return storeTimings;
	}

	public void setStoreTimings(Set<StoreTimings> storeTimings) {
		this.storeTimings = storeTimings;
	}

	public Set<BreakTimings> getBreakTimings() {
		return breakTimings;
	}

	public void setBreakTimings(Set<BreakTimings> breakTimings) {
		this.breakTimings = breakTimings;
	}

	public Set<StoreHoliday> getStoreHolidays() {
		return storeHolidays;
	}

	public void setStoreHolidays(Set<StoreHoliday> storeHolidays) {
		this.storeHolidays = storeHolidays;
	}

	@Override
	public String toString() {
		return "Store [id=" + id + ", storeName=" + storeName + ", slotDuration=" + slotDuration + ", deliveryInSlot="
				+ deliveryInSlot + ", isActive=" + isActive + ", storeTimings=" + storeTimings + ", breakTimings="
				+ breakTimings + ", storeHolidays=" + storeHolidays + "]";
	}
}
