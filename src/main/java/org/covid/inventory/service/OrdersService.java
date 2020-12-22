package org.covid.inventory.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Order;
import javax.validation.Valid;

import org.covid.inventory.entity.Orders;
import org.covid.inventory.entity.User;
import org.covid.inventory.model.OrderDetailsDto;
import org.covid.inventory.model.OrderDto;

public interface OrdersService {
	
	public List<OrderDto> getOrdersList(Optional<List<String>> status) throws Exception;
	
	public List<OrderDetailsDto> giveOrderDetails(int orderId) throws Exception;
	
	public List<OrderDto> getUpdatedData() throws Exception;
	
	public int updateOrderTable(OrderDto dto) throws Exception;

	public void updateInventoryTable(int orderNo) throws Exception;

	public void updateAvailCountTimeSlot(int storeId, Date date, String tm) throws Exception;

	Orders findByOrderNo(Integer orderNo);
	
	public List<Orders> updateReadyPendingOrdersList() throws Exception;
	
	public List<Orders> getOrderByCustomerId (Integer id) throws Exception;

	public OrderDto getOrderDetailsById(int orderId) throws Exception;
	public boolean isAggregatorAvailable(@Valid OrderDto orderData,int id);

	public Orders saveWithAggregator(@Valid OrderDto orderData);

	public List<User> getAggregatorForStore(@Valid OrderDto orderData);


}
