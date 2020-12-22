package org.covid.inventory.entity;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "break_timings")
public class BreakTimings {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "break_from")
    private String breakFrom;

    @Column(name = "break_to")
    private String breakTo;

    @Column(name = "break_type")
    private String breakType;

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

    public String getBreakFrom() {
        return breakFrom;
    }

    public void setBreakFrom(String breakFrom) {
        this.breakFrom = breakFrom;
    }

    public String getBreakTo() {
        return breakTo;
    }

    public void setBreakTo(String breakTo) {
        this.breakTo = breakTo;
    }

    public String getBreakType() {
        return breakType;
    }

    public void setBreakType(String breakType) {
        this.breakType = breakType;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

	@Override
	public String toString() {
		return "BreakTimings [id=" + id + ", breakFrom=" + breakFrom + ", breakTo=" + breakTo + ", breakType="
				+ breakType + ", store=" + store + "]";
	}
}
