package org.covid.inventory.model;
/*
 * Item Number	
 * Group	Name	 Price 	Stock	Category	Low Stock Indicator	To be Sold one item per order (Yes/No)	Weight/ Volume per Item	Whether In Stock (Yes/No)	Monthly Quota per User per Item	ItemType (AFD/ Non AFD)	Yearly Quota Per User Per Item

 */

public class ChangeOrderDto {
	
	private OrderDto orderDto;
	
	private RoleDto roleDto;

	public OrderDto getOrderDto() {
		return orderDto;
	}

	public void setOrderDto(OrderDto orderDto) {
		this.orderDto = orderDto;
	}

	public RoleDto getRoleDto() {
		return roleDto;
	}

	public void setRoleDto(RoleDto roleDto) {
		this.roleDto = roleDto;
	}
	
}
