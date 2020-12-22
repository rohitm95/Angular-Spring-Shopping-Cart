package org.covid.inventory.model;
import java.io.Serializable;
public class JwtResponseDto implements Serializable {
	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final UserDto user;
	
	public JwtResponseDto(String jwttoken, UserDto user) {
		this.jwttoken = jwttoken;
		this.user = user;
	}
	
	public String getToken() {
		return this.jwttoken;
	}
	
	public UserDto getUser() {
		return this.user;
	}
	
}