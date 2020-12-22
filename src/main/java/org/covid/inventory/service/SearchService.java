package org.covid.inventory.service;

import java.util.List;

import org.covid.inventory.model.OrderDto;

public interface SearchService {
	
	public List<OrderDto> searchOrderData(String fname,String orderNo,String dDate,String tSlot);
}
