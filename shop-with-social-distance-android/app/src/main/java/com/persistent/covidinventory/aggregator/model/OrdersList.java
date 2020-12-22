package com.persistent.covidinventory.aggregator.model;

import com.persistent.covidinventory.customer.model.ItemDetails;
import com.persistent.covidinventory.customer.model.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;

public class OrdersList implements Serializable {

    int orderNo ;
    String deliveryDate;
    String slotFrom;
    String slotTo;
    double amountPayable;
    String status;

    //
    String itemName;
    int itemQuantity;

    UserDetails userDetails;
    StoreDetails storeDetails;
    ArrayList<ItemDetails> inventoryData;
    ItemDetails itemDetails;

    public OrdersList(){

    }

    public OrdersList(int orderNo, String deliveryDate, String slotFrom, String slotTo, double amountPayable, String status, String itemName, int itemQuantity, UserDetails userDetails, StoreDetails storeDetails ,ArrayList<ItemDetails> inventoryData ,ItemDetails itemDetails){

        this.orderNo = orderNo;
        this.deliveryDate = deliveryDate;
        this.slotTo = slotTo;
        this.slotFrom = slotFrom;
        this.amountPayable = amountPayable;
        this.status = status;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.userDetails = userDetails;
        this.storeDetails = storeDetails;
        this.inventoryData = inventoryData;
        this.itemDetails = itemDetails;

    }


    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
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

    public double getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(double amountPayable) {
        this.amountPayable = amountPayable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public StoreDetails getStoreDetails() {
        return storeDetails;
    }

    public void setStoreDetails(StoreDetails storeDetails) {
        this.storeDetails = storeDetails;
    }

    public ArrayList<ItemDetails> getInventoryData() {
        return inventoryData;
    }

    public void setInventoryData(ArrayList<ItemDetails> inventoryData) {
        this.inventoryData = inventoryData;
    }

    public ItemDetails getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ItemDetails itemDetails) {
        this.itemDetails = itemDetails;
    }
}
