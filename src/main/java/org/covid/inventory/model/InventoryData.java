package org.covid.inventory.model;

import java.io.File;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

import org.covid.inventory.entity.Store;

/*
 * Item Number	
 * Group	Name	 Price 	Stock	Category	Low Stock Indicator	To be Sold one item per order (Yes/No)	Weight/ Volume per Item	Whether In Stock (Yes/No)	Monthly Quota per User per Item	ItemType (AFD/ Non AFD)	Yearly Quota Per User Per Item

 */

public class InventoryData {
	private Long id;
	private String itemNumber;
	private String group;
	private String itemName;
	private Double price;
	private Integer stock;
	private String category;
	private Boolean lowStockIndicator;
	private Boolean toBeSoldOneItemPerOrder;
	private String volumePerItem;
	private String monthlyQuotaPerUser;
	private String itemType;
	private String yearlyQuotaPerUser; 
	private Integer noOfItemsOrderded;
	private Store store;
	private String imageUrl;
	private Date itemEntryDate;
	private boolean newArrival;
//	private String imageFile;
//
//	public String getImageFile() {
//		return imageFile;
//	}
//
//	public void setImageFile(String imageFile) {
//		this.imageFile = imageFile;
//	}

	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getItemEntryDate() {
		return itemEntryDate;
	}

	public void setItemEntryDate(Date itemEntryDate) {
		this.itemEntryDate = itemEntryDate;
	}

	public boolean isNewArrival() {
		return newArrival;
	}

	public void setNewArrival(boolean newArrival) {
		this.newArrival = newArrival;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getLowStockIndicator() {
		return lowStockIndicator;
	}

	public void setLowStockIndicator(Boolean lowStockIndicator) {
		this.lowStockIndicator = lowStockIndicator;
	}

	public Boolean getToBeSoldOneItemPerOrder() {
		return toBeSoldOneItemPerOrder;
	}

	public void setToBeSoldOneItemPerOrder(Boolean toBeSoldOneItemPerOrder) {
		this.toBeSoldOneItemPerOrder = toBeSoldOneItemPerOrder;
	}

	public String getVolumePerItem() {
		return volumePerItem;
	}

	public void setVolumePerItem(String volumePerItem) {
		this.volumePerItem = volumePerItem;
	}

	public String getMonthlyQuotaPerUser() {
		return monthlyQuotaPerUser;
	}

	public void setMonthlyQuotaPerUser(String monthlyQuotaPerUser) {
		this.monthlyQuotaPerUser = monthlyQuotaPerUser;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getYearlyQuotaPerUser() {
		return yearlyQuotaPerUser;
	}

	public void setYearlyQuotaPerUser(String yearlyQuotaPerUser) {
		this.yearlyQuotaPerUser = yearlyQuotaPerUser;
	}
	
	public Integer getNoOfItemsOrderded() {
		return noOfItemsOrderded;
	}

	public void setNoOfItemsOrderded(Integer noOfItemsOrderded) {
		this.noOfItemsOrderded = noOfItemsOrderded;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	@Override
	public String toString() {
		return "InventoryData [id=" + id + ", itemNumber=" + itemNumber + ", group=" + group + ", itemName=" + itemName
				+ ", price=" + price + ", stock=" + stock + ", category=" + category + ", lowStockIndicator="
				+ lowStockIndicator + ", toBeSoldOneItemPerOrder=" + toBeSoldOneItemPerOrder + ", volumePerItem="
				+ volumePerItem + ", monthlyQuotaPerUser=" + monthlyQuotaPerUser + ", itemType=" + itemType
				+ ", yearlyQuotaPerUser=" + yearlyQuotaPerUser + ", noOfItemsOrderded=" + noOfItemsOrderded + ", store="
				+ store + ", imageUrl=" + imageUrl + ", itemEntryDate=" + itemEntryDate + ", newArrival=" + newArrival
				+ "]";
	}

	
}
