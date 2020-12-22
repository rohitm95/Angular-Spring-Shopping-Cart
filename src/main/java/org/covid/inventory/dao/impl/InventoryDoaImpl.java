package org.covid.inventory.dao.impl;

import java.util.Date;
import java.util.List;

import org.covid.inventory.datajpa.InventoryRepository;
import org.covid.inventory.entity.Inventory;
import org.covid.inventory.model.InventoryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class InventoryDoaImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryDoaImpl.class);
	@Autowired
	private InventoryRepository inventoryRepository;

	public List<Inventory> getInventoryDatas() {
		List<Inventory> results = inventoryRepository.findAll();
		return results;

	}

	public void insertInventoryData(List<Inventory> inventoryData) {
		inventoryRepository.saveAll(inventoryData);
	}

	public List<Inventory> getInventoryDataByCategory(List<String> categories) {
		LOGGER.debug("In getInventoryDataByCategory method.....");
		List<Inventory> results = inventoryRepository.findInventoryByCategory(categories);
		if (results != null && results.size() > 0) {
			return results;
		}

		return null;
	}

	public Page<Inventory> getPagedInventoryDatas(Pageable paging) {
		return inventoryRepository.findAll(paging);
	}

	public Page<Inventory> getPagedInventoryDataByCategory(List<String> categories, Pageable paging) {
		LOGGER.debug("In getInventoryDataByCategory method.....");
		Page<Inventory> results = inventoryRepository.findPagedInventoryByCategory(categories, paging);
		if (results != null && results.getSize() > 0)
			return results;
		return null;
	}

	public Inventory save(Inventory inventory) {
		return inventoryRepository.save(inventory);
	}

	public Inventory findById(int id) {
		return inventoryRepository.findById(id).get();
	}

	public boolean existsById(int id) {
		return inventoryRepository.existsById(id);
	}

	public void deleteUserById(int id) {
		inventoryRepository.deleteById(id);
	}

	public List<String> getGroupsFromInventory() {
		List<String> results = inventoryRepository.getGroupsFromInventory();
		return results;
	}

	public List<String> getCategoriesFromInventory() {
		List<String> results = inventoryRepository.getCategoriesFromInventory();
		return results;
	}

	public List<Inventory> searchItem(String itemName) {
		// TODO Auto-generated method stub
		List<Inventory> results = inventoryRepository.searchItem(itemName);
		return results;
	}

	public List<Inventory> searchItem(List<String> categories, String itemName) {
		// TODO Auto-generated method stub
		List<Inventory> results = inventoryRepository.searchItem(categories, itemName);
		return results;
	}

	public Date checkItemExist(String itemNumber) {
		return inventoryRepository.checkItemExist(itemNumber);
	}

		
}
