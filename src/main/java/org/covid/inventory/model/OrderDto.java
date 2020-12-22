package org.covid.inventory.model;

import java.util.Date;
import java.util.List;

import org.springframework.lang.NonNull;

public class OrderDto {
	
	private Integer order_no;
	
	@NonNull
	private UserDto customer;
	
	@NonNull
	private StoreDto store;
	
	@NonNull
	private Date deliveryDate;
	
	@NonNull
	private String slotFrom;
	
	@NonNull
	private String slotTo;
	
	@NonNull
	private Double amountPayable;
	
	private String status;
	
	private List<InventoryData> inventoryDatas;
	
	private String cancelBy;
	
	private String cancelRemark;
	
	private boolean isOrderChanged;
	
	private UserDto aggregator;

	public UserDto getCustomer() {
		return customer;
	}

	public void setCustomer(UserDto customer) {
		this.customer = customer;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

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

	public void setOrderChanged(boolean isOrderChanged) {
		this.isOrderChanged = isOrderChanged;
	}

	public List<InventoryData> getInventoryDatas() {
		return inventoryDatas;
	}

	public void setInventoryDatas(List<InventoryData> inventoryDatas) {
		this.inventoryDatas = inventoryDatas;
	}
	
	public StoreDto getStore() {
		return store;
	}

	public void setStore(StoreDto store) {
		this.store = store;
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

	public Integer getOrder_no() {
		return order_no;
	}

	public void setOrder_no(Integer order_no) {
		this.order_no = order_no;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getIsOrderChanged() {
		return isOrderChanged;
	}

	public void setIsOrderChanged(boolean isOrderChanged) {
		this.isOrderChanged = isOrderChanged;
	}

	public UserDto getAggregator() {
		return aggregator;
	}

	public void setAggregator(UserDto aggregator) {
		this.aggregator = aggregator;
	}
	
}
