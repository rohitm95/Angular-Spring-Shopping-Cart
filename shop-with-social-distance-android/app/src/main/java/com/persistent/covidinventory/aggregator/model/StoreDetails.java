package com.persistent.covidinventory.aggregator.model;

import java.io.Serializable;

public class StoreDetails implements Serializable {
    int storeId;
    String storeName;
    int slotDuration;
    int deliveryInStlot;
    String storeTiming;
    String breakTiming;
    String storeHolidays;
    boolean active ;

    public StoreDetails(){

    }

    public StoreDetails(int storeId, String storeName, int slotDuration, int deliveryInStlot, String storeTiming, String breakTiming, String storeHolidays, boolean active){
        this.storeId = storeId;
        this.storeName = storeName;
        this.slotDuration = slotDuration;
        this.deliveryInStlot = deliveryInStlot;
        this.storeTiming = storeTiming;
        this.breakTiming = breakTiming;
        this.storeHolidays = storeHolidays;
        this.active = active;

    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getSlotDuration() {
        return slotDuration;
    }

    public void setSlotDuration(int slotDuration) {
        this.slotDuration = slotDuration;
    }

    public int getDeliveryInStlot() {
        return deliveryInStlot;
    }

    public void setDeliveryInStlot(int deliveryInStlot) {
        this.deliveryInStlot = deliveryInStlot;
    }

    public String getStoreTiming() {
        return storeTiming;
    }

    public void setStoreTiming(String storeTiming) {
        this.storeTiming = storeTiming;
    }

    public String getBreakTiming() {
        return breakTiming;
    }

    public void setBreakTiming(String breakTiming) {
        this.breakTiming = breakTiming;
    }

    public String getStoreHolidays() {
        return storeHolidays;
    }

    public void setStoreHolidays(String storeHolidays) {
        this.storeHolidays = storeHolidays;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
