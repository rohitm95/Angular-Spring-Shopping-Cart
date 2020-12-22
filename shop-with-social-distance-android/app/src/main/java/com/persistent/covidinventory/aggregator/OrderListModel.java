package com.persistent.covidinventory.aggregator;

import com.google.gson.annotations.SerializedName;

public class OrderListModel {
    @SerializedName("order_no")
    public int orderNo;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("store")
    private Store store;
    @SerializedName("deliveryDate")
    public String deliveryDate;
    @SerializedName("slotFrom")
    public String slotFrom;
    @SerializedName("slotTo")
    public String slotTo;
    @SerializedName("amountPayable")
    public double amountPayable;
    @SerializedName("status")
    public String status;
    @SerializedName("inventoryDatas")
    public Inventory[] inventoryDatas;
    @SerializedName("cancelBy")
    public boolean cancelBy;
    @SerializedName("cancelRemark")
    public boolean cancelRemark;


    public Inventory[] getInventoryDatas() {
        return inventoryDatas;
    }

    public void setInventoryDatas(Inventory[] inventoryDatas) {
        this.inventoryDatas = inventoryDatas;
    }

    public boolean isCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(boolean cancelBy) {
        this.cancelBy = cancelBy;
    }

    public boolean isCancelRemark() {
        return cancelRemark;
    }

    public void setCancelRemark(boolean cancelRemark) {
        this.cancelRemark = cancelRemark;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return orderNo + " " + status + " ";
    }

    public static class Store {
        @SerializedName("id")
        private int id;
        @SerializedName("storeName")
        private String storeName = null;
        @SerializedName("slotDuration")
        private int slotDuration;
        @SerializedName("deliveryInSlot")
        private int deliveryInSlot;
        @SerializedName("active")
        private Boolean active;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSlotDuration() {
            return slotDuration;
        }

        public void setSlotDuration(int slotDuration) {
            this.slotDuration = slotDuration;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public int getDeliveryInSlot() {
            return deliveryInSlot;
        }

        public void setDeliveryInSlot(int deliveryInSlot) {
            this.deliveryInSlot = deliveryInSlot;
        }

        @Override
        public String toString() {
            return "{" +
                    "id:" + id +
                    ", storeName:'" + storeName + '\'' +
                    ", slotDuration:" + slotDuration +
                    ", deliveryInSlot:" + deliveryInSlot +
                    ", active:" + active +
                    '}';
        }
    }

    public static class Customer {
        @SerializedName("id")
        private int id;
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("emailId")
        private String emailId;
        @SerializedName("mobileNumber")
        private String mobileNumber;
        @SerializedName("gender")
        private String gender;
        @SerializedName("isActive")
        private Boolean isActive;
        @SerializedName("dateOfJoin")
        private String dateOfJoin;
        @SerializedName("username")
        private String username;
        @SerializedName("password")
        private String password;
        @SerializedName("role")
        private Role role;
        @SerializedName("newUser")
        private Boolean newUser;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public String getDateOfJoin() {
            return dateOfJoin;
        }

        public void setDateOfJoin(String dateOfJoin) {
            this.dateOfJoin = dateOfJoin;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public Boolean getNewUser() {
            return newUser;
        }

        public void setNewUser(Boolean newUser) {
            this.newUser = newUser;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        @Override
        public String toString() {
            return "{" +
                    "id:" + id +
                    ", firstName:'" + firstName + '\'' +
                    ", lastName:'" + lastName + '\'' +
                    ", emailId:'" + emailId + '\'' +
                    ", mobileNumber:'" + mobileNumber + '\'' +
                    ", gender:'" + gender + '\'' +
                    ", isActive:" + isActive +
                    ", dateOfJoin:'" + dateOfJoin + '\'' +
                    ", username:'" + username + '\'' +
                    ", password:'" + password + '\'' +
                    ", role:" + role +
                    ", newUser:" + newUser +
                    '}';
        }
    }

    public static class Role {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "{" +
                    "id:" + id +
                    ", name:'" + name + '\'' +
                    '}';
        }
    }

    public static class Inventory {
        @SerializedName("id")
        private int id;
        @SerializedName("itemNumber")
        private String itemNumber;
        @SerializedName("group")
        private String group;
        @SerializedName("itemName")
        private String itemName;
        @SerializedName("price")
        private double price;
        @SerializedName("stock")
        private int stock;
        @SerializedName("category")
        private String category = null;
        @SerializedName("lowStockIndicator")
        private Boolean lowStockIndicator;
        @SerializedName("toBeSoldOneItemPerOrder")
        private String toBeSoldOneItemPerOrder = null;
        @SerializedName("volumePerItem")
        private String volumePerItem = null;
        @SerializedName("monthlyQuotaPerUser")
        private String monthlyQuotaPerUser;
        @SerializedName("itemType")
        private String itemType = null;
        @SerializedName("yearlyQuotaPerUser")
        private String yearlyQuotaPerUser = null;
        @SerializedName("noOfItemsOrderded")
        private int noOfItemsOrderded;
/*        private int updatedOrderItem;

        public int getUpdatedOrderItem() {
            return updatedOrderItem;
        }

        public void setUpdatedOrderItem(int updatedOrderItem) {
            this.updatedOrderItem = updatedOrderItem;
        }*/

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

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
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

        public String getToBeSoldOneItemPerOrder() {
            return toBeSoldOneItemPerOrder;
        }

        public void setToBeSoldOneItemPerOrder(String toBeSoldOneItemPerOrder) {
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

        @Override
        public String toString() {
            return "{" +
                    "id:" + id +
                    ", itemNumber:'" + itemNumber + '\'' +
                    ", group:'" + group + '\'' +
                    ", itemName:'" + itemName + '\'' +
                    ", price:" + price +
                    ", stock:" + stock +
                    ", category:'" + category + '\'' +
                    ", lowStockIndicator:" + lowStockIndicator +
                    ", toBeSoldOneItemPerOrder:'" + toBeSoldOneItemPerOrder + '\'' +
                    ", volumePerItem:'" + volumePerItem + '\'' +
                    ", monthlyQuotaPerUser:'" + monthlyQuotaPerUser + '\'' +
                    ", itemType:'" + itemType + '\'' +
                    ", yearlyQuotaPerUser:'" + yearlyQuotaPerUser + '\'' +
                    ", noOfItemsOrderded:" + noOfItemsOrderded +
                    '}';
        }
    }
}