package org.covid.inventory.datajpa;

import org.covid.inventory.entity.Store;
import org.covid.inventory.entity.StoreHoliday;
import org.covid.inventory.entity.StoreTimings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface StoreRepository extends JpaRepository<Store, Integer> {

    @Query("SELECT s FROM Store s WHERE UPPER(s.id) = ?1")
    Store getStoreById(int storeId);
    
    @Query("select s from Store s where s.storeName = ?1")
    Store getStoreByName(String storeName);
}
