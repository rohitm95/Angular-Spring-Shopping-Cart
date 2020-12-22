package com.persistent.covidinventory.customer.model;

public class CustomerOrderDetails {
    int orderId ;
    double orderTotalAmount;
    String itemName;
    int orderNo;
    double itemPrice;
    int itemQuantity;

    public CustomerOrderDetails(){

    }
    public CustomerOrderDetails(int orderId, double orderTotalAmount, String itemName, int orderNo, double itemPrice, int itemQuantity){
        this.orderId = orderId;
        this.orderTotalAmount = orderTotalAmount;
        this.itemName = itemName;
        this.orderNo = orderNo;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
