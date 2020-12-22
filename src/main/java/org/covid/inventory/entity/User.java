package org.covid.inventory.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

/**
 * 
 * @author mayank_gupta
 *
 */
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_id", unique=true)
	private String emailId;
	
	@Column(name = "mobile_number", unique=true)
	private String mobileNumber;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(nullable = false, name = "is_active")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isActive;
	
	@Column(name = "username", unique=true)
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "AFD_purchase_limit")
	private Double AFD_purchase_limit;
	
	@Column(name = "NonAFD_purchase_limit")
	private Double NonAFD_purchase_limit;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name="fk_user_role_id"))
	private Role role;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_user_store_id"))
	private Store store;

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getAFD_purchase_limit() {
		return AFD_purchase_limit;
	}

	public void setAFD_purchase_limit(Double aFD_purchase_limit) {
		AFD_purchase_limit = aFD_purchase_limit;
	}

	public Double getNonAFD_purchase_limit() {
		return NonAFD_purchase_limit;
	}

	public void setNonAFD_purchase_limit(Double nonAFD_purchase_limit) {
		NonAFD_purchase_limit = nonAFD_purchase_limit;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=").append(id).append(", firstName=").append(firstName).append(", lastName=")
				.append(lastName).append(", emailId=").append(emailId).append(", mobileNumber=").append(mobileNumber).append(", username=").append(username)
				.append(", password=").append(password).append(", gender=").append(gender).append(", isActive=").append(isActive).append(", role=").append(role)
				.append("]");
		return builder.toString();
	}

}
