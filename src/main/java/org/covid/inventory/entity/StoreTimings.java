package org.covid.inventory.entity;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "store_timings")
public class StoreTimings {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "day")
    private String day;

    @Column(name = "delivery_start_at")
    private String deliveryStartAt;

    @Column(name = "delivery_end_at")
    private String deliveryEndAt;

    @Column(name = "is_weakly_off")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean weaklyOff;

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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDeliveryStartAt() {
        return deliveryStartAt;
    }

    public void setDeliveryStartAt(String deliveryStartAt) {
        this.deliveryStartAt = deliveryStartAt;
    }

    public String getDeliveryEndAt() {
        return deliveryEndAt;
    }

    public void setDeliveryEndAt(String deliveryEndAt) {
        this.deliveryEndAt = deliveryEndAt;
    }

    public boolean isWeaklyOff() {
        return weaklyOff;
    }

    public void setWeaklyOff(boolean weaklyOff) {
        this.weaklyOff = weaklyOff;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

	@Override
	public String toString() {
		return "StoreTimings [id=" + id + ", day=" + day + ", deliveryStartAt=" + deliveryStartAt + ", deliveryEndAt="
				+ deliveryEndAt + ", weaklyOff=" + weaklyOff + ", store=" + store + "]";
	}
}
