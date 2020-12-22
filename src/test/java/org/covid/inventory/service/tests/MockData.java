package org.covid.inventory.service.tests;

import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.covid.inventory.entity.BreakTimings;
import org.covid.inventory.entity.Inventory;
import org.covid.inventory.entity.OrderDetails;
import org.covid.inventory.entity.Orders;
import org.covid.inventory.entity.Role;
import org.covid.inventory.entity.User;
import org.covid.inventory.model.OrderDetailsDto;
import org.covid.inventory.model.OrderDto;
import org.covid.inventory.model.RoleDto;
import org.covid.inventory.model.UserDto;
import org.covid.inventory.entity.Store;
import org.covid.inventory.entity.StoreHoliday;
import org.covid.inventory.entity.StoreTimings;

public class MockData {
	static Store store = new Store();
	static Role role = new Role();
	static RoleDto roleDto = new RoleDto();
	static User teacher = new User();
	static User user = new User();
	static UserDto userDto = new UserDto();

	private static User getUser(Integer id) {

		teacher.setId(id);
		return teacher;
	}

	public static Date getDate() {
		return new Date(System.currentTimeMillis());
	}

	public static Role getRole(int i) {
		role.setName("admin");
		role.setId(1);
		return role;
	}

	public static User getUser(int i) {
		user.setUsername("test");
		user.setPassword("test");
//		user.setRole(new Role() {
//			{
//				setName("test");
//				setId(1);
//			}
//		});
		user.setRole(getRole(1));
		user.setEmailId("email");
		user.setMobileNumber("mobile");
	
		return user;
	}

	public static Store getStore(int i) {

		store.setActive(true);
		store.setCreatedBy("admin");
		store.setStoreName("Kamal");
		store.setId(i);
		store.setDeliveryInSlot(1);
		store.setModifiedBy(null);
		store.setSlotDuration(30);
		Set<StoreTimings> storeTimings = new HashSet<StoreTimings>();
		StoreTimings st = new StoreTimings();
		st.setId(i);
		st.setDay("monday");
		st.setDeliveryStartAt("12:00PM");
		st.setDeliveryEndAt("1:00PM");
		st.setStore(store);
		st.setWeaklyOff(true);
		storeTimings.add(st);
		store.setStoreTimings(storeTimings);
		Set<BreakTimings> breakTimings = new HashSet<BreakTimings>();
		BreakTimings bt = new BreakTimings();
		bt.setId(1);
		bt.setStore(store);
		bt.setBreakType("lunch");
		bt.setBreakFrom("1:15PM");
		bt.setBreakTo("2:00PM");
		breakTimings.add(bt);
		store.setBreakTimings(breakTimings);
		Set<StoreHoliday> storeHoliday = new HashSet<StoreHoliday>();
		StoreHoliday sh = new StoreHoliday();
		sh.setId(1);
		sh.setDate(getDate());
		sh.setStore(store);
		sh.setHoliday("holiday");
		storeHoliday.add(sh);
		store.setStoreHolidays(storeHoliday);
		return store;

	}

	public static Orders getOrder(int i) {
		Orders order = new Orders();
		order.setActive(true);
		order.setAmountPayable(1200.00);
		order.setCancelBy(null);
		order.setCancelRemark("");
		user.setFirstName("user1");
		order.setCustomer(user);
		
		order.setDeliveryDate(new Date(System.currentTimeMillis()));
		order.setOrderNo(i);
		order.setSlotFrom("12:00PM");
		order.setSlotTo("1:00PM");
		order.setStatus("Initiated");
		order.setStore(store);
		return order;
	}
	public static UserDto getUserDto(int i) {
		UserDto userDto = new UserDto();
		userDto.setActive(true);
		userDto.setAFD_purchase_limit(1200000.00);
		userDto.setEmailId("a@gmail.com");
		userDto.setFirstName("test");
		userDto.setGender("test");
		userDto.setId(i);
		userDto.setRole(roleDto);
		userDto.setLastName("test");
		userDto.setRole(roleDto);
		userDto.setStore(store);
		return userDto;
	}
	public static OrderDto getOrderDto(int i) {
		OrderDto orderDto = new OrderDto();
		orderDto.setAmountPayable(1200.00);
		orderDto.setCancelBy(null);
		orderDto.setCancelRemark("");
		orderDto.setDeliveryDate(new Date(System.currentTimeMillis()));
		orderDto.setSlotFrom("12:00PM");
		orderDto.setSlotTo("1:00PM");
		orderDto.setStatus("Initiated");
		orderDto.setCustomer(userDto);
		orderDto.setCustomer(userDto);
		return orderDto;
	}
	
	public static OrderDetails getOrderDetails(int i) {
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setActive(true);
		orderDetails.setAmount(1200);
		orderDetails.setItemName("pen");
		orderDetails.setItemNumber("123");
		orderDetails.setOrderNo(1);
		orderDetails.setPrice(1200);
		orderDetails.setQuantity(1);
		return orderDetails;


	}
	public static OrderDetailsDto getOrderDetailsDto(int i) {
		OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
		orderDetailsDto.setAmount(1200.00);
		orderDetailsDto.setItem_name("pen");
		orderDetailsDto.setOrder_no(1);
		orderDetailsDto.setPrice(1200.00);
		orderDetailsDto.setQuantity(1);
		return orderDetailsDto;


	}

	public static Inventory getInventory(int i) {
		Inventory inv = new Inventory();
		inv.setActive(true);
		inv.setId(i);
		inv.setItemName("pen");
		inv.setItemNumber("123");
		inv.setGroup("A");
		inv.setItemType("Household");
		inv.setLowStockIndicator(1);
		inv.setMonthlyQuotaPerUser("5");
		inv.setOnePerItem(true);
		inv.setPrice(1200.00);
		inv.setStock(50);
		inv.setStore(store);
		inv.setWeightVolumePerItem("1");

		return inv;
	}
	public static RoleDto getRoleDto(int i) {
		roleDto.setName("admin");
		roleDto.setId(1);
		return roleDto;
	}
}
