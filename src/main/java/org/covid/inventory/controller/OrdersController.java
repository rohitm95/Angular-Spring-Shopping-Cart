package org.covid.inventory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.covid.inventory.datajpa.OrdersRepository;
import org.covid.inventory.entity.Orders;
import org.covid.inventory.entity.User;
import org.covid.inventory.model.OrderDetailsDto;
import org.covid.inventory.model.OrderDto;
import org.covid.inventory.model.UserDto;
import org.covid.inventory.service.OrdersService;
import org.covid.inventory.transform.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/orders")
public class OrdersController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

	private OrdersService ordersService;
	
	@Autowired
	public OrdersController(OrdersService theOrdersService) {
		ordersService = theOrdersService;
	}

	@GetMapping	
	public ResponseEntity<ResponseMessage<List<OrderDto>>> findAll(@RequestParam("status") Optional<String[]> status) {
		// System.out.println(ordersService.findAll());
		LOGGER.info("Web Service called : /api/orders");
		List<OrderDto> ordersDtoData = null;
		ResponseMessage<List<OrderDto>> responseMessage = new ResponseMessage<List<OrderDto>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		String orderFilterArray[]=null;
		List<String> orderFilterList=null;
		Optional<List<String>> orderFilter= Optional.of(new ArrayList<String>());
		
		try {
			
			if(status.isPresent())
				{
					orderFilterArray= status.get();
					 orderFilterList = Arrays.asList(orderFilterArray);
					 orderFilter=Optional.of(orderFilterList);
				}
			ordersDtoData = ordersService.getOrdersList(orderFilter);
				if (ordersDtoData != null && ordersDtoData.size() > 0) {
				responseMessage.setResult(ordersDtoData);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<OrderDto>>>(responseMessage, HttpStatus.OK);
			}

		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<OrderDto>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<OrderDto>>>(responseMessage, HttpStatus.NOT_FOUND);

	}

	@Deprecated
	@GetMapping("/orderDetailss/{orderId}")
	public ResponseEntity<ResponseMessage<List<OrderDetailsDto>>> giveOrderDetails(@PathVariable int orderId) {
		LOGGER.info("Web Service called : /api/orders/orderDetails/{orderId}");
		List<OrderDetailsDto> orderDetailsData = null;
		ResponseMessage<List<OrderDetailsDto>> responseMessage = new ResponseMessage<List<OrderDetailsDto>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");

		try {
			orderDetailsData = ordersService.giveOrderDetails(orderId);
			if (orderDetailsData != null && orderDetailsData.size() > 0) {
				responseMessage.setResult(orderDetailsData);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<OrderDetailsDto>>>(responseMessage, HttpStatus.OK);
			}

		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<OrderDetailsDto>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<OrderDetailsDto>>>(responseMessage, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/orderDetails/{orderId}")
	public ResponseEntity<ResponseMessage<OrderDto>> getOrderDetailsById(@PathVariable int orderId) {
		LOGGER.info("Web Service called : /api/orders/orderDetails/{orderId}");
		OrderDto orderDetailsData = null;
		ResponseMessage<OrderDto> responseMessage = new ResponseMessage<OrderDto>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");

		try {
			orderDetailsData = ordersService.getOrderDetailsById(orderId);
			if (orderDetailsData != null) {
				responseMessage.setResult(orderDetailsData);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<OrderDto>>(responseMessage, HttpStatus.OK);
			}

		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<OrderDto>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<OrderDto>>(responseMessage, HttpStatus.NOT_FOUND);
	}
	@GetMapping("/refresh")
	public ResponseEntity<ResponseMessage<List<OrderDto>>> giveStatusDetails() {
		LOGGER.info("Web Service called : /api/orders/refresh");
		ResponseMessage<List<OrderDto>> responseMessage = new ResponseMessage<List<OrderDto>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		List<OrderDto> orders = null;
		try {
			orders = ordersService.getUpdatedData();
			if (orders != null && !orders.isEmpty()) {
				responseMessage.setResult(orders);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<OrderDto>>>(responseMessage, HttpStatus.OK);
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<OrderDto>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<OrderDto>>>(responseMessage, HttpStatus.NOT_FOUND);
	}

	@PutMapping("/cancelOrder")
	public ResponseEntity<?> cancelOrder(@RequestBody OrderDto dto) throws Exception {
		LOGGER.info("Web Service called : /api/orders/cancelOrder");
		Date date = (Date) dto.getDeliveryDate();
		int storeId = dto.getStore().getId();
		String tm = dto.getSlotFrom();
		try {

			// updating orders table
			if (ordersService.updateOrderTable(dto) == 1) {

				// updating inventory table
				ordersService.updateInventoryTable(dto.getOrder_no());

				// updating store_timeslots table
				ordersService.updateAvailCountTimeSlot(storeId, date, tm);
			}
		} catch (Exception e) {
			ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE);
		}

		return ResponseEntity.ok("Successfully status for order changed for Order No: " + dto.getOrder_no());
	}
	
	@GetMapping("/updateReadyPendingOrders")
	public ResponseEntity<ResponseMessage<List<Orders>>> getPendingReadyOrders() {
		LOGGER.info("Web Service called : /api/orders/updateReadyPendingOrders");
		List<Orders> ordersData = null;
		ResponseMessage<List<Orders>> responseMessage = new ResponseMessage<List<Orders>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		try {
			ordersData = ordersService.updateReadyPendingOrdersList();
			if (ordersData != null && ordersData.size() > 0) {
				responseMessage.setResult(ordersData);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<Orders>>>(responseMessage, HttpStatus.OK);
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<Orders>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<Orders>>>(responseMessage, HttpStatus.NOT_FOUND);
	}
	
	
	@GetMapping("/customer/{id}")
	public ResponseEntity<ResponseMessage<List<Orders>>> getOrderByCustomerId(@PathVariable Integer id) {
		LOGGER.info("Web Service called : /api/orders/{id}");
		List<Orders> ordersData = null;
		ResponseMessage<List<Orders>> responseMessage = new ResponseMessage<List<Orders>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		try {
			ordersData = ordersService.getOrderByCustomerId(id);
			if (ordersData != null && ordersData.size() > 0) {
				responseMessage.setResult(ordersData);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<Orders>>>(responseMessage, HttpStatus.OK);
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<Orders>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<Orders>>>(responseMessage, HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/assignToAggregator")
	public ResponseEntity<ResponseMessage<Orders>> assignOrderToAggregator(@Valid @RequestBody OrderDto orderData) {
		LOGGER.info("Web Service called : /api/orders/assignToAggregator");
		ResponseMessage<Orders> responseMessage = new ResponseMessage<Orders>();
		Orders order = null;
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Aggregator not available for this time slot.");
		try {
			List<User> aggregatorForStore = ordersService.getAggregatorForStore(orderData);
			boolean isAggregatorAvailable = false;
			for (User user : aggregatorForStore) {
				isAggregatorAvailable = ordersService.isAggregatorAvailable(orderData, user.getId());
				if (isAggregatorAvailable) {
					UserDto aggregator = new UserDto();
					aggregator.setId(user.getId());
					orderData.setAggregator(aggregator);
					break;
				}
			}

			if (isAggregatorAvailable) {
				order = ordersService.saveWithAggregator(orderData);
				if (order != null) {
					responseMessage.setResult(order);
					responseMessage.setStatus(200);
					responseMessage.setStatusText("SUCCESS");
				}
				return new ResponseEntity<ResponseMessage<Orders>>(responseMessage, HttpStatus.OK);
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<Orders>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<Orders>>(responseMessage, HttpStatus.NOT_FOUND);

	}
}
