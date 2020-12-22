package org.covid.inventory.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.sql.Date;

@Entity
@Table(name = "store_holiday")
public class StoreHoliday {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date")
    private Date date;

    @Column(name = "holiday")
    private String holiday;

    @ManyToOne
    @JoinColumn(name="store_id", nullable=false)
    @JsonBackReference
    private Store store;

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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

	@Override
	public String toString() {
		return "StoreHoliday [id=" + id + ", date=" + date + ", holiday=" + holiday + ", store=" + store + "]";
	}
}
