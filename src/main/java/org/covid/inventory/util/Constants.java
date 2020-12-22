package org.covid.inventory.util;

public class Constants {

	public static final String HEALTH_CHECK_PATTERN = "/api/welcome";

	public static final String AUTHENTICATE_PATTERN = "/api/authenticate";

	public static final String ID = "id";

	public static final String REGISTER_PATTERN = "/register";
	
	public static final String URL_SEPARATOR = "/";
	
	public static final String IMAGES = "images";
	
	
	/* ALL ORDER STATUS USED IN APPLICATION : STARTS */
	public static final String STATUS_INITIATED = "INITIATED";//pending
	
	public static final String STATUS_READY_TO_DELIVER = "READY_TO_DELIVER";//ready
	
	public static final String STATUS_CANCEL_BY = "CANCEL_BY_";//cancelled
	
	public static final String PENDING_STATUS = "PENDING";//pending status to display on UI
	
	public static final String READY_STATUS = "READY";//ready status to display on UI
	
	public static final String STATUS_COMPLETED = "COMPLETED";
	
	public static final String STATUS_LAPSED = "LAPSED";
	
	public static final String CANCELLED_STATUS = "CANCELLED";//cancelled status to display on UI
		
	
	/* ALL ORDER STATUS USED IN APPLICATION : END */
	
	/* ALL ROLES USED IN APPLICATION : STARTS */
	public static final Integer USER_ADMIN = 1;
	
	public static final Integer USER_CUSTOMER = 2;
	
	public static final Integer USER_AGGREGATOR = 3;
	
	/* ALL ROLES USED IN APPLICATION : END */
	
}
