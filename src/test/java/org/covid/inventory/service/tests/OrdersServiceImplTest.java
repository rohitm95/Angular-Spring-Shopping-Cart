package org.covid.inventory.service.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.covid.inventory.service.impl.OrdersServiceImpl;
import org.covid.inventory.transform.util.BreakTimeUtil;
import org.covid.inventory.transform.util.StoreHolidaysUtil;
import org.covid.inventory.transform.util.StoreTimingsUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.covid.inventory.datajpa.InventoryRepository;
import org.covid.inventory.datajpa.OrderDetailRepository;
import org.covid.inventory.datajpa.OrdersRepository;
import org.covid.inventory.datajpa.StoreTimeslotsRepository;
import org.covid.inventory.entity.Orders;
import org.covid.inventory.model.OrderDetailsDto;
import org.covid.inventory.model.OrderDto;

@RunWith(MockitoJUnitRunner.class)
class OrdersServiceImplTest {

	@Mock
	private ModelMapper mapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersServiceImpl.class);

	@Mock
	private OrdersRepository ordersRepository;

	@Mock
	private OrderDetailRepository orderDetailsRepository;

	@Mock
	private InventoryRepository inventoryRepository;

	@Mock
	private StoreTimeslotsRepository storeTimeslotsRepository;

	@Mock
	private BreakTimeUtil breakTimeUtil;

	@Mock
	private StoreHolidaysUtil storeHolidaysUtil;

	@Mock
	private StoreTimingsUtil storeTimingsUtil;

	@InjectMocks
	private OrdersServiceImpl ordersServiceImpl;

	Orders order;

	Orders res = null;

	@Test
	void getAllOrders() throws Exception {
		MockitoAnnotations.initMocks(this);
		Optional<List<String>> status = Optional.of(new ArrayList<String>());
		List<String> other = new ArrayList<String>();
		other.add("Initiated");
		status.orElse(other);
		List<OrderDto> orderDtoList = new ArrayList<OrderDto>();
		orderDtoList.add(MockData.getOrderDto(1));
		orderDtoList.add(MockData.getOrderDto(2));
		orderDtoList.add(MockData.getOrderDto(3));

		when(ordersServiceImpl.getOrdersList(status)).thenReturn(orderDtoList);

		assertEquals(orderDtoList.get(1).getAmountPayable(), 1200);
	}

	@Test
	void getAllOrdersNotNull() throws Exception {
		MockitoAnnotations.initMocks(this);
		Optional<List<String>> status = Optional.of(new ArrayList<String>());
		List<String> other = new ArrayList<String>();
		other.add("Initiated");
		status.orElse(other);
		List<OrderDto> orderDtoList = new ArrayList<OrderDto>();
		orderDtoList.add(MockData.getOrderDto(1));
		orderDtoList.add(MockData.getOrderDto(2));
		orderDtoList.add(MockData.getOrderDto(3));

		when(ordersServiceImpl.getOrdersList(status)).thenReturn(orderDtoList);

		assertNotNull(orderDtoList);
	}

	@Test
	void getAllOrdersNumberOfOrders() throws Exception {
		MockitoAnnotations.initMocks(this);
		Optional<List<String>> status = Optional.of(new ArrayList<String>());
		List<String> other = new ArrayList<String>();
		other.add("Initiated");
		status.orElse(other);
		List<OrderDto> orderDtoList = new ArrayList<OrderDto>();
		orderDtoList.add(MockData.getOrderDto(1));
		orderDtoList.add(MockData.getOrderDto(2));
		orderDtoList.add(MockData.getOrderDto(3));

		when(ordersServiceImpl.getOrdersList(status)).thenReturn(orderDtoList);

		assertEquals(orderDtoList.size(), 3);
	}

	@Test
	void findByOrderNumber() throws Exception {
		MockitoAnnotations.initMocks(this);
		Optional<Orders> value = Optional.of(MockData.getOrder(1));
		when(ordersRepository.findById(1)).thenReturn(value);
		res = ordersServiceImpl.findByOrderNo(1);
		assertEquals(res.getOrderNo(), 1);
	}

	@Test
	void findByOrderNumberNotNull() throws Exception {
		MockitoAnnotations.initMocks(this);
		Optional<Orders> value = Optional.of(MockData.getOrder(1));
		when(ordersRepository.findById(1)).thenReturn(value);
		res = ordersServiceImpl.findByOrderNo(1);
		assertNotNull(res);
	}

	@Test
	void getOrderDetails() throws Exception {
		MockitoAnnotations.initMocks(this);
		List<OrderDetailsDto> OrderDetailsDtoList = new ArrayList<OrderDetailsDto>();
		OrderDetailsDtoList.add(MockData.getOrderDetailsDto(1));
		when(ordersServiceImpl.giveOrderDetails(1)).thenReturn(OrderDetailsDtoList);
		assertEquals(OrderDetailsDtoList.get(0).getAmount(), 1200);
	}

	@Test
	void getOrderDetailsNotNull() throws Exception {
		MockitoAnnotations.initMocks(this);
		List<OrderDetailsDto> OrderDetailsDtoList = new ArrayList<OrderDetailsDto>();
		OrderDetailsDtoList.add(MockData.getOrderDetailsDto(1));
		when(ordersServiceImpl.giveOrderDetails(1)).thenReturn(OrderDetailsDtoList);
		assertNotNull(OrderDetailsDtoList);
	}

	@Test
	void getUpdateData() throws Exception {
		MockitoAnnotations.initMocks(this);
		List<OrderDto> orderDto = new ArrayList<OrderDto>();
		orderDto.add(MockData.getOrderDto(1));
		when(ordersServiceImpl.getUpdatedData()).thenReturn(orderDto);
		assertEquals(orderDto.get(0).getAmountPayable(), 1200);
	}

	@Test
	void getUpdateDataNotNull() throws Exception {
		MockitoAnnotations.initMocks(this);
		List<OrderDto> orderDto = new ArrayList<OrderDto>();
		orderDto.add(MockData.getOrderDto(1));
		when(ordersServiceImpl.getUpdatedData()).thenReturn(orderDto);
		assertNotNull(orderDto);
	}

	@Test
	void getOrderByCustomerId() throws Exception {
		MockitoAnnotations.initMocks(this);
		List<Orders> ordersList = new ArrayList<Orders>();
		ordersList.add(MockData.getOrder(1));
		when(ordersRepository.getOrderByCustomerId(1)).thenReturn(ordersList);
		List<Orders> resList = ordersServiceImpl.getOrderByCustomerId(1);
		assertEquals(resList.get(0).getCustomer().getFirstName(), "user1");
	}

	@Test
	void getOrderByCustomerIdNotNull() throws Exception {
		MockitoAnnotations.initMocks(this);
		List<Orders> ordersList = new ArrayList<Orders>();
		ordersList.add(MockData.getOrder(1));
		when(ordersRepository.getOrderByCustomerId(1)).thenReturn(ordersList);
		List<Orders> resList = ordersServiceImpl.getOrderByCustomerId(1);
		assertNotNull(resList);
	}

	@Test
	void updateReadyPendingOrdersList() throws Exception {
		MockitoAnnotations.initMocks(this);
		List<Orders> ordersList = new ArrayList<Orders>();
		ordersList.add(MockData.getOrder(1));
		when(ordersRepository.updateReadyPendingOrders()).thenReturn(ordersList);
		List<Orders> resList = ordersServiceImpl.updateReadyPendingOrdersList();
		assertEquals(resList.get(0).getAmountPayable(), 1200);
	}

	@Test
	void updateReadyPendingOrdersListNotNull() throws Exception {
		MockitoAnnotations.initMocks(this);
		List<Orders> ordersList = new ArrayList<Orders>();
		ordersList.add(MockData.getOrder(1));
		when(ordersRepository.updateReadyPendingOrders()).thenReturn(ordersList);
		List<Orders> resList = ordersServiceImpl.updateReadyPendingOrdersList();
		assertNotNull(resList);
	}

	@Test
	void checkFun() throws Exception {
		MockitoAnnotations.initMocks(this);
		order = MockData.getOrder(1);
		String time1 = order.getSlotFrom();
		String time2 = order.getSlotTo();
		assertEquals(ordersServiceImpl.checkFun(time1, time2), 0);
	}

	@Test
	void checkFunNotNull() throws Exception {
		MockitoAnnotations.initMocks(this);
		order = MockData.getOrder(1);
		String time1 = order.getSlotFrom();
		String time2 = order.getSlotTo();
		assertNotNull(ordersServiceImpl.checkFun(time1, time2));
	}
	
	
}
