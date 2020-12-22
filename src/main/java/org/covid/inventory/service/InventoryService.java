package org.covid.inventory.service;

import java.util.List;

import org.covid.inventory.entity.Inventory;
import org.covid.inventory.entity.Store;
import org.covid.inventory.model.InventoryData;
import org.springframework.web.multipart.MultipartFile;

public interface InventoryService {
	
	public List<InventoryData> getAllInventoryData() throws Exception;

	public List<InventoryData> readInventoryListFromFile(MultipartFile file, String shortUrl, String storeId)
			throws Exception;

	public void insertInventoryData(List<InventoryData> inventoryDatas, Store store);

	public List<InventoryData> getCategoryInventoryData(final List<String> categories) throws Exception;

	public List<InventoryData> getCategoryInventoryData(final List<String> categories, Integer pageNumber, Integer pageSize) throws Exception;
	
	public List<InventoryData> getAllInventoryData(Integer pageNumber, Integer pageSize);
	
	public Inventory save(InventoryData inventoryData);

	public Inventory update(int id, InventoryData inventoryData);

	public void deleteItemById(int id);
	
	public List<String> getGroupsFromInventory() throws Exception;
	
	public List<String> getCategoriesFromInventory() throws Exception;
	
	public List<InventoryData> searchItem(String itemName) throws Exception;
	
	public List<InventoryData> searchItem(List<String> categories, String itemName) throws Exception;

}
