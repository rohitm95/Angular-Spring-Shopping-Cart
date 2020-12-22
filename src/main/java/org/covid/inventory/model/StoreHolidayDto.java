package org.covid.inventory.model;

import java.sql.Date;

public class StoreHolidayDto {

	private Integer id;
	
    private Date date;

    private String holiday;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

	@Override
	public String toString() {
		return "StoreHolidayDto [date=" + date + ", holiday=" + holiday + "]";
	}
}
