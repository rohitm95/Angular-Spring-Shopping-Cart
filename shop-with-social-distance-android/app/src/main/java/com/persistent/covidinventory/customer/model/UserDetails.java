package com.persistent.covidinventory.customer.model;

import java.io.Serializable;

public class UserDetails implements Serializable {

    String dateOfJoin;
    String emailId;
    String firstName;
    String gender;
    int id;
    boolean isActive;
    String lastName;
    String mobileNumber;
    boolean newUser;
    String password;
    String username;
    Role role;

    String store;
    String category;
    String afdPurchaseLimit;
    String nonAFDPurchaseLimit;


    public UserDetails(){

    }

    public UserDetails( String dateOfJoin,String emailId,String firstName,String gender,int id,boolean isActive,String lastName,String mobileNumber,boolean newUser,String password,String username,Role role , String store ,String category, String afdPurchaseLimit, String nonAFDPurchaseLimit){
        this.dateOfJoin = dateOfJoin;
        this.emailId= emailId ;
        this.firstName = firstName;
        this.gender = gender;
        this.id = id;
        this.isActive =isActive;
        this.lastName= lastName ;
        this.mobileNumber = mobileNumber;
        this.newUser = newUser;
        this.password = password;
        this.username = username;
        this.role = role;
        this.store = store;
        this.category = category;
        this.afdPurchaseLimit = afdPurchaseLimit;
        this.nonAFDPurchaseLimit = nonAFDPurchaseLimit;

    }


    public String getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(String dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAfdPurchaseLimit() {
        return afdPurchaseLimit;
    }

    public void setAfdPurchaseLimit(String afdPurchaseLimit) {
        this.afdPurchaseLimit = afdPurchaseLimit;
    }

    public String getNonAFDPurchaseLimit() {
        return nonAFDPurchaseLimit;
    }

    public void setNonAFDPurchaseLimit(String nonAFDPurchaseLimit) {
        this.nonAFDPurchaseLimit = nonAFDPurchaseLimit;
    }
}


