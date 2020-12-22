package com.persistent.covidinventory.customer.model;


import java.io.Serializable;
import java.util.ArrayList;

public class CartDetails implements Serializable {

    UserDetails customer;
    ArrayList<ItemDetails> inventoryDatas ;
    ArrayList<ItemDetails> categoryList;
    public ArrayList<String> categoriesSelected;
    public Boolean[] arraySelected;

    public CartDetails(){
        customer = new UserDetails();
        inventoryDatas = new ArrayList<>();
        categoryList = new ArrayList<>();
        arraySelected = new Boolean[6];
        categoriesSelected = new ArrayList<>();

    }
//
//    public ArrayList<UserDetails> getCustomer() {
//        return customer1;
//    }
//
//    public void setCustomer(ArrayList<UserDetails> customer) {
//        this.customer1 = customer;
//    }

    public ArrayList<ItemDetails> getCartItems() {
        return inventoryDatas;
    }

    public void setCartItems(ArrayList<ItemDetails> cartItems) {
        this.inventoryDatas = cartItems;
    }

    public ArrayList<ItemDetails> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<ItemDetails> categoryList) {
        this.categoryList = categoryList;
    }

    public UserDetails getCustomer() {
        return customer;
    }

    public void setCustomer(UserDetails customer) {
        this.customer = customer;
    }

//    public ItemDetails getItemDetails() {
//        return itemDetails;
//    }
//
//    public void setItemDetails(ItemDetails itemDetails) {
//        this.itemDetails = itemDetails;
//    }



    //
//    public String getCostumer() {
//        return "";
//    }
//
//    public void setCostumer(String costumer) {
//        this.costumer = costumer;
//    }


    public Boolean[] getArraySelected() {
        return arraySelected;
    }

    public void setArraySelected(Boolean[] arraySelected) {
        this.arraySelected = arraySelected;
    }
}
