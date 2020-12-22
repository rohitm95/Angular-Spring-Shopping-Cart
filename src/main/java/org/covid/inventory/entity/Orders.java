package org.covid.inventory.entity;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;

@Entity
@Table(name="orders")
public class Orders  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer orderNo;
	
	@ManyToOne
	private Store store;
	
	@ManyToOne
	private User customer;

	@Temporal(TIMESTAMP)
	private Date deliveryDate;
	
	@NonNull
	private String slotFrom;
	
	@NonNull
	private String slotTo;
	
	@ManyToOne
	private User aggregator;
	
	@NonNull
	@Column(name="amount_payable",precision=2)
	private Double amountPayable;
	
	private String status;
	
	@Column(nullable = false, name = "is_active")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isActive;
	
	private String cancelBy;
	
	private String cancelRemark;

	/**
	 * @return the id
	 */
	public Integer getOrderNo() {
		return orderNo;
	}

	/**
	 * @param id the id to set
	 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the store
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * @param store the store to set
	 */
	public void setStore(Store store) {
		this.store = store;
	}


	/**
	 * @return the deliveryDate
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public Double getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(Double amountPayable) {
		this.amountPayable = amountPayable;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}
	
	public String getCancelBy() {
		return cancelBy;
	}

	public void setCancelBy(String cancelBy) {
		this.cancelBy = cancelBy;
	}

	public String getCancelRemark() {
		return cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}
	
	public User getAggregator() {
		return aggregator;
	}
	public void setAggregator(User aggregator) {
		this.aggregator = aggregator;
	}
	
	
	
}
