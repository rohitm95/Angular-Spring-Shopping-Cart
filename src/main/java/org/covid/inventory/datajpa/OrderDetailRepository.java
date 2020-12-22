package org.covid.inventory.datajpa;

import java.util.List;

import org.covid.inventory.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails, Integer> {
	
	@Query("select o from OrderDetails o where o.orderNo = ?1")
	public List<OrderDetails> giveOrderDetails(int orderId);
	
	@Modifying
	@Transactional
	@Query("update OrderDetails o set o.isActive=0 where o.orderNo=?1")
	public void updateOrderDetailsActive(int orderId);
	
	@Modifying
	@Transactional
	@Query("update OrderDetails o set o.amount=?1*?2 , o.quantity =?2 where o.orderNo=?3 and o.itemNumber=?4")
	public void updateOrderDetails(Double price, Integer quantity, Integer order_no, String itemNumber);
	
}