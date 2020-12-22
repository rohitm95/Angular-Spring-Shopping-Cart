package com.persistent.covidinventory.customer.model;

import java.io.Serializable;

public class ItemDetails implements Serializable {
    private int id;
    private String itemNumber;
    private String group;
    private String itemName;
    private double price;
    private Integer stock;
    private String category;
    private boolean lowStockIndicator;
    private boolean toBeSoldOneItemPerOrder;
    private String volumePerItem;
    private String monthlyQuotaPerUser;
    private String itemType;
    private String yearlyQuotaPerUser;
    private int noOfItemsOrderded;
    private String store;

    public ItemDetails(){

    }

    public ItemDetails(int productId, String productNumber, String productGroup, String productName,double productCost, int productStock, String productCategory, boolean productLowStockIndicator, boolean productToBeSoldOneItemPerOrder, String productVolumePerItem, String monthlyQuotaPerUser, String productType, String productYearlyQuotaPerUser, int noOfItemsOrdered, String store){

        this.id = productId;
        this.itemNumber = productNumber;
        this.group = productGroup;
        this.itemName = productName;
        this.price = productCost;
        this.stock = productStock;
        this.category = productCategory;
        this.lowStockIndicator = productLowStockIndicator;
        this.toBeSoldOneItemPerOrder = productToBeSoldOneItemPerOrder;
        this.volumePerItem = productVolumePerItem;
        this.monthlyQuotaPerUser = monthlyQuotaPerUser;
        this.itemType = productType;
        this.yearlyQuotaPerUser = productYearlyQuotaPerUser;
        this.noOfItemsOrderded = noOfItemsOrdered;
        this.store = store;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public boolean isLowStockIndicator() {
        return lowStockIndicator;
    }

    public void setLowStockIndicator(boolean lowStockIndicator) {
        this.lowStockIndicator = lowStockIndicator;
    }

    public boolean getToBeSoldOneItemPerOrder() {
        return toBeSoldOneItemPerOrder;
    }

    public void setToBeSoldOneItemPerOrder(boolean toBeSoldOneItemPerOrder) {
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

    public int getNoOfItemsOrderded() {
        return noOfItemsOrderded;
    }

    public void setNoOfItemsOrderded(int noOfItemsOrderded) {
        this.noOfItemsOrderded = noOfItemsOrderded;
    }


    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
