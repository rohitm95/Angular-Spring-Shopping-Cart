package org.covid.inventory.datajpa;

import java.util.ArrayList;
import java.util.Date;

import javax.transaction.Transactional;

import org.covid.inventory.entity.StoreTimeslots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreTimeslotsRepository extends JpaRepository<StoreTimeslots, Integer> {
	
	@Query("SELECT ts FROM StoreTimeslots ts  where (ts.availableCount > 0 and ts.slotDate = CURDATE() and ts.slotFrom >= DATE_FORMAT(NOW(),'%H:%i') and DATE_FORMAT(NOW(), '%H:%i') <= ts.slotTo) OR (ts.availableCount > 0 and ts.slotDate >= NOW())")
	ArrayList<StoreTimeslots> findAllStoreTimeslotsByAvailableCount();
	
	@Modifying
	@Transactional
	@Query("UPDATE  StoreTimeslots ts SET ts.availableCount = ?1 where DATE_FORMAT(SLOT_DATE,'%m/%d/%Y') = ?2 and ts.slotFrom =?3 and ts.slotTo =?4 and ts.storeId =?5")
	void updateStoreTimeslotsByAvailableCount(Integer availableCount,String formatedDate,
					String slotFrom,String slotTo,Integer storeId);
	
	@Query("SELECT ts.availableCount FROM StoreTimeslots ts  where  DATE_FORMAT(SLOT_DATE,'%m/%d/%Y') = ?1 and ts.slotFrom =?2 and ts.slotTo =?3 and ts.storeId =?4")
	Integer findAvailableCountFromStoreTimeslotsByDate(String formatedDate,
			String slotFrom,String slotTo,Integer storeId);
			
	@Modifying
	@Transactional
	@Query("update StoreTimeslots ts set ts.availableCount= ts.availableCount + 1 where ts.storeId=?1 and ts.slotFrom = ?2 and DATE(?3) = DATE(ts.slotDate)")
	void updateAvailCountTimeSlot(int storeId, String tm, Date date1);
}