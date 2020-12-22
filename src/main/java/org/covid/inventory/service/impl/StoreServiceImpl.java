package org.covid.inventory.service.impl;

import org.covid.inventory.datajpa.StoreRepository;
import org.covid.inventory.entity.Store;
import org.covid.inventory.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class StoreServiceImpl implements StoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public Store save(Store store) {
        setStoreId(store);
        return storeRepository.save(store);
    }

    private void setStoreId(Store store) {
        store.getStoreTimings().stream().forEach(s -> s.setStore(store));
        store.getBreakTimings().stream().forEach(b -> b.setStore(store));
        store.getStoreHolidays().stream().forEach(h -> h.setStore(store));
    }

    @Override
    @Transactional
    public Store update(Store store) {
        setStoreId(store);
        entityManager.merge(store);
       Store store1 = entityManager.find(Store.class, store.getId());
        store1.getStoreHolidays();
        store1.getBreakTimings();
        store1.getStoreTimings();
        return store1;
    }

	@Override
	public Store findById(Integer id) {
		return storeRepository.findById(id).orElse(null);
	}
	
	@Override
	public Store getStoreByName(String storeName) {
		return storeRepository.getStoreByName(storeName);
	}

	@Override
	public List<Store> findAll() {
		return storeRepository.findAll();
	}

	@Override
	public Boolean changeActiveStatus(Integer id, Boolean active) {
		Optional<Store> store = storeRepository.findById(id);
		if(store.isPresent()) {
			store.get().setActive(active);
			storeRepository.save(store.get());
			return true;
		}
		return false;
	}
}
