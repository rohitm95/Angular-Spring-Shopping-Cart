package org.covid.inventory.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.covid.inventory.entity.Store;

public class UserDto {

	private Integer id;

	@NotBlank(message = "{first.name.nonempty}")
	private String firstName;

	@NotBlank(message = "{last.name.nonempty}")
	private String lastName;

	@Email(message = "{email.valid}")
	@NotBlank(message = "{email.nonempty}")
	private String emailId;

	@NotBlank(message = "{mobile.number.nonempty}")
	private String mobileNumber;

	@NotBlank(message = "{gender.nonempty}")
	private String gender;

	@NotNull(message = "{is.active.nonempty}")
	private boolean isActive;

//	@Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))", message = "{date.of.join.invalid}")
//	@NotNull(message = "{date.of.join.nonempty}")
	private String dateOfJoin;

	@NotBlank(message = "{username.nonempty}")
	private String username;

	// @NotBlank(message = "{password.nonempty}")
	private String password;

	private RoleDto role;
	
	//added store as foreign key
	private Store store;

	private boolean isNewUser;

	private String category;
	
	private Double AFD_purchase_limit;
	
	private Double NonAFD_purchase_limit;

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

	public String getDateOfJoin() {
		return dateOfJoin;
	}

	public void setDateOfJoin(String dateOfJoin) {
		this.dateOfJoin = dateOfJoin;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public RoleDto getRole() {
		return role;
	}

	public void setRole(RoleDto role) {
		this.role = role;
	}

	public boolean isNewUser() {
		return isNewUser;
	}

	public void setNewUser(boolean isNewUser) {
		this.isNewUser = isNewUser;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDto [id=").append(id).append(", firstName=").append(firstName).append(", lastName=")
				.append(lastName).append(", emailId=").append(emailId).append(", gender=").append(gender)
				.append(", isActive=").append(isActive).append(", dateOfJoin=").append(dateOfJoin).append(", username=")
				.append(username).append(", password=").append(password).append(", role=").append(role)
				.append(", category=").append(category).append(", AFD purchase limit=").append(AFD_purchase_limit)
				.append(", Non AFD purchase limit=").append(NonAFD_purchase_limit).append("]");
		return builder.toString();
	}

}
