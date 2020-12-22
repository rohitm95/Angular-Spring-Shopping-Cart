package org.covid.inventory.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.covid.inventory.dao.impl.TimeSlotManagerDoaImpl;
import org.covid.inventory.datajpa.OrdersRepository;
import org.covid.inventory.datajpa.UserRepository;
import org.covid.inventory.entity.OrderDetails;
import org.covid.inventory.entity.Orders;
import org.covid.inventory.entity.Store;
import org.covid.inventory.entity.StoreTimeslots;
import org.covid.inventory.entity.User;
import org.covid.inventory.model.InventoryData;
import org.covid.inventory.model.OrderDto;
import org.covid.inventory.model.RoleDto;
import org.covid.inventory.model.UserDto;
import org.covid.inventory.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeSlotManagerServiceImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeSlotManagerServiceImpl.class);
	
	@Autowired
	private TimeSlotManagerDoaImpl timeSlotManagerDoaImpl;
	
	@Autowired
	private MailServiceImpl mailServiceImpl;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrdersRepository ordersRepository;
	
	public Map<String, List<String>> getAvailableSlots() throws Exception {
		Map<String, List<String>> timeSlotsList = new LinkedHashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try {
				List<StoreTimeslots> result = timeSlotManagerDoaImpl.getExistingTimeSLots();
				Date tempDate = null;
				List<String> tempList = new ArrayList<>();
				for (StoreTimeslots timeSlots : result) {
					Date date = timeSlots.getSlotDate();
					if (tempDate == null) {
						tempList.add(convertDateTo12HrsTime(timeSlots.getSlotFrom(),timeSlots.getSlotTo()));
					} else if (tempDate.equals(timeSlots.getSlotDate())) {
						tempList.add(convertDateTo12HrsTime(timeSlots.getSlotFrom(),timeSlots.getSlotTo()));
					} else {
						if (timeSlotsList.size() < 3) {
							timeSlotsList.put(sdf.format(tempDate), tempList);
							tempList = new ArrayList<>();
						} else {
							break;
						}
					}
					if(result.indexOf(timeSlots) == result.size()-1 && timeSlotsList.size() != 3) {
						timeSlotsList.put(sdf.format(tempDate), tempList);
					}
					tempDate = date;
				}
		} catch (Exception e) {
			LOGGER.info("Exception while booking order :"+e.getMessage());
			e.printStackTrace();
			throw new Exception();
		}
		return timeSlotsList;
	}
	
	public Orders bookOrderWithTimeSlot(OrderDto order) throws Exception {
		Orders orders = null;
		try {
			if(order != null) {
				// insert order in orders table
				orders = createOrdersFromDTO(order, order.getCustomer().getId());
				Integer orderId = timeSlotManagerDoaImpl.bookOrderWithTimeSlot(orders);
				order.setOrder_no(orderId);
				// insert order details in orderDetails table
				List<OrderDetails> orderDetails = createOrdersDetailsFromDTO(order, orderId);
				timeSlotManagerDoaImpl.bookOrderDetailsWithTimeSlot(orderDetails);
				
				// update slot counter in store_timeslot
				timeSlotManagerDoaImpl.updateSlotCounter(orders);
				
				// update slot counter in store_timeslot
				timeSlotManagerDoaImpl.updateItemInfo(orders, order);
				
				String storeName=orders.getStore().getStoreName();
				if(storeName==null || storeName!=null && storeName.isEmpty()) {
					storeName="Golden Katar URC";
				}
				
				final String firstName = Optional.ofNullable(order.getCustomer().getFirstName()).orElse("").toUpperCase();
                final String lastName = Optional.ofNullable(order.getCustomer().getLastName()).orElse("").toUpperCase();
      
				 String textMessage = "Order successfully booked with the store "+storeName+" for the Customer "+firstName+ " "+lastName
				 		+ " Order status is :"+Constants.STATUS_INITIATED;
             	final String emailId=order.getCustomer().getEmailId();
            	if(emailId!=null && !emailId.isEmpty())
            	{
            		String regex = "^(.+)@(.+)$";
            		Pattern pattern = Pattern.compile(regex);
            		Matcher matcher = pattern.matcher(emailId);
            		if(matcher.matches()) {
            			 String textMessage1 = prepareOrderDetails(order, textMessage,storeName);
//            			LOGGER.info("Email body content--->"+textMessage1);
            			sendMail(emailId,orders.getOrderNo(),textMessage1);
					}
            		else {
            			LOGGER.info("Invalid emaild id"+ emailId);
            		}
            	}

			}
		} catch (Exception e) {
			LOGGER.info("Exception while booking order :"+e.getMessage());
			e.printStackTrace();
			throw new Exception();
		}
		LOGGER.info("Successfully order booked with Order No: "+orders.getOrderNo());
		return orders;
	}
    private String prepareOrderDetails(OrderDto orderDto, String orderStatus,String storeName) {
    	DecimalFormat df = new DecimalFormat("#.##");
    	StringBuffer orderDetails=new StringBuffer("");
    	orderDetails.append("Dear "+orderDto.getCustomer().getFirstName().toUpperCase()+",");
    	orderDetails.append("<br>");
    	orderDetails.append(orderStatus);
    	orderDetails.append("<br>");
    	orderDetails.append("Order Id: "+orderDto.getOrder_no());
    	orderDetails.append("<br>");
    	orderDetails.append("Delivery Date: "+orderDto.getDeliveryDate());
    	orderDetails.append("<br>");
//    	StoreDto store=orderDto.getStore();
    	orderDetails.append("Delivery Time Slot:" +orderDto.getSlotFrom() + ":" + orderDto.getSlotTo());
//    	System.out.println("Delivery Time Slot:" +orderDto.getSlotFrom() + ":" + orderDto.getSlotTo());
    	/*orderDetails.append("Payable Amount: "+orderDto.getAmountPayable());
    	orderDetails.append(System.getProperty("line.separator")+"<br>");*/
    	orderDetails.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\">\r\n" + 
    			"      <thead>\r\n" + 
    			"        <tr>\r\n" + 
    			"          <th>SL No</th>\r\n" + 
    			"          <th>Item Number</th>\r\n" + 
    			"          <th>Item Name</th>\r\n" + 
    			"          <th>Item Price</th>\r\n" + 
    			"          <th>Quantity</th>\r\n" + 
    			"          <th>Amount</th>\r\n" + 
    			"        </tr>\r\n" + 
    			"		\r\n" + 
    			"      </thead>	  <tbody>");
    	
    	double totalAmount=0.0;
    	int i=0;
    	for(InventoryData inventoryData : orderDto.getInventoryDatas()) {
    		orderDetails.append("<tr>");
    		orderDetails.append("<td>");
    		orderDetails.append(++i);
    		orderDetails.append("</td>");
    		orderDetails.append("<td>");
    		orderDetails.append(inventoryData.getItemNumber());
    		orderDetails.append("</td>");
    		orderDetails.append("<td>");
    		orderDetails.append(inventoryData.getItemName());
    		orderDetails.append("</td>");
    		orderDetails.append("<td>");
    		orderDetails.append(inventoryData.getPrice());
    		orderDetails.append("</td>");
    		orderDetails.append("<td>");
    		orderDetails.append(inventoryData.getNoOfItemsOrderded());
    		orderDetails.append("</td>");
    		orderDetails.append("<td align='right'>");
    		

    		double amount=inventoryData.getNoOfItemsOrderded()*inventoryData.getPrice();
    		
    		orderDetails.append(df.format(amount));
    		orderDetails.append("</td>");
    		totalAmount=totalAmount+amount;
    		orderDetails.append("</tr>");
           
        }
    	orderDetails.append("<tr>");
    	orderDetails.append("<td colspan='5'>");
    	orderDetails.append("Totoal Amount to be paid");
    	orderDetails.append("</td>");
    	orderDetails.append("<td align='right'>");
    	orderDetails.append(Math.round(totalAmount));
    	orderDetails.append("</td>");
    	orderDetails.append("</tr>");
    	orderDetails.append("</tbody></table>");
    	orderDetails.append("<br>");
    	orderDetails.append("<br>");
    	orderDetails.append("Regards,");
    	orderDetails.append("<br>");
    	orderDetails.append(storeName+" Support Team");
    	
    	return orderDetails.toString();
    }

	public OrderDto changeOrderStatus(OrderDto orderDto, RoleDto roleDto) throws Exception {
		String updatedStatus = null;
		boolean isOrderUpdated = false;
		try {

			if (Constants.USER_ADMIN == roleDto.getId()) {
				if (Constants.STATUS_INITIATED.equalsIgnoreCase(orderDto.getStatus()))
					updatedStatus = Constants.STATUS_READY_TO_DELIVER;
				else if (Constants.STATUS_READY_TO_DELIVER.equalsIgnoreCase(orderDto.getStatus()))
					updatedStatus = Constants.STATUS_COMPLETED;

			} else if (Constants.USER_AGGREGATOR == roleDto.getId()) {
				if (Constants.STATUS_INITIATED.equalsIgnoreCase(orderDto.getStatus()))
					updatedStatus = Constants.STATUS_READY_TO_DELIVER;
			}
			if (updatedStatus != null) {
				orderDto.setStatus(updatedStatus);
				timeSlotManagerDoaImpl.changeOrderStatus(orderDto.getOrder_no(), updatedStatus);
				LOGGER.info("orderDto.getIsOrderChanged()--->" + orderDto.getIsOrderChanged());
				if (orderDto.getIsOrderChanged()) {
					Map<String, Integer> inventoryDataToUpdate = new HashMap<>();
					Map<String, String> orderDetailDataToUpdate = new HashMap<>();
					for (InventoryData inventoryData : orderDto.getInventoryDatas()) {
						// if(inventoryData.getNoOfItemsOrderded() != 0) {// will be set from ui if
						// agregator/admin quantity
						inventoryDataToUpdate.put(inventoryData.getItemNumber(),
								inventoryData.getStock() - inventoryData.getNoOfItemsOrderded());
						orderDetailDataToUpdate.put(inventoryData.getItemNumber(),
								inventoryData.getPrice() + "," + inventoryData.getNoOfItemsOrderded());
						// }
					}
					if (!inventoryDataToUpdate.isEmpty()) {
						LOGGER.info("inverntor is not empt");
						isOrderUpdated = timeSlotManagerDoaImpl.updateOrder(inventoryDataToUpdate, orderDto,
								orderDetailDataToUpdate);
						LOGGER.info("isOrderUpdated--->" + isOrderUpdated);
					}
				}

				final UserDto userDto = orderDto.getCustomer();
				if (userDto != null) {
					final String emailId = userDto.getEmailId();

					if (emailId != null && !emailId.isEmpty()) {
						String regex = "^(.+)@(.+)$";

						Pattern pattern = Pattern.compile(regex);
						Matcher matcher = pattern.matcher(emailId);
						if (matcher.matches()) {

							final String firstName = Optional.ofNullable(userDto.getFirstName()).orElse("")
									.toUpperCase();
							final String lastName = Optional.ofNullable(userDto.getLastName()).orElse("").toUpperCase();
							updatedStatus = "Order Status Changed to  " + updatedStatus + " for the Customer "
									+ firstName + " " + lastName;

							LOGGER.info("status changed()--->" + updatedStatus);
							String storeName = orderDto.getStore().getStoreName();
							if (storeName == null || storeName != null && storeName.isEmpty()) {
								storeName = "Golden Katar URC";
							}
							String textMessage = prepareOrderDetails(orderDto, updatedStatus, storeName);
							//LOGGER.info("Email body content--->" + textMessage);
							
							sendMail(orderDto.getCustomer().getEmailId(),orderDto.getOrder_no(),textMessage);
						} else {
							LOGGER.info("Invalid emaild id" + emailId);
						}
					}
				}

			}

		} catch (Exception e) {
			LOGGER.info("Exception while Changing stauts of order :" + e.getMessage());
			e.printStackTrace();
			throw new Exception();

		}
		return orderDto;
	}

	private List<OrderDetails> createOrdersDetailsFromDTO(OrderDto order, int orderId) {
		List<OrderDetails> orderDetails = new ArrayList<>();
		List<InventoryData> inventoryDatas = order.getInventoryDatas();
		for(InventoryData inventoryData : inventoryDatas) {
			OrderDetails orderDetail = new OrderDetails();
			orderDetail.setAmount(inventoryData.getNoOfItemsOrderded()*inventoryData.getPrice());
			orderDetail.setActive(true);
			orderDetail.setItemName(inventoryData.getItemName());
			orderDetail.setItemNumber(inventoryData.getItemNumber());
			orderDetail.setOrderNo(orderId);
			orderDetail.setPrice(inventoryData.getPrice());
			orderDetail.setQuantity(inventoryData.getNoOfItemsOrderded());
			
			orderDetails.add(orderDetail);
			
		}
		return orderDetails;
	}

	private Orders createOrdersFromDTO(OrderDto order, int customerId) {
		Orders orders = new  Orders();
		orders.setAmountPayable(order.getAmountPayable());
		orders.setSlotFrom(order.getSlotFrom());
		orders.setSlotTo(order.getSlotTo());
		orders.setDeliveryDate(order.getDeliveryDate());
		User user = new User();
		user.setId(customerId);
		orders.setCustomer(user);
		orders.setStatus(Constants.STATUS_INITIATED);
		orders.setActive(true);
		Store store = new Store();
		store.setId(1);
		orders.setStore(store);
		return orders;
	}

	private String convertDateTo12HrsTime(String slotFrom, String slotTo) {
		String newTimeSlot = null;
		SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
		SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
		try {
			Date _24HourDtslotFrom =_24HourSDF.parse(slotFrom);
			newTimeSlot = _12HourSDF.format(_24HourDtslotFrom);
			
			Date _24HourDtslotTo =_24HourSDF.parse(slotTo);
			newTimeSlot = newTimeSlot + "-" +_12HourSDF.format(_24HourDtslotTo);
			
		} catch (ParseException e) {
			LOGGER.info("Exception "+e.getMessage());
		}
		return newTimeSlot;
	}
	
	
	/*
	 * Get user monthly and yearly limits
	 * also gets total of all orders of current month and current year
	 * 
	 *  @param customerId/userId whose orders total we want to get
	 *  
	 *  @return Map containing yearly and monthly limits and order total placed in current month and year
	 */
	public Map<String, Map<String, Double>> getExpensesAndLimit(Integer customerId) {
		Map<String, Map<String, Double>> limits = new HashMap<String, Map<String,Double>>();
		
		User user = userRepository.findById(customerId).get();
		
		Double currentYearOrderTotal = getYearlyOrderTotal(user);
		Double currentMonthOrderTotal = getMonthlyOrderTotal(user);
		
		limits.put("Year", new HashMap<String, Double>(){{
			put("Limit", user.getAFD_purchase_limit());
			put("Expend", currentYearOrderTotal!=null ? currentYearOrderTotal : 0.0 );
		}});
		limits.put("Month", new HashMap<String, Double>(){{
			put("Limit", user.getNonAFD_purchase_limit());
			put("Expend", currentMonthOrderTotal!=null ? currentMonthOrderTotal : 0.0);
		}});
		
		return limits;
	}
	
	/*
	 * Get total of all orders placed in current year
	 * 
	 *  @param user whose orders total we want to get
	 *  
	 *  @return total of all orders placed in current year
	 */
	private Double getYearlyOrderTotal(User customer) {
		Calendar today = Calendar.getInstance();
		
		Calendar firstDayOfYear = Calendar.getInstance();
		firstDayOfYear.set(today.get(Calendar.YEAR), 0, 1);
		
		Calendar lastDayOfYear = Calendar.getInstance();
		lastDayOfYear.set(today.get(Calendar.YEAR), 11, 31);
		
		return ordersRepository.getOrderTotalBetweenDates(customer, firstDayOfYear.getTime(), lastDayOfYear.getTime());
	}
	
	/*
	 * Get total of all orders placed in current month
	 * 
	 *  @param user whose orders total we want to get
	 *  
	 *  @return total of all orders placed in current month
	 */
	private Double getMonthlyOrderTotal(User customer) {
		Calendar today = Calendar.getInstance();
		YearMonth yearMonth = YearMonth.of(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1);
		
		Date currentMonthFirstDay = Date.from(yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date currentMonthLastDay = Date.from(yearMonth.atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		return ordersRepository.getOrderTotalBetweenDates(customer, currentMonthFirstDay, currentMonthLastDay);
	}

	private boolean sendMail(final String emailId, final Integer orderNo,final String textMessage)
	{
		final ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
		emailExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					mailServiceImpl.sendMail(emailId,orderNo, textMessage);
				} catch (Exception e) {
					LOGGER.error("failed", e);
				}
			}
		});
		emailExecutor.shutdown();
		return true;
	}
	
}
