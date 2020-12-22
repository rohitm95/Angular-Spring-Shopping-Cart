package org.covid.inventory.model;

import javax.validation.constraints.NotBlank;

public class ChangePassword {
	private Integer id;

	@NotBlank(message = "{new.password.nonempty}")
	private String password;

	@NotBlank(message = "{password.nonempty}")
	private String oldPassword;

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

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Override
	public String toString() {
		return "ChangePassword [id=" + id + ", new password=" + password + ", oldPassword=" + oldPassword + "]";
	}


}
