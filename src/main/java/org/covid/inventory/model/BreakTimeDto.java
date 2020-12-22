package org.covid.inventory.model;

public class BreakTimeDto {
	
	private Integer id;

	private String breakFrom;

    private String breakTo;

    private String breakType;
    
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

	@Override
	public String toString() {
		return "BreakTimeDto [breakFrom=" + breakFrom + ", breakTo=" + breakTo + ", breakType=" + breakType + "]";
	}
}
