package org.covid.inventory.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.covid.inventory.datajpa.InventoryRepository;
import org.covid.inventory.datajpa.ItemCategoryRepository;
import org.covid.inventory.datajpa.OrderDetailRepository;
import org.covid.inventory.datajpa.OrdersRepository;
import org.covid.inventory.datajpa.RoleRepository;
import org.covid.inventory.datajpa.StoreTimeslotsRepository;
import org.covid.inventory.datajpa.UserRepository;
import org.covid.inventory.entity.Inventory;
import org.covid.inventory.entity.ItemCategory;
import org.covid.inventory.entity.OrderDetails;
import org.covid.inventory.entity.Orders;
import org.covid.inventory.entity.Role;
import org.covid.inventory.entity.StoreTimeslots;
import org.covid.inventory.entity.User;
import org.covid.inventory.model.InventoryData;
import org.covid.inventory.model.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TimeSlotManagerDoaImpl {

	@Autowired
	private StoreTimeslotsRepository storeTimeslotsRepository;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	InventoryDoaImpl inventoryDoaImpl;

	@Autowired
	private ItemCategoryRepository itemCategoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public List<StoreTimeslots> getExistingTimeSLots() {
		List<StoreTimeslots> results = storeTimeslotsRepository.findAllStoreTimeslotsByAvailableCount();
		return results;

	}

	public Integer bookOrderWithTimeSlot(Orders orders) {
		ordersRepository.save(orders);
		return orders.getOrderNo();
	}

	public void bookOrderDetailsWithTimeSlot(List<OrderDetails> orderDetails) {
		orderDetailRepository.saveAll(orderDetails);
	}

	public void updateSlotCounter(Orders orders) throws Exception {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
			Calendar cal = Calendar.getInstance();
			cal.setTime(orders.getDeliveryDate());
			String formatedDate = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/"
					+ cal.get(Calendar.YEAR);
			Date date = formatter.parse(formatedDate);
			formatedDate = formatter.format(date);
			Integer availableTimeCounter = getAvalibleCountForSlot(formatedDate, orders);
			storeTimeslotsRepository.updateStoreTimeslotsByAvailableCount(availableTimeCounter - 1, formatedDate,
					orders.getSlotFrom(), orders.getSlotTo(), orders.getStore().getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}

	}

	public void updateItemInfo(Orders orders, OrderDto order) {
		List<Inventory> inventories = new ArrayList<Inventory>();
		List<ItemCategory> categories = itemCategoryRepository.findAll();
		Map<String, Integer> categoryMap = new HashMap<>();
		for (ItemCategory categorie : categories) {
			categoryMap.put(categorie.getItemCategoryName(), categorie.getId());
		}
		for (InventoryData entities : order.getInventoryDatas()) {
			Inventory inventory = new Inventory();
			inventory.setItemName(entities.getItemName());
			inventory.setItemNumber(entities.getItemNumber());
			inventory.setItemType(entities.getItemType());
			inventory.setLowStockIndicator(1);
			if (!entities.getMonthlyQuotaPerUser().equals("NO LIMIT")) {
				Integer stock = Integer.parseInt(entities.getMonthlyQuotaPerUser()) - entities.getNoOfItemsOrderded();
				inventory.setMonthlyQuotaPerUser(stock.toString());
			} else
				inventory.setMonthlyQuotaPerUser("NO LIMIT");
			inventory.setOnePerItem(entities.getToBeSoldOneItemPerOrder());
			inventory.setPrice(entities.getPrice());
			inventory.setStock(entities.getStock() - entities.getNoOfItemsOrderded());
			if (!entities.getYearlyQuotaPerUser().equals("NO LIMIT")) {
				Integer yearlyQuotaPerUser = Integer.parseInt(entities.getYearlyQuotaPerUser())
						- entities.getNoOfItemsOrderded();
				inventory.setYearlyQuotaPerUser(yearlyQuotaPerUser.toString());
			} else
				inventory.setYearlyQuotaPerUser("NO LIMIT");

			inventory.setId(entities.getId().intValue());
			/*
			 * ItemCategory itemCategory = new ItemCategory();
			 * itemCategory.setId(categoryMap.get(entities.getCategory()));
			 * inventory.setItemCategory(itemCategory);
			 */
			inventory.setItemCategory(entities.getCategory());
			inventory.setWeightVolumePerItem(entities.getVolumePerItem());
			inventory.setGroup(entities.getGroup());
			inventory.setStore(orders.getStore());
			inventory.setImageUrl(entities.getImageUrl());

			inventory.setItemEntryDate(entities.getItemEntryDate());

			inventories.add(inventory);
		}
		inventoryRepository.saveAll(inventories);
	}

	public void changeOrderStatus(Integer orderId, String status) {
		ordersRepository.updateOrderStatusAsPerUserRole(status, orderId);

	}

	private Integer getAvalibleCountForSlot(String formatedDate, Orders orders) {
		return storeTimeslotsRepository.findAvailableCountFromStoreTimeslotsByDate(formatedDate, orders.getSlotFrom(),
				orders.getSlotTo(), orders.getStore().getId());

	}

	public Integer getRoleId(Integer custId) {
		User user = userRepository.findUserById(custId);
		return user != null ? user.getId() : 0;
	}

	public String getRoleName(Integer roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		return role != null ? role.get().getName() : null;
	}

	public boolean updateOrder(Map<String, Integer> inventoryDataToUpdate, OrderDto orderDto,
			Map<String, String> orderDetailDataToUpdate) {
		boolean isOrderUpdated = true;

		try {
			ordersRepository.updateAmountPayble(orderDto.getAmountPayable(), orderDto.getOrder_no());

			for (Entry<String, String> map : orderDetailDataToUpdate.entrySet()) {
				orderDetailRepository.updateOrderDetails(Double.parseDouble(map.getValue().split(",")[0]),
						Integer.parseInt(map.getValue().split(",")[1]), orderDto.getOrder_no(), map.getKey());
			}

			for (Entry<String, Integer> map : inventoryDataToUpdate.entrySet()) {
				inventoryRepository.updateInventoryStock(map.getValue(), map.getKey());
			}

			// return true;
		} catch (Exception e) {
			System.out.println("Exception occured while updating order--?" + orderDto.toString() + " error message-->"
					+ e.getMessage());
			isOrderUpdated = false;

		}
		return isOrderUpdated;
	}

}
