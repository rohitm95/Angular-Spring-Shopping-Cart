package org.covid.inventory.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.covid.inventory.service.impl.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

	private static final String BEARER = "Bearer ";

	@Autowired
	private UserDetailsServiceImpl userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				LOGGER.error("Unable to get JWT Token");
				// TODO: Need to handle this
			} catch (ExpiredJwtException e) {
				LOGGER.error("JWT Token has expired");
				// TODO: Need to handle this
			} catch (MalformedJwtException e) {
				LOGGER.error("JWT Token is in-correct");
			}
		} else {
			LOGGER.debug("JWT Token does not begin with Bearer String");
		}
		// Once we get the token validate it.
		/*Code comment by mayank as Spring security SessionCreationPolicy.IF_REQUIRED*/
		//if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		if (username != null) {
			//System.out.println(SecurityContextHolder.getContext().getAuthentication() != null ?"principal : " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() : "null principal");
			try {
				UserDetails userDetails = this.userService.loadUserByUsername(username);
			 
				// if token is valid configure Spring Security to manually set authentication
				if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// After setting the Authentication in the context, we specify
					// that the current user is authenticated. So it passes the
					// Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			} catch (UsernameNotFoundException e) {
				LOGGER.error("JWT Token User does not exist");
			}
		}
		chain.doFilter(request, response);
	}
}