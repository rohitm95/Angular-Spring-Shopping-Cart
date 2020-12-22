package org.covid.inventory.datajpa;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.covid.inventory.entity.Inventory;
import org.covid.inventory.model.InventoryData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE  Inventory inv SET inv.stock = ?1, inv.store.id=1 where inv.itemNumber =?2")
	void updateInventoryStock(Integer stock, String itemNumber);

	@Modifying
	@Transactional
	@Query("update Inventory i set i.store.id=1, i.stock = i.stock + ?2 where i.itemNumber = ?1")
	void updateInventoryTable(String itmNumber, int stock);

	@Query("select i from Inventory i where i.itemNumber = ?1 and i.store.id = 1")
	Inventory getInventoryFromTable(String itemNumber);

	@Query("SELECT i FROM Inventory i WHERE i.itemCategory IN :categories")
	List<Inventory> findInventoryByCategory(@Param("categories") List<String> categories);

	@Query("SELECT i FROM Inventory i WHERE i.itemCategory IN :categories")
	Page<Inventory> findPagedInventoryByCategory(@Param("categories") List<String> categories, Pageable paging);

	@Query(value = "SELECT DISTINCT group_type FROM inventory", nativeQuery = true)
	List<String> getGroupsFromInventory();

	@Query(value = "SELECT DISTINCT item_category FROM inventory", nativeQuery = true)
	List<String> getCategoriesFromInventory();

	@Query("SELECT i FROM Inventory i WHERE i.itemName like %?1%")
	List<Inventory> searchItem(String itemName);

	@Query("SELECT i FROM Inventory i WHERE i.itemCategory IN :categories AND i.itemName like %:itemName%")
	List<Inventory> searchItem(@Param("categories") List<String> categories, String itemName);

	@Query("SELECT i.itemEntryDate FROM Inventory i  WHERE i.itemNumber=?1")
	Date checkItemExist(String itemNumber);

	
}