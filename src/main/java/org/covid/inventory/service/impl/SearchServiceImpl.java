package org.covid.inventory.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.covid.inventory.datajpa.InventoryRepository;
import org.covid.inventory.datajpa.OrderDetailRepository;
import org.covid.inventory.datajpa.SearchRepository;
import org.covid.inventory.datajpa.UserRepository;
import org.covid.inventory.entity.Inventory;
import org.covid.inventory.entity.OrderDetails;
import org.covid.inventory.entity.Orders;
import org.covid.inventory.model.InventoryData;
import org.covid.inventory.model.OrderDto;
import org.covid.inventory.model.RoleDto;
import org.covid.inventory.model.SearchDto;
import org.covid.inventory.model.StoreDto;
import org.covid.inventory.model.UserDto;
import org.covid.inventory.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderDetailRepository orderDetailsRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private SearchRepository searchRepository;

	@Override
	public List<OrderDto> searchOrderData(String fname, String orderNo, String dDate, String tSlot) {
		List<OrderDto> orderDto = null;
		String qry = "SELECT * FROM orders WHERE";
		String res = "SELECT * FROM orders WHERE";
		SearchDto sDto = null;
		sDto = getSearchDto(fname, orderNo, dDate, tSlot);
		
		if (sDto != null) {
			if (sDto.getCustName() != null && !sDto.getCustName().equals("")) {
				List<Integer> custIds = userRepository.findUserIdByFirstname(sDto.getCustName());
				if (custIds != null && !custIds.isEmpty()) {
					String strId = "";
					for (Integer cId : custIds) {
						strId += cId.toString() + ",";
					}
					if (strId != null && strId.length() > 0 && strId.endsWith(",")) {
						strId = strId.substring(0, strId.length() - 1);
					}
					qry = qry + " customer_id IN(" + strId + ")";
				}
			}
			if (sDto.getOrderId() != null && sDto.getOrderId() > 0) {
				if (sDto.getCustName() != null && !sDto.getCustName().equals("")) {
					qry = qry + " AND order_no=" + sDto.getOrderId();
				} else {
					qry = qry + " order_no=" + sDto.getOrderId();
				}
			}
			if (sDto.getDeliveryDate() != null) {
				String fDate = updateSlotCounter(sDto);
				if ((sDto.getCustName() != null && !sDto.getCustName().equals("")) || (sDto.getOrderId() != null)) {
					qry = qry + " AND DATE_FORMAT(delivery_date,'%m/%d/%Y')=" + "'" + fDate + "'";
				} else {
					qry = qry + " DATE_FORMAT(delivery_date,'%m/%d/%Y')=" + "'" + fDate + "'";
				}
			}
			if (sDto.getTimeSlotFrom() != null && !sDto.getTimeSlotFrom().equals("")) {
				if ((sDto.getCustName() != null && !sDto.getCustName().equals("")) || sDto.getOrderId() != null
						|| sDto.getDeliveryDate() != null) {
					qry = qry + " AND slot_from=" + "'" + sDto.getTimeSlotFrom() + "'";
				} else {
					qry = qry + " slot_from=" + "'" + sDto.getTimeSlotFrom() + "'";
				}
			}
			if (!qry.equals(res)) {
				List<Orders> listOrder = searchRepository.getSearchResultForDynamicQuery(qry);
				orderDto = getOrderDtoData(listOrder);
			}
		}
		return orderDto;
	}

	private List<OrderDto> getOrderDtoData(List<Orders> order) {
		List<OrderDto> orderDtos = new ArrayList<>();
		for (Orders dto : order) {
			OrderDto orderDto = new OrderDto();
			UserDto customer = new UserDto();
			RoleDto role = new RoleDto();
			role.setId(dto.getCustomer().getRole().getId());
			role.setName(dto.getCustomer().getRole().getName());
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
			orderDto.setSlotFrom(dto.getSlotFrom());
			orderDto.setSlotTo(dto.getSlotTo());
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

	private String updateSlotCounter(SearchDto orders) {
		String formatedDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
			Calendar cal = Calendar.getInstance();
			cal.setTime(orders.getDeliveryDate());
			formatedDate = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
			Date date = formatter.parse(formatedDate);
			formatedDate = formatter.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatedDate;
	}
	
	private SearchDto getSearchDto(String fname, String orderNo, String dDate, String tSlot) {
		SearchDto sDto = new SearchDto();
		/*sDto.setCustName(fname);
		sDto.setOrderId(orderNo);
		sDto.setDeliveryDate(dDate);
		sDto.setTimeSlotFrom(tSlot);*/
		if (fname.equals("null") || fname==null) {
			sDto.setCustName(null);
		} else {
			sDto.setCustName(fname);
			
		}

		if (orderNo.equals("null") || orderNo==null) {
			sDto.setOrderId(null);
		} else {
			sDto.setOrderId(Integer.valueOf(orderNo));
			
		}

		if (dDate.equals("null") || dDate==null) {
			sDto.setDeliveryDate(null);
		} else {
			Date dd = new Date(Long.valueOf(dDate));
			sDto.setDeliveryDate(dd);
			
		}

		if (tSlot.equals("null") || tSlot==null) {
			sDto.setTimeSlotFrom(null);
		} else {
			sDto.setTimeSlotFrom(tSlot);
		}
		return sDto;
	}
}
