package org.covid.inventory.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserPasswordDto {

	private Integer id;

	@NotBlank(message = "{password.nonempty}")
	private String password;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDto [id=").append(id).append(", firstName=").append(", password=").append(password).append("]");
		return builder.toString();
	}

}
