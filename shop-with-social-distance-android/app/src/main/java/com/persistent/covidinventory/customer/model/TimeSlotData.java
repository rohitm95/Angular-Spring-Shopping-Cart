package com.persistent.covidinventory.customer.model;

import org.json.JSONArray;

public class TimeSlotData {
    private String timeSlotDate;
    JSONArray timeSlotTimes;

    public TimeSlotData() {

    }

    public TimeSlotData(String timeSlotDate , JSONArray timeSlotTimes){
        this.timeSlotDate = timeSlotDate;
        this.timeSlotTimes = timeSlotTimes;

    }

    public String getTimeSlotDate() {
        return timeSlotDate;
    }

    public void setTimeSlotDate(String timeSlotDate) {
        this.timeSlotDate = timeSlotDate;
    }

    public JSONArray getTimeSlotTimes() {
        return timeSlotTimes;
    }

    public void setTimeSlotTimes(JSONArray timeSlotTimes) {
        this.timeSlotTimes = timeSlotTimes;
    }
}
