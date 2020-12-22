package org.covid.inventory.service;

import java.util.List;

import org.covid.inventory.entity.Store;
import org.springframework.stereotype.Service;

@Service
public interface StoreService {
	
	List<Store> findAll();
	
	Store findById(Integer id);

    public Store save(Store store);

    public Store update(Store store);
    
    Boolean changeActiveStatus(Integer id, Boolean active);
    
    Store getStoreByName(String storeName);
}
