package org.covid.inventory.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class Inventory 	 {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "item_number")
	private String itemNumber;

	@Column(name = "group_type")
	private String group;
	
	@Column(name = "item_name")
	private String itemName;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "stock")
	private Integer stock;
	
	/*@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(foreignKey = @ForeignKey(name="fk_invertory_item_category"))
	private ItemCategory itemCategory;*/
	
	@Column(name = "item_category")
	private String itemCategory;
	
	@Column(name = "low_stock_indicator")
	private int lowStockIndicator; 
	
	@Column(nullable = false, name = "is_one_per_item")
	private boolean isOnePerItem;
	
	@Column(nullable = false, name = "item_type")
	private String itemType;
	
	@Column(nullable = false, name = "is_active")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isActive;
	
	@Column(name = "monthly_quota_per_user")
	private String monthlyQuotaPerUser;
	
	@Column(name = "yearly_quota_per_user")
	private String yearlyQuotaPerUser; 
	
	@Column(name = "weight_volume_per_item")
	private String weightVolumePerItem; 

	@Column(name = "image_url")
	private String imageUrl; 
	
	@Column(name = "item_entry_date")
	private Date itemEntryDate; 
	
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

	//added store id as foreign key
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_inv_store_id"))
	private Store store;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @return the stock
	 */
	public Integer getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/**
	 * @return the itemCategory
	 */
	/*public ItemCategory getItemCategory() {
		return itemCategory;
	}

	*//**
	 * @param itemCategory the itemCategory to set
	 *//*
	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}*/

	/**
	 * @return the lowStockIndicator
	 */
	public int getLowStockIndicator() {
		return lowStockIndicator;
	}

	/**
	 * @param lowStockIndicator the lowStockIndicator to set
	 */
	public void setLowStockIndicator(int lowStockIndicator) {
		this.lowStockIndicator = lowStockIndicator;
	}

	/**
	 * @return the isOnePerItem
	 */
	public boolean isOnePerItem() {
		return isOnePerItem;
	}

	/**
	 * @param isOnePerItem the isOnePerItem to set
	 */
	public void setOnePerItem(boolean isOnePerItem) {
		this.isOnePerItem = isOnePerItem;
	}

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
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
	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	
	public String getWeightVolumePerItem() {
		return weightVolumePerItem;
	}

	public void setWeightVolumePerItem(String weightVolumePerItem) {
		this.weightVolumePerItem = weightVolumePerItem;
	}

	public String getMonthlyQuotaPerUser() {
		return monthlyQuotaPerUser;
	}

	public void setMonthlyQuotaPerUser(String monthlyQuotaPerUser) {
		this.monthlyQuotaPerUser = monthlyQuotaPerUser;
	}

	public String getYearlyQuotaPerUser() {
		return yearlyQuotaPerUser;
	}

	public void setYearlyQuotaPerUser(String yearlyQuotaPerUser) {
		this.yearlyQuotaPerUser = yearlyQuotaPerUser;
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
}
