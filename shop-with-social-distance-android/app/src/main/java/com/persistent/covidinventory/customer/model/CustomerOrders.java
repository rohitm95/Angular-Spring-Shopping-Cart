package com.persistent.covidinventory.customer.model;

public class CustomerOrders {

    int orderNo ;
    long deliveryDate;
    String slotFrom;
    String slotTo;
    double amountPayable;
    String status;
    boolean active;
    //
    String itemName;
    int itemQuantity;

    public CustomerOrders(){

    }

    public CustomerOrders(int orderNo, long deliveryDate, String slotFrom, String slotTo, double amountPayable, String status, boolean active)
    {
        this.orderNo = orderNo;
        this.deliveryDate = deliveryDate;
        this.slotFrom = slotFrom;
        this.slotTo = slotTo;
        this.amountPayable = amountPayable;
        this.status = status;
        this.active = active;
    }


    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(long deliveryDate) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
}
