package org.covid.inventory.exceptions;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InventoryAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		InventoryError inventoryError = new InventoryError();
		inventoryError.setMessage(new ArrayList<String>() {
			{
				add(exception.getMessage());
			}
		});
		inventoryError.setStatus(HttpStatus.UNAUTHORIZED.value());
		inventoryError.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
		response.getOutputStream().println(objectMapper.writeValueAsString(inventoryError));
	}

}