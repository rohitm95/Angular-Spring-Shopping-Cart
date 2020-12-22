package org.covid.inventory.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.covid.inventory.entity.Orders;
import org.covid.inventory.model.ChangeOrderDto;
import org.covid.inventory.model.OrderDto;
import org.covid.inventory.model.RoleDto;
import org.covid.inventory.service.OrdersService;
import org.covid.inventory.service.impl.TimeSlotManagerServiceImpl;
import org.covid.inventory.transform.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Validated
@PropertySource("classpath:messages.properties")
@RequestMapping("/api/availableSlots")
public class TimeSlotManagerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeSlotManagerController.class);

	@Autowired
	private TimeSlotManagerServiceImpl timeSlotManagerServiceImpl;
	
	@Autowired
	private OrdersService ordersService;
	
	@Value("${limit.exceed.month}")
	private String monthlyLimitExceedMessage;
	
	@Value("${limit.exceed.year}")
	private String yearlyLimitExceedMessage;
	
	@Value("${limit.exceed.month.admin}") private String monthlyLimitExceedMessageAdmin;
	
	@Value("${limit.exceed.year.admin}") private String yearlyLimitExceedMessageAdmin;

	@GetMapping
	public ResponseEntity<?> getAvailableSlots(@RequestParam("userId") Integer userId, @RequestParam("amountPayable") Double amountPayable) {
		LOGGER.info("Web Service called : /getAvailableSlots");
		ResponseMessage<Map<String, List<String>>> responseMessage = new ResponseMessage<Map<String, List<String>>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Timeslot data not available!!!");
		Map<String, List<String>> timeSlotsList = null;
		try {
			// checking if monthly & yearly purchase limit is available
			// i.e. purchase limit available > amountPayable of order being placed
			if(isLimitAvailable(userId, amountPayable, responseMessage)) {
				timeSlotsList = timeSlotManagerServiceImpl.getAvailableSlots();
				if (timeSlotsList != null && !timeSlotsList.isEmpty()) {
					responseMessage.setResult(timeSlotsList);
					responseMessage.setStatus(200);
					responseMessage.setStatusText("SUCCESS");
				}
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<Map<String, List<String>>>>(responseMessage,HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<ResponseMessage<Map<String, List<String>>>>(responseMessage,HttpStatus.OK);
	}

	@PostMapping("/bookOrder")
	public ResponseEntity<?> bookOrderWithTimeSlot(@Valid @RequestBody OrderDto orderDto) {
		LOGGER.info("Web Service called : /getAvailableSlots/bookOrder");
		ResponseMessage<Orders> responseMessage = new ResponseMessage<Orders>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Timeslot not available!!!");
		Orders orders = null;
		try {
			orders = timeSlotManagerServiceImpl.bookOrderWithTimeSlot(orderDto);
			if (orders != null) {
				responseMessage.setResult(orders);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<Orders>>(responseMessage,HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<ResponseMessage<Orders>>(responseMessage,HttpStatus.OK);

	}

	@PutMapping("/orderStatus")
	public ResponseEntity<?> changeOrderStatus(@Valid @RequestBody ChangeOrderDto changeOrderDto) {
		LOGGER.info("Web Service called : /orderStatus");
		ResponseMessage<ChangeOrderDto> responseMessage = new ResponseMessage<ChangeOrderDto>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Order Status Not Changed!!!");
		OrderDto orderDto = null;
		RoleDto roleDto = null;
		
		try {
			orderDto = changeOrderDto.getOrderDto();
			roleDto = changeOrderDto.getRoleDto();
			
			// checking if order is changed
			// and if order changed then check if newly added items exceeds customer yearly and/or monthly limit or not
			if(!orderDto.getIsOrderChanged() || (orderDto.getIsOrderChanged() && canUpdateOrder(orderDto, responseMessage))) {
				orderDto = timeSlotManagerServiceImpl.changeOrderStatus(orderDto, roleDto);
				if (orderDto != null) {
					responseMessage.setResult(changeOrderDto);
					responseMessage.setStatus(200);
					responseMessage.setStatusText("SUCCESS");
				}
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<ChangeOrderDto>>(responseMessage,HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<ResponseMessage<ChangeOrderDto>>(responseMessage,HttpStatus.OK);
	}
	
	/*
	 * Check if user is allowed to place order of total amountPayable
	 * by checking their monthly and yearly available purchase limit
	 * 
	 * @param userId, who is trying place order
	 * @param amountPayable, current order total amount
	 * @param ResponseMessage<Orders> object, to set response status and result
	 * 
	 * @return true if purchase limit available
	 * @return false if purchase limit not available
	 */
	private Boolean isLimitAvailable(Integer userId, Double amountPayable, ResponseMessage<Map<String, List<String>>> responseMessage) {
		try {
			LOGGER.info("Checking available purchase limit for user : " + userId);
			
			Map<String, Map<String, Double>> expensesAndLimits = timeSlotManagerServiceImpl.getExpensesAndLimit(userId);
			
			Map<String, Double> yearlyData = expensesAndLimits.get("Year");
			Map<String, Double> monthlyData = expensesAndLimits.get("Month");
			
			// checking if yearly limit is available
			if(yearlyData.get("Expend")<yearlyData.get("Limit") && (yearlyData.get("Limit") - yearlyData.get("Expend")) > amountPayable) {
				// checking if monthly limit is available
				if(monthlyData.get("Expend")<monthlyData.get("Limit") && (monthlyData.get("Limit") - monthlyData.get("Expend")) > amountPayable) {
					return true;
				} else {	// if monthly limit exceeds amountPayable of current order
					responseMessage.setResult(null);
					responseMessage.setStatus(400);
					responseMessage.setStatusText(monthlyLimitExceedMessage + (monthlyData.get("Limit") - monthlyData.get("Expend")));
				}
			} else {	// if yearly limit exceeds amountPayable of current order
				responseMessage.setResult(null);
				responseMessage.setStatus(400);
				responseMessage.setStatusText(yearlyLimitExceedMessage + (yearlyData.get("Limit") - yearlyData.get("Expend")));
			}
		} catch (Exception e) {
			LOGGER.info("Exception while Checking available purchase limit of user :"+e.getMessage());
            e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * Check if admin / aggregator can update order items quantity
	 * by checking if new items are added and added items amount exceeds customer yearly and/or monthly limit 
	 * 
	 * @param OrderDto, order details received from API
	 * @param ResponseMessage<Orders> object, to set response status and result
	 * 
	 * @return true if added items amount doesn't exceed customer limit
	 * @return false if added items amount does exceed customer limit
	 */
	private Boolean canUpdateOrder(OrderDto orderDto, ResponseMessage<ChangeOrderDto> responseMessage) {
		LOGGER.info("Checking if order item quantity can be changed for order : " + orderDto.getOrder_no());
		try {
			Map<String, Map<String, Double>> expensesAndLimits = timeSlotManagerServiceImpl.getExpensesAndLimit(orderDto.getCustomer().getId());
			Orders orders = ordersService.findByOrderNo(orderDto.getOrder_no());
			
			if(orderDto.getAmountPayable() > orders.getAmountPayable()) {
				Double addOnOrderAmount = orderDto.getAmountPayable() - orders.getAmountPayable();
				
				Map<String, Double> yearlyData = expensesAndLimits.get("Year");
				Map<String, Double> monthlyData = expensesAndLimits.get("Month");
	
				// checking if yearly limit is available
				if(yearlyData.get("Expend")<yearlyData.get("Limit") && (yearlyData.get("Limit") - yearlyData.get("Expend")) > addOnOrderAmount) {
					// checking if monthly limit is available
					if(monthlyData.get("Expend")<monthlyData.get("Limit") && (monthlyData.get("Limit") - monthlyData.get("Expend")) > addOnOrderAmount) {
						return true;
					} else {	// if monthly limit exceeds amountPayable of updated order
						responseMessage.setResult(null);
						responseMessage.setStatus(400);
						responseMessage.setStatusText(monthlyLimitExceedMessageAdmin + (monthlyData.get("Limit") - monthlyData.get("Expend")));
					}
				} else {	// if yearly limit exceeds amountPayable of updated order
					responseMessage.setResult(null);
					responseMessage.setStatus(400);
					responseMessage.setStatusText(yearlyLimitExceedMessageAdmin + (yearlyData.get("Limit") - yearlyData.get("Expend")));
				}
			} else {
				return true;
			}
		} catch(Exception e) {
			LOGGER.info("Exception while Checking if order item quantity can be changed for order : " + orderDto.getOrder_no());
			e.printStackTrace();
		}

		return false;
	}
}
