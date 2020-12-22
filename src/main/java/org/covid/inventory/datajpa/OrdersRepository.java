package org.covid.inventory.datajpa;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.covid.inventory.entity.Orders;
import org.covid.inventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	
	@Query(value = "SELECT * FROM orders WHERE status IN ('Pending','Initiated')", nativeQuery = true)
	List<Orders> getUpdatedStatusForOrders();
	
	@Modifying
	@Transactional
	@Query("UPDATE  Orders ordr SET ordr.status = ?1 where ordr.orderNo = ?2")
	void updateOrderStatusAsPerUserRole(String status,Integer orderNo);
	
	@Modifying
	@Transactional
	@Query("update Orders o set o.isActive=0,o.status=?2,o.cancelBy=?3,o.cancelRemark=?4 where o.orderNo=?1")
	void updateOrderTable(int orderNo, String status1, String updatedCancelBy, String cancelRemark);
	
	@Modifying
	@Transactional
	@Query("update Orders o set o.amountPayable=?1 where o.orderNo=?2")
	void updateAmountPayble(Double amountPayable, Integer orderNo);

	@Query(value = "SELECT ordr FROM Orders ordr ORDER BY ordr.deliveryDate DESC")
	List<Orders> getOrders();
	
	//get ready, pending orders
	@Query(value = "select ordr from Orders ordr where ordr.status in ('READY_TO_DELIVER', 'INITIATED') and ordr.deliveryDate < CURRENT_DATE()")
	List<Orders> updateReadyPendingOrders();
	
	List<Orders> findByStatusIn(List<String> status);
	
	//get Orders by Customer ID
	@Query(value = "select ordr from Orders ordr where ordr.customer.id = ?1")
	List<Orders> getOrderByCustomerId(Integer id);
	
	List<Orders> findByAmountPayable(Double amountPayable);
	
	@Query(value="select * from orders where status in :status",nativeQuery = true)
	List<Orders> findAllByStatus(List<String> status);
	
	@Query(value = "SELECT SUM(amountPayable) FROM Orders ordr WHERE ordr.customer=?1 and "
			+ "ordr.deliveryDate BETWEEN ?2 and ?3")
	Double getOrderTotalBetweenDates(User customer, Date date1, Date date2);

	@Query(value="SELECT * FROM Orders where aggregator_id=?1", nativeQuery = true)
	List<Orders> findOrdersForAggregator(Integer id);
}