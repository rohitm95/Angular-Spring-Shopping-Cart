package org.covid.inventory.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.covid.inventory.datajpa.InventoryRepository;
import org.covid.inventory.datajpa.OrderDetailRepository;
import org.covid.inventory.datajpa.OrdersRepository;
import org.covid.inventory.datajpa.StoreTimeslotsRepository;
import org.covid.inventory.datajpa.UserRepository;
import org.covid.inventory.entity.BreakTimings;
import org.covid.inventory.entity.Inventory;
import org.covid.inventory.entity.OrderDetails;
import org.covid.inventory.entity.Orders;
import org.covid.inventory.entity.Store;
import org.covid.inventory.entity.StoreHoliday;
import org.covid.inventory.entity.StoreTimings;
import org.covid.inventory.entity.User;
import org.covid.inventory.model.BreakTimeDto;
import org.covid.inventory.model.InventoryData;
import org.covid.inventory.model.OrderDetailsDto;
import org.covid.inventory.model.OrderDto;
import org.covid.inventory.model.RoleDto;
import org.covid.inventory.model.StoreDto;
import org.covid.inventory.model.StoreHolidayDto;
import org.covid.inventory.model.StoreTimeDto;
import org.covid.inventory.model.UserDto;
import org.covid.inventory.service.OrdersService;
import org.covid.inventory.transform.util.BreakTimeUtil;
import org.covid.inventory.transform.util.StoreHolidaysUtil;
import org.covid.inventory.transform.util.StoreTimingsUtil;
import org.covid.inventory.transform.util.UserUtil;
import org.covid.inventory.util.Constants;
import org.covid.inventory.util.InventoryUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrdersServiceImpl implements OrdersService {
	
	@Autowired
	private ModelMapper mapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersServiceImpl.class);

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private OrderDetailRepository orderDetailsRepository;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	@Autowired
	private StoreTimeslotsRepository storeTimeslotsRepository;
	
	@Autowired
	private BreakTimeUtil breakTimeUtil;
	
	@Autowired
	private StoreHolidaysUtil storeHolidaysUtil;
	
	@Autowired
	private StoreTimingsUtil storeTimingsUtil;
	
	@Autowired
	private UserUtil userUtil;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	public OrdersServiceImpl(OrdersRepository theOrdersRepository, OrderDetailRepository theOrderDetailsRepository) {
		ordersRepository = theOrdersRepository;
		orderDetailsRepository = theOrderDetailsRepository;
	}
	
	@Override
	public Orders findByOrderNo(Integer orderNo) {
		return ordersRepository.findById(orderNo).get();
	}

	@Override
	public List<OrderDto> getOrdersList(Optional<List<String>> status) throws Exception {
		LOGGER.info("OrderService to get orders called");
		List<OrderDto> orderDtos = new ArrayList<>();
		
		List<Orders> orders;
		if(status.isPresent() && (status.get().size()>0)) {
			orders = ordersRepository.findAllByStatus(status.get());
		} else {
			orders = ordersRepository.getOrders();
		}
		
		InventoryUtil util = new InventoryUtil();
		try {
			for (Orders order : orders) {
				OrderDto orderDto = new OrderDto();
				UserDto customer = new UserDto();
				RoleDto role = new RoleDto();
	            role.setId(order.getCustomer().getRole().getId());
	            role.setName(order.getCustomer().getRole().getName());
	            customer.setRole(role);
				customer.setId(order.getCustomer().getId());
				customer.setFirstName(order.getCustomer().getFirstName());
				customer.setLastName(order.getCustomer().getLastName());
				customer.setMobileNumber(order.getCustomer().getMobileNumber());
				customer.setEmailId(order.getCustomer().getEmailId());
				Store store = order.getCustomer().getStore();
				customer.setStore(store);
				orderDto.setCustomer(customer);
				orderDto.setDeliveryDate(order.getDeliveryDate());
				orderDto.setOrder_no(order.getOrderNo());
				/*orderDto.setSlotFrom(order.getSlotFrom());
				orderDto.setSlotTo(order.getSlotTo());*/
				orderDto.setSlotFrom(util.convertDateTo12HrsTime(order.getSlotFrom(), null));
				orderDto.setSlotTo(util.convertDateTo12HrsTime(null,order.getSlotTo()));
				orderDto.setAmountPayable(order.getAmountPayable());
				orderDto.setStatus(order.getStatus());
				orderDto.setIsOrderChanged(false);
				
				StoreDto storeDto = new StoreDto();
				store = order.getStore();
				storeDto.setId(store.getId());
				storeDto.setStoreName(store.getStoreName());
				storeDto.setActive(store.isActive());
				storeDto.setSlotDuration(store.getSlotDuration());
				storeDto.setDeliveryInSlot(store.getDeliveryInSlot());
				
				Set<BreakTimeDto> breakTime = new HashSet<BreakTimeDto>();
				for (BreakTimings breakTimings : store.getBreakTimings()) {
					breakTimeUtil.toDto(breakTimings);
				}
				storeDto.setBreakTimings(breakTime);
				
				Set<StoreHolidayDto> storeHolidays = new HashSet<StoreHolidayDto>();
				for (StoreHoliday storeHoliday : store.getStoreHolidays()) {
					storeHolidaysUtil.toDto(storeHoliday);
				}
				storeDto.setStoreHolidays(storeHolidays);
				
				Set<StoreTimeDto> storeTimings = new HashSet<StoreTimeDto>();
				for (StoreTimings storeTiming : store.getStoreTimings()) {
					storeTimingsUtil.toDto(storeTiming);
				}
				storeDto.setStoreTimings(storeTimings);
				
				orderDto.setStore(storeDto);
				
				// getting order details list
/*				List<InventoryData> invData = new ArrayList<>();
				List<OrderDetails> orderDetails = orderDetailsRepository.giveOrderDetails(order.getOrderNo());
				for (OrderDetails orderDetail : orderDetails) {
					Inventory inv = inventoryRepository.getInventoryFromTable(orderDetail.getItemNumber());
					
						InventoryData inD = new InventoryData();
						inD.setId(orderDetail.getId());
						inD.setItemName(orderDetail.getItemName());
						inD.setItemNumber(orderDetail.getItemNumber());
						if(inv!=null)
						{
							inD.setStock(inv.getStock());
							if(inv.getStock() <= inv.getLowStockIndicator())
								inD.setLowStockIndicator(true);
							else
								inD.setLowStockIndicator(false);
							
							inD.setMonthlyQuotaPerUser(inv.getMonthlyQuotaPerUser());
							inD.setToBeSoldOneItemPerOrder(inv.isOnePerItem());
						}
						
						
						inD.setNoOfItemsOrderded(orderDetail.getQuantity());
						inD.setPrice(orderDetail.getPrice());
						
						invData.add(inD);
					
				}
				
				orderDto.setInventoryDatas(invData); */
				
				orderDtos.add(orderDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return orderDtos;
	}

	@Override
	public List<OrderDetailsDto> giveOrderDetails(int orderId) throws Exception {
		LOGGER.info("OrderService to get order details called");
		List<OrderDetailsDto> orderDetailsDtos = new ArrayList<>();
		List<OrderDetails> orderDetails = orderDetailsRepository.giveOrderDetails(orderId);
		try {
			for (OrderDetails orderDetail : orderDetails) {
				OrderDetailsDto orderDteailsDto = new OrderDetailsDto();
				orderDteailsDto.setId(orderDetail.getId());
				orderDteailsDto.setAmount(orderDetail.getAmount());
				orderDteailsDto.setItem_name(orderDetail.getItemName());
				orderDteailsDto.setOrder_no(orderDetail.getOrderNo());
				orderDteailsDto.setPrice(orderDetail.getPrice());
				orderDteailsDto.setQuantity(orderDetail.getQuantity());
				orderDetailsDtos.add(orderDteailsDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return orderDetailsDtos;
	}
	
	@Override
	public OrderDto getOrderDetailsById(final int orderId) throws Exception {
		LOGGER.info("OrderService to get order details called");
		InventoryUtil util = new InventoryUtil();
		final Orders order = findByOrderNo(orderId);
		OrderDto orderDto = new OrderDto();
		UserDto customer = new UserDto();
		RoleDto role = new RoleDto();
        role.setId(order.getCustomer().getRole().getId());
        role.setName(order.getCustomer().getRole().getName());
        customer.setRole(role);
		customer.setId(order.getCustomer().getId());
		customer.setFirstName(order.getCustomer().getFirstName());
		customer.setLastName(order.getCustomer().getLastName());
		customer.setMobileNumber(order.getCustomer().getMobileNumber());
		customer.setEmailId(order.getCustomer().getEmailId());
		orderDto.setCustomer(customer);
		StoreDto st = new StoreDto();
        st.setId(order.getStore().getId());
        orderDto.setStore(st);
		orderDto.setDeliveryDate(order.getDeliveryDate());
		orderDto.setOrder_no(order.getOrderNo());
		/*orderDto.setSlotFrom(order.getSlotFrom());
		orderDto.setSlotTo(order.getSlotTo());*/
		orderDto.setSlotFrom(util.convertDateTo12HrsTime(order.getSlotFrom(), null));
		orderDto.setSlotTo(util.convertDateTo12HrsTime(null,order.getSlotTo()));
		orderDto.setAmountPayable(order.getAmountPayable());
		orderDto.setStatus(order.getStatus());
		orderDto.setIsOrderChanged(false);
		
		// getting order details list
		List<InventoryData> invData = new ArrayList<>();
		List<OrderDetails> orderDetails = orderDetailsRepository.giveOrderDetails(order.getOrderNo());
		for (OrderDetails orderDetail : orderDetails) {
			Inventory inv = inventoryRepository.getInventoryFromTable(orderDetail.getItemNumber());
			
				InventoryData inD = new InventoryData();
				inD.setId(orderDetail.getId());
				inD.setItemName(orderDetail.getItemName());
				inD.setItemNumber(orderDetail.getItemNumber());
				if(inv!=null)
				{
					inD.setStock(inv.getStock());
					if(inv.getStock() <= inv.getLowStockIndicator())
						inD.setLowStockIndicator(true);
					else
						inD.setLowStockIndicator(false);
					
					inD.setMonthlyQuotaPerUser(inv.getMonthlyQuotaPerUser());
					inD.setToBeSoldOneItemPerOrder(inv.isOnePerItem());
				}
				
				
				inD.setNoOfItemsOrderded(orderDetail.getQuantity());
				inD.setPrice(orderDetail.getPrice());
				
				invData.add(inD);
			
		}
		
		orderDto.setInventoryDatas(invData); 
		
		//orderDtos.add(orderDto);
	return orderDto;
	}

	@Override
	public List<OrderDto> getUpdatedData() throws Exception {
		LOGGER.info("OrderService to get orders to check changed status");
		List<OrderDto> orderDtos = null;
		List<Orders> order = ordersRepository.getUpdatedStatusForOrders();
		try {
			orderDtos = getOrderDtoData(order);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return orderDtos;
	}
	
	
	@Override
	public int updateOrderTable(OrderDto dto) throws Exception {
		LOGGER.info("in updateOrderTable");
		String updatedStatus = null;
		String updatedCancelBy = null;
		int flag = 0;
		try {
			if(Constants.STATUS_INITIATED.equalsIgnoreCase(dto.getStatus())  ||  Constants.STATUS_READY_TO_DELIVER.equalsIgnoreCase(dto.getStatus())) {
				updatedCancelBy = dto.getCustomer().getRole().getName().toUpperCase() +"_"+dto.getCustomer().getFirstName().toUpperCase();
				updatedStatus = Constants.STATUS_CANCEL_BY + dto.getCustomer().getRole().getName().toUpperCase();
				flag = 1;
			}
			if(flag == 1)
				ordersRepository.updateOrderTable(dto.getOrder_no(),updatedStatus,updatedCancelBy,dto.getCancelRemark());
		}
		catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
		}
		if(flag == 1)
			return 1;
		return 0;
	}

	@Override
	public void updateInventoryTable(int orderNo) throws Exception {
		LOGGER.info("in updateInventoryTable");
		List<OrderDetails> orderDetails = orderDetailsRepository.giveOrderDetails(orderNo);
		orderDetailsRepository.updateOrderDetailsActive(orderNo);
		try {
			for(OrderDetails orderDetail : orderDetails) {
				String itmNumber = orderDetail.getItemNumber();
				int stock = orderDetail.getQuantity();
				inventoryRepository.updateInventoryTable(itmNumber,stock);
			}
		}
		catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
		return;
	}
	
	public int checkFun(String time1,String time2) throws Exception {
		try {
			String[] part1 = time1.split("\\:");
			String[] part2 = time2.split("\\:");
			if( Integer.parseInt(part1[0]) < Integer.parseInt(part2[0]))
				return 1;
			if( Integer.parseInt(part1[0]) == Integer.parseInt(part2[0]))
				if(Integer.parseInt(part1[1]) < Integer.parseInt(part2[1]))
					return 1;
			return 0;
		}
		catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
		}					
	}

	@Override
	public void updateAvailCountTimeSlot(int storeId, Date date, String tm) throws Exception {
		LOGGER.info("in updateAvailCountTimeSlot");
		try {
			Date dateobj = new Date();
			String pattern1 = "HH:mm";
			SimpleDateFormat df = new SimpleDateFormat(pattern1);
			if(date.compareTo(dateobj) > 0)
				storeTimeslotsRepository.updateAvailCountTimeSlot(storeId,tm,date);
			else if(date.compareTo(dateobj) == 0 && checkFun(df.format(dateobj),tm) == 1)
				storeTimeslotsRepository.updateAvailCountTimeSlot(storeId,tm,date);
		}
		catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
		return;	
	}
	
	private List<OrderDto> getOrderDtoData(List<Orders> order) {
		List<OrderDto> orderDtos = new ArrayList<>();
		InventoryUtil util = new InventoryUtil();
		for (Orders dto : order) {
			OrderDto orderDto = new OrderDto();
			UserDto customer = new UserDto();
			RoleDto role = new RoleDto();
			role.setId(dto.getCustomer().getRole().getId());
			customer.setRole(role);
			customer.setId(dto.getCustomer().getId());
			customer.setFirstName(dto.getCustomer().getFirstName());
			customer.setLastName(dto.getCustomer().getLastName());
			customer.setMobileNumber(dto.getCustomer().getMobileNumber());
			orderDto.setCustomer(customer);
			StoreDto st = new StoreDto();
			st.setId(dto.getStore().getId());
			orderDto.setStore(st);
			orderDto.setDeliveryDate(dto.getDeliveryDate());
			orderDto.setOrder_no(dto.getOrderNo());
			/*orderDto.setSlotFrom(dto.getSlotFrom());
			orderDto.setSlotTo(dto.getSlotTo());*/
			orderDto.setSlotFrom(util.convertDateTo12HrsTime(dto.getSlotFrom(), null));
			orderDto.setSlotTo(util.convertDateTo12HrsTime(null,dto.getSlotTo()));
			orderDto.setAmountPayable(dto.getAmountPayable());
			orderDto.setStatus(dto.getStatus());

			// getting order details list
			List<InventoryData> invData = new ArrayList<>();
			List<OrderDetails> orderDetails = orderDetailsRepository.giveOrderDetails(dto.getOrderNo());
			for (OrderDetails orderDetail : orderDetails) {
				Inventory inv = inventoryRepository.getInventoryFromTable(orderDetail.getItemNumber());
				InventoryData inD = new InventoryData();
				inD.setId(orderDetail.getId());
				inD.setItemName(orderDetail.getItemName());
				inD.setItemNumber(orderDetail.getItemNumber());
				inD.setStock(inv.getStock());
				if (inv.getStock() <= inv.getLowStockIndicator())
					inD.setLowStockIndicator(true);
				else
					inD.setLowStockIndicator(false);
				inD.setMonthlyQuotaPerUser(inv.getMonthlyQuotaPerUser());
				inD.setNoOfItemsOrderded(orderDetail.getQuantity());
				inD.setPrice(orderDetail.getPrice());
				invData.add(inD);
			}
			orderDto.setInventoryDatas(invData);
			orderDtos.add(orderDto);
		}
		return orderDtos;
	}
	
	//updating ready and pending order list before today's date
	@Override
	public List<Orders> updateReadyPendingOrdersList() throws Exception{
		// TODO Auto-generated method stub
		LOGGER.info("OrderService to get READY & PENDING orders called");
		
//		List<Orders> orders = ordersRepository.findByStatusIn(
//				Arrays.asList("READY_TO_DELIVER", "INITIATED"));
		
		List<Orders> pendingReadyOrders = ordersRepository.updateReadyPendingOrders();
		for (Orders orders : pendingReadyOrders) {
			System.out.println("Orders--- " + orders.getOrderNo());
		}
		List<Orders> returnPendingReadyOrders = new ArrayList<Orders>();
		
		try {
			for (Orders orders : pendingReadyOrders) {
				orders.setStatus("LAPSED");
				ordersRepository.save(orders);
				returnPendingReadyOrders.add(orders);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return returnPendingReadyOrders;
	}

	@Override
	public List<Orders> getOrderByCustomerId(Integer id) throws Exception{
		// TODO Auto-generated method stub
		List<Orders> getOrderByCustomerId;
		try {
			getOrderByCustomerId = ordersRepository.getOrderByCustomerId(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return getOrderByCustomerId;
	}
	
	public boolean isAggregatorAvailable(@Valid OrderDto orderData, int id) {
		List<Orders> orders = ordersRepository.findOrdersForAggregator(id);

		boolean flag = true;
		if (orders.size() > 0) {
			for (Orders order : orders) {

				if (orderData.getDeliveryDate().compareTo(order.getDeliveryDate()) == 0
						&& order.getSlotFrom().equalsIgnoreCase(orderData.getSlotFrom())
						&& order.getSlotTo().equalsIgnoreCase(order.getSlotTo())) {

					flag = false;
					break;
				}
			}
		}

		return flag;
	}

	@Override
	public Orders saveWithAggregator(@Valid OrderDto orderData) {
		Orders order = findByOrderNo(orderData.getOrder_no());
		order.setAggregator(userUtil.toEntity(orderData.getAggregator()));
		return ordersRepository.save(order);
	}

	@Override
	public List<User> getAggregatorForStore(@Valid OrderDto orderData) {
		List<User> aggregatorForStore = userRepository.findAggregatorsForStore(orderData.getStore().getId());

		return aggregatorForStore;
	}
}