package org.covid.inventory.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.covid.inventory.dao.impl.InventoryDoaImpl;
import org.covid.inventory.entity.Inventory;
import org.covid.inventory.entity.Store;
import org.covid.inventory.exceptions.EntityNotFoundException;
import org.covid.inventory.model.InventoryData;
import org.covid.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

@Service
public class InventoryServiceImpl implements InventoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);

	@Autowired
	private InventoryDoaImpl inventoryDoaImpl;

	@Autowired
	private MessageSource messageSource;

	@Override
	public List<InventoryData> getAllInventoryData() throws Exception {
		LOGGER.debug("in getAllInventoryData()....");
		try {

			List<InventoryData> inventoryDatasToSend = new ArrayList<InventoryData>();
			List<Inventory> inventoryData = inventoryDoaImpl.getInventoryDatas();
			for (Inventory inventory : inventoryData) {
				InventoryData data = new InventoryData();
//				data.setCategory(inventory.getItemCategory().getItemCategoryName());
				data.setCategory(inventory.getItemCategory());
				data.setId(Long.parseLong(inventory.getId().toString()));
				data.setGroup(inventory.getGroup());
				data.setItemName(inventory.getItemName());
//				data.setItemNumber(inventory.getItemNumber());
				if (inventory.getItemNumber().contains(".")) {
					String[] itemNumber = inventory.getItemNumber().split("\\.");
					data.setItemNumber(itemNumber[0]);
				} else {
					data.setItemNumber(inventory.getItemNumber());
				}
				data.setMonthlyQuotaPerUser(inventory.getMonthlyQuotaPerUser());
				data.setPrice(inventory.getPrice());
				data.setStock(inventory.getStock());
				data.setToBeSoldOneItemPerOrder(inventory.isOnePerItem());
				// data.setVolumePerItem(inventory.getStock().toString());
				data.setYearlyQuotaPerUser(inventory.getYearlyQuotaPerUser());
				data.setItemType(inventory.getItemType());

				Store store = new Store();
				store = inventory.getStore();
				data.setStore(store);

				if (inventory.getLowStockIndicator() == 1)
					data.setLowStockIndicator(true);
				else
					data.setLowStockIndicator(false);
				data.setImageUrl(inventory.getImageUrl());
				data.setItemEntryDate(inventory.getItemEntryDate());

				Date currDate = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				try {
					Date d1 = sdf.parse(sdf.format(inventory.getItemEntryDate()));
					Date d2 = sdf.parse(sdf.format(currDate));
					long dateDiff = d2.getTime() - d1.getTime();
					long difference_In_Days = (dateDiff / (1000 * 60 * 60 * 24)) % 365;
					if (difference_In_Days <= 5) // new Arrival code
					{
						data.setNewArrival(true);
					} else {
						data.setNewArrival(false);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				inventoryDatasToSend.add(data);
			}
			return inventoryDatasToSend;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

	}

	@Override
	@SuppressWarnings({ "resource", "deprecation" })
	public List<InventoryData> readInventoryListFromFile(MultipartFile file, String shortUrl, String storeId)
			throws Exception {
		LOGGER.debug("in readInventoryListFromFile()....");
		List<InventoryData> inventoryDatas = null;
		String SHEET = "Sheet1";
		try {
			inventoryDatas = new ArrayList<InventoryData>();
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();
			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				Iterator<Cell> cellsInRow = currentRow.iterator();
				InventoryData inventoryData = new InventoryData();
				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						if (currentCell.getCellTypeEnum().equals(CellType.STRING)) {
							inventoryData.setItemNumber(currentCell.getStringCellValue());
						} else {
							inventoryData.setItemNumber(String.valueOf((int) currentCell.getNumericCellValue()));
						}
						Date itemEntryDate = inventoryDoaImpl.checkItemExist(inventoryData.getItemNumber());
						Date currDate = new Date(System.currentTimeMillis());
						if (itemEntryDate == null) {

							inventoryData.setItemEntryDate(currDate);
						} else {
							inventoryData.setItemEntryDate(itemEntryDate);
						}
						break;
					case 1:
						inventoryData.setGroup(currentCell.getStringCellValue());
						break;
					case 2:
						inventoryData.setItemName(currentCell.getStringCellValue());
						break;
					case 3:
						Double price = currentCell.getNumericCellValue();
						inventoryData.setPrice(price);
						break;
					case 4:
						Integer stock = (int) currentCell.getNumericCellValue();
						inventoryData.setStock(stock.intValue());
						break;
					case 5:
						inventoryData.setCategory(currentCell.getStringCellValue());
						break;
					case 6:
						Integer stockIndicator = (int) currentCell.getNumericCellValue();
						if (stockIndicator < 5) {
							inventoryData.setLowStockIndicator(true);
						} else {
							inventoryData.setLowStockIndicator(false);
						}
						break;
					case 7:
						String oneItem = currentCell.getStringCellValue();
						if (oneItem.equalsIgnoreCase("Yes")) {
							inventoryData.setToBeSoldOneItemPerOrder(true);
						} else if (oneItem.equalsIgnoreCase("No")) {
							inventoryData.setToBeSoldOneItemPerOrder(false);
						}
						break;
					case 8:
						String volumePerItem = null;
						if (currentCell.getCellType() == 1) {
							volumePerItem = currentCell.getStringCellValue();
						}
						if (currentCell.getCellType() == 0) {
							Double wightVolume = currentCell.getNumericCellValue();
							volumePerItem = wightVolume.toString();
						}
						inventoryData.setVolumePerItem(volumePerItem);
						break;
					case 9:
//						String inStock = currentCell.getStringCellValue();
						String inStock = null;
						if (currentCell.getCellType() == 1)
							inStock = currentCell.getStringCellValue();
						if (currentCell.getCellType() == 0) {
							Integer inStockNumber = (int) currentCell.getNumericCellValue();
							inStock = inStockNumber.toString();
						}
						break;
					case 10:
						if (currentCell.getCellTypeEnum().equals(CellType.STRING)) {
							if (currentCell.getStringCellValue().equals("")) {
								inventoryData.setMonthlyQuotaPerUser("5");
							} else {
								inventoryData.setMonthlyQuotaPerUser(currentCell.getStringCellValue());
							}

						} else {
							if (currentCell.getNumericCellValue() == 0) {
								inventoryData.setMonthlyQuotaPerUser("5");
							} else {
								inventoryData.setMonthlyQuotaPerUser(
										String.valueOf((int) currentCell.getNumericCellValue()));
							}
						}
						break;
					case 11:
						if (currentCell.getCellTypeEnum().equals(CellType.STRING)) {
							inventoryData.setItemType(currentCell.getStringCellValue());
						} else {
							inventoryData.setItemType(String.valueOf(currentCell.getNumericCellValue()));
						}
						break;
					case 12:
						if (currentCell.getCellTypeEnum().equals(CellType.STRING)) {
							if (currentCell.getStringCellValue().equals("")) {
								inventoryData.setYearlyQuotaPerUser("1");
							} else {
								inventoryData.setYearlyQuotaPerUser(currentCell.getStringCellValue());
							}

						} else {
							if (currentCell.getNumericCellValue() == 0) {
								inventoryData.setYearlyQuotaPerUser("1");
							} else {
								inventoryData
										.setYearlyQuotaPerUser(String.valueOf((int) currentCell.getNumericCellValue()));
							}
						}
						break;
					case 13:
						if (currentCell.getCellTypeEnum().equals(CellType.STRING)) {

							File fl = new File(currentCell.getStringCellValue());

							Path path = Paths.get(currentCell.getStringCellValue());
							String name = fl.getName();
							String originalFileName = fl.getName();
							String contentType = "image/png";
							byte[] content = null;
							try {
								content = Files.readAllBytes(path);
							} catch (final IOException e) {
							}
							MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType,
									content);

							final String UPLOADED_FOLDER =  System.getProperty("user.home") + File.separator+"Images"+File.separator + storeId
									+ File.separator;
							File dir = new File(UPLOADED_FOLDER);
							MultipartFile ImageFile = multipartFile;

							try {
								byte[] bytes = ImageFile.getBytes();

								if (!dir.exists())
									dir.mkdirs();

								File uploadFile = new File(
										dir.getAbsolutePath() + File.separator + ImageFile.getOriginalFilename());
								BufferedOutputStream outputStream = new BufferedOutputStream(
										new FileOutputStream(uploadFile));
								outputStream.write(bytes);
								outputStream.close();

								String imageUrl = shortUrl + "/images/" + storeId + "/"
										+ ImageFile.getOriginalFilename();
								inventoryData.setImageUrl(imageUrl);

							} catch (Exception e) {

							}

						}
						break;
					default:
						break;
					}
					cellIdx++;
				}
				inventoryDatas.add(inventoryData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return inventoryDatas;
	}

	@Override
	public void insertInventoryData(List<InventoryData> inventoryDatas, Store store) {
		LOGGER.debug("in insertInventoryData()....");

		try {

			List<Inventory> inventoryDataExisting = inventoryDoaImpl.getInventoryDatas();
			Map<String, Inventory> inventoryDataMapExisting = new LinkedHashMap<>();
			for (Inventory data : inventoryDataExisting) {
				inventoryDataMapExisting.put(data.getItemNumber(), data);
			}
			Map<String, Inventory> inventoryData = new LinkedHashMap<>();
			for (InventoryData data : inventoryDatas) {

				Inventory inventory = new Inventory();
				/*
				 * ItemCategory itemCategory = new ItemCategory();
				 * itemCategory.setItemCategoryName(data.getCategory());
				 * inventory.setItemCategory(itemCategory);
				 */
				try {

					inventory.setItemCategory(data.getCategory());
					inventory.setGroup(data.getGroup());
					inventory.setItemName(data.getItemName());
					inventory.setItemNumber(data.getItemNumber());
					inventory.setItemType(data.getItemType());
					inventory.setOnePerItem(data.getToBeSoldOneItemPerOrder());
					inventory.setPrice(data.getPrice());
					inventory.setStock(data.getStock());
					inventory.setYearlyQuotaPerUser(data.getYearlyQuotaPerUser());
					inventory.setMonthlyQuotaPerUser(data.getMonthlyQuotaPerUser());
					inventory.setStore(store);
					if (data.getLowStockIndicator())
						inventory.setLowStockIndicator(1);
					else
						inventory.setLowStockIndicator(0);
					inventory.setImageUrl(data.getImageUrl());
					inventory.setItemEntryDate(data.getItemEntryDate());
					inventoryData.put(data.getItemNumber(), inventory);
				} catch (NullPointerException e) {
					LOGGER.debug("There exists blank entry in the file");
				}
			}
			List<Inventory> inventories = new ArrayList<>();

			for (Map.Entry<String, Inventory> entry : inventoryData.entrySet()) {
				if (inventoryDataMapExisting.containsKey(entry.getKey())) {
					Inventory inventory = inventoryDataMapExisting.get(entry.getKey());
					/*
					 * ItemCategory itemCategory = new ItemCategory();
					 * itemCategory.setItemCategoryName(entry.getValue().getItemCategory().
					 * getItemCategoryName()); inventory.setItemCategory(itemCategory);
					 */
					inventory.setItemCategory(entry.getValue().getItemCategory());
					inventory.setGroup(entry.getValue().getGroup());
					inventory.setItemName(entry.getValue().getItemName());
					inventory.setItemNumber(entry.getValue().getItemNumber());
					inventory.setItemType(entry.getValue().getItemType());
					inventory.setOnePerItem(entry.getValue().isOnePerItem());
					inventory.setPrice(entry.getValue().getPrice());
					inventory.setStock(entry.getValue().getStock() + inventory.getStock());
					inventory.setYearlyQuotaPerUser(entry.getValue().getYearlyQuotaPerUser());
					inventory.setMonthlyQuotaPerUser(entry.getValue().getMonthlyQuotaPerUser());
					inventory.setLowStockIndicator(entry.getValue().getLowStockIndicator());
					inventory.setId(inventory.getId());
					inventory.setStore(store);
					inventory.setImageUrl(entry.getValue().getImageUrl());
					inventory.setItemEntryDate(entry.getValue().getItemEntryDate());
					inventories.add(inventory);
				} else {
					inventories.add(entry.getValue());
				}
			}

			inventoryDoaImpl.insertInventoryData(inventories);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<InventoryData> getCategoryInventoryData(List<String> categories) throws Exception {
		LOGGER.debug("in getCategoryInventoryData()....");
		try {
			LOGGER.debug("In getCategoryInventoryData method.....");
			List<InventoryData> inventoryDatasToSend = new ArrayList<InventoryData>();
			List<Inventory> inventoryData;
			inventoryData = inventoryDoaImpl.getInventoryDataByCategory(categories);
			for (Inventory inventory : inventoryData) {
				InventoryData data = new InventoryData();
				data.setCategory(inventory.getItemCategory());
				data.setId(Long.parseLong(inventory.getId().toString()));
				data.setItemName(inventory.getItemName());
//				data.setItemNumber(inventory.getItemNumber());
				if (inventory.getItemNumber().contains(".")) {
					String[] itemNumber = inventory.getItemNumber().split("\\.");
					data.setItemNumber(itemNumber[0]);
				} else {
					data.setItemNumber(inventory.getItemNumber());
				}
				data.setMonthlyQuotaPerUser(inventory.getMonthlyQuotaPerUser());
				data.setPrice(inventory.getPrice());
				data.setStock(inventory.getStock());
				data.setToBeSoldOneItemPerOrder(inventory.isOnePerItem());
				// data.setVolumePerItem(inventory.getStock().toString());
				data.setVolumePerItem(inventory.getWeightVolumePerItem());
				if (inventory.getYearlyQuotaPerUser() != null) {
					data.setYearlyQuotaPerUser(inventory.getYearlyQuotaPerUser());

				} else {
					data.setYearlyQuotaPerUser("1");
				}
				data.setItemType(inventory.getItemType());
				data.setGroup(inventory.getGroup());
				if (inventory.getLowStockIndicator() == 1)
					data.setLowStockIndicator(true);
				else
					data.setLowStockIndicator(false);
				data.setImageUrl(inventory.getImageUrl());
				data.setItemEntryDate(inventory.getItemEntryDate());
				Date currDate = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				try {
					Date d1 = sdf.parse(sdf.format(inventory.getItemEntryDate()));
					Date d2 = sdf.parse(sdf.format(currDate));
					long dateDiff = d2.getTime() - d1.getTime();
					long difference_In_Days = (dateDiff / (1000 * 60 * 60 * 24)) % 365;
					if (difference_In_Days <= 5) // new Arrival code
					{
						data.setNewArrival(true);
					} else {
						data.setNewArrival(false);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				inventoryDatasToSend.add(data);
			}
			return inventoryDatasToSend;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public List<InventoryData> getCategoryInventoryData(List<String> categories, Integer pageNumber, Integer pageSize)
			throws Exception {
		LOGGER.debug("in getCategoryInventoryData()....");
		Pageable paging = PageRequest.of(pageNumber, pageSize);
		try {
			LOGGER.debug("In getCategoryInventoryData method.....");
			List<InventoryData> inventoryDatasToSend = new ArrayList<InventoryData>();
			Page<Inventory> inventoryData;
			inventoryData = inventoryDoaImpl.getPagedInventoryDataByCategory(categories, paging);

			for (Inventory inventory : inventoryData) {
				InventoryData data = new InventoryData();
				data.setCategory(inventory.getItemCategory());
				data.setId(Long.parseLong(inventory.getId().toString()));
				data.setItemName(inventory.getItemName());
//				data.setItemNumber(inventory.getItemNumber());
				if (inventory.getItemNumber().contains(".")) {
					String[] itemNumber = inventory.getItemNumber().split("\\.");
					data.setItemNumber(itemNumber[0]);
				} else {
					data.setItemNumber(inventory.getItemNumber());
				}
				data.setMonthlyQuotaPerUser(inventory.getMonthlyQuotaPerUser());
				data.setPrice(inventory.getPrice());
				data.setStock(inventory.getStock());
				data.setToBeSoldOneItemPerOrder(inventory.isOnePerItem());
				// data.setVolumePerItem(inventory.getStock().toString());
				data.setVolumePerItem(inventory.getWeightVolumePerItem());
				if (inventory.getYearlyQuotaPerUser() != null) {
					data.setYearlyQuotaPerUser(inventory.getYearlyQuotaPerUser());

				} else {
					data.setYearlyQuotaPerUser("1");
				}
				data.setItemType(inventory.getItemType());
				data.setGroup(inventory.getGroup());
				if (inventory.getLowStockIndicator() == 1)
					data.setLowStockIndicator(true);
				else
					data.setLowStockIndicator(false);
				data.setImageUrl(inventory.getImageUrl());
				data.setItemEntryDate(inventory.getItemEntryDate());
				Date currDate = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				try {
					Date d1 = sdf.parse(sdf.format(inventory.getItemEntryDate()));
					Date d2 = sdf.parse(sdf.format(currDate));
					long dateDiff = d2.getTime() - d1.getTime();
					long difference_In_Days = (dateDiff / (1000 * 60 * 60 * 24)) % 365;
					if (difference_In_Days <= 5) // new Arrival code
					{
						data.setNewArrival(true);
					} else {
						data.setNewArrival(false);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				inventoryDatasToSend.add(data);
			}

			Page<InventoryData> pagedResult = new PageImpl<InventoryData>(inventoryDatasToSend);
			return pagedResult.getContent();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public List<InventoryData> getAllInventoryData(Integer pageNumber, Integer pageSize) {
		LOGGER.info("inside getAllInventoryData Paginated");
		Pageable paging = PageRequest.of(pageNumber, pageSize);
		Page<Inventory> pagedInventoryResult = inventoryDoaImpl.getPagedInventoryDatas(paging);
		List<InventoryData> inventoryDatasToSend = new ArrayList<InventoryData>();
		for (Inventory inventory : pagedInventoryResult.getContent()) {
			InventoryData data = new InventoryData();
			data.setCategory(inventory.getItemCategory());
			data.setId(Long.parseLong(inventory.getId().toString()));
			data.setGroup(inventory.getGroup());
			data.setItemName(inventory.getItemName());
//			data.setItemNumber(inventory.getItemNumber());
			if (inventory.getItemNumber().contains(".")) {
				String[] itemNumber = inventory.getItemNumber().split("\\.");
				data.setItemNumber(itemNumber[0]);
			} else {
				data.setItemNumber(inventory.getItemNumber());
			}
			data.setMonthlyQuotaPerUser(inventory.getMonthlyQuotaPerUser());
			data.setPrice(inventory.getPrice());
			data.setStock(inventory.getStock());
			data.setToBeSoldOneItemPerOrder(inventory.isOnePerItem());
			data.setYearlyQuotaPerUser(inventory.getYearlyQuotaPerUser());
			data.setItemType(inventory.getItemType());
			if (inventory.getLowStockIndicator() == 1)
				data.setLowStockIndicator(true);
			else
				data.setLowStockIndicator(false);
			data.setImageUrl(inventory.getImageUrl());
			data.setItemEntryDate(inventory.getItemEntryDate());
			Date currDate = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			try {
				Date d1 = sdf.parse(sdf.format(inventory.getItemEntryDate()));
				Date d2 = sdf.parse(sdf.format(currDate));
				long dateDiff = d2.getTime() - d1.getTime();
				long difference_In_Days = (dateDiff / (1000 * 60 * 60 * 24)) % 365;
				if (difference_In_Days <= 5) // new Arrival code
				{
					data.setNewArrival(true);
				} else {
					data.setNewArrival(false);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			inventoryDatasToSend.add(data);
		}
		Page<InventoryData> pagedResult = new PageImpl<InventoryData>(inventoryDatasToSend);
		return pagedResult.getContent();
	}

	@Override
	public Inventory save(InventoryData inventoryData) {
		Inventory inventory = new Inventory();

		inventory.setItemCategory(inventoryData.getCategory());
		inventory.setGroup(inventoryData.getGroup());
		inventory.setItemName(inventoryData.getItemName());
		inventory.setItemNumber(inventoryData.getItemNumber());
		inventory.setItemType(inventoryData.getItemType());
		inventory.setOnePerItem(inventoryData.getToBeSoldOneItemPerOrder());
		inventory.setPrice(inventoryData.getPrice());
		inventory.setStock(inventoryData.getStock());
		inventory.setYearlyQuotaPerUser(inventoryData.getYearlyQuotaPerUser());
		inventory.setMonthlyQuotaPerUser(inventoryData.getMonthlyQuotaPerUser());
		inventory.setWeightVolumePerItem(inventoryData.getVolumePerItem());
		Store store = inventoryData.getStore();
		inventory.setStore(store);
		if (inventoryData.getLowStockIndicator())
			inventory.setLowStockIndicator(1);
		else
			inventory.setLowStockIndicator(0);
		inventory.setImageUrl(inventoryData.getImageUrl());
		Date currDate = new Date(System.currentTimeMillis());
		inventory.setItemEntryDate(currDate);
		return inventoryDoaImpl.save(inventory);
	}

	@Override
	public Inventory update(int id, InventoryData inventoryData) {
		Inventory inventory = inventoryDoaImpl.findById(id);

		inventory.setItemCategory(inventoryData.getCategory());
		inventory.setGroup(inventoryData.getGroup());
		inventory.setItemName(inventoryData.getItemName());
		inventory.setItemNumber(inventoryData.getItemNumber());
		inventory.setItemType(inventoryData.getItemType());
		inventory.setOnePerItem(inventoryData.getToBeSoldOneItemPerOrder());
		inventory.setPrice(inventoryData.getPrice());
		inventory.setStock(inventoryData.getStock());
		inventory.setYearlyQuotaPerUser(inventoryData.getYearlyQuotaPerUser());
		inventory.setMonthlyQuotaPerUser(inventoryData.getMonthlyQuotaPerUser());
		inventory.setWeightVolumePerItem(inventoryData.getVolumePerItem());
		if (inventoryData.getLowStockIndicator())
			inventory.setLowStockIndicator(1);
		else
			inventory.setLowStockIndicator(0);
		inventory.setImageUrl(inventoryData.getImageUrl());
		

		return inventoryDoaImpl.save(inventory);
	}

	@Override
	public void deleteItemById(int id) {
		if (!inventoryDoaImpl.existsById(id)) {
			throw new EntityNotFoundException(HttpStatus.NOT_FOUND.value(),
					messageSource.getMessage("item.not.found", new Integer[] { id }, null));
		}
		inventoryDoaImpl.deleteUserById(id);
	}

	@Override
	public List<String> getGroupsFromInventory() throws Exception {
		List<String> groupList = null;
		try {
			groupList = inventoryDoaImpl.getGroupsFromInventory();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return groupList;
	}

	@Override
	public List<String> getCategoriesFromInventory() throws Exception {
		List<String> categoryList = null;
		try {
			categoryList = inventoryDoaImpl.getCategoriesFromInventory();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return categoryList;
	}

	@Override
	public List<InventoryData> searchItem(String itemName) throws Exception {
		// TODO Auto-generated method stub

		LOGGER.debug("In search item service...");
		try {
			List<InventoryData> inventoryDatasToSend = new ArrayList<InventoryData>();
			List<Inventory> inventoryItems = inventoryDoaImpl.searchItem(itemName);
			for (Inventory inventory : inventoryItems) {
				InventoryData data = new InventoryData();
				data.setCategory(inventory.getItemCategory());
				data.setId(Long.parseLong(inventory.getId().toString()));
				data.setItemName(inventory.getItemName());
				// data.setItemNumber(inventory.getItemNumber());
				if (inventory.getItemNumber().contains(".")) {
					String[] itemNumber = inventory.getItemNumber().split("\\.");
					data.setItemNumber(itemNumber[0]);
				} else {
					data.setItemNumber(inventory.getItemNumber());
				}
				data.setMonthlyQuotaPerUser(inventory.getMonthlyQuotaPerUser());
				data.setPrice(inventory.getPrice());
				data.setStock(inventory.getStock());
				data.setToBeSoldOneItemPerOrder(inventory.isOnePerItem());
				// data.setVolumePerItem(inventory.getStock().toString());
				data.setVolumePerItem(inventory.getWeightVolumePerItem());
				if (inventory.getYearlyQuotaPerUser() != null) {
					data.setYearlyQuotaPerUser(inventory.getYearlyQuotaPerUser());

				} else {
					data.setYearlyQuotaPerUser("1");
				}
				data.setItemType(inventory.getItemType());
				data.setGroup(inventory.getGroup());
				if (inventory.getLowStockIndicator() == 1)
					data.setLowStockIndicator(true);
				else
					data.setLowStockIndicator(false);
				data.setImageUrl(inventory.getImageUrl());
				data.setItemEntryDate(inventory.getItemEntryDate());
				Date currDate = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				
					Date d1 = sdf.parse(sdf.format(inventory.getItemEntryDate()));
					Date d2 = sdf.parse(sdf.format(currDate));
					long dateDiff = d2.getTime() - d1.getTime();
					long difference_In_Days = (dateDiff / (1000 * 60 * 60 * 24)) % 365;
					if (difference_In_Days <= 5) // new Arrival code
					{
						data.setNewArrival(true);
					} else {
						data.setNewArrival(false);
					}

				inventoryDatasToSend.add(data);
			}
			return inventoryDatasToSend;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public List<InventoryData> searchItem(List<String> categories, String itemName) throws Exception {
		// TODO Auto-generated method stub

		LOGGER.debug("In search item service...");
		try {
			List<InventoryData> inventoryDatasToSend = new ArrayList<InventoryData>();
			List<Inventory> inventoryItems = inventoryDoaImpl.searchItem(categories, itemName);
			for (Inventory inventory : inventoryItems) {
				InventoryData data = new InventoryData();
				data.setCategory(inventory.getItemCategory());
				data.setId(Long.parseLong(inventory.getId().toString()));
				data.setItemName(inventory.getItemName());
				// data.setItemNumber(inventory.getItemNumber());
				if (inventory.getItemNumber().contains(".")) {
					String[] itemNumber = inventory.getItemNumber().split("\\.");
					data.setItemNumber(itemNumber[0]);
				} else {
					data.setItemNumber(inventory.getItemNumber());
				}
				data.setMonthlyQuotaPerUser(inventory.getMonthlyQuotaPerUser());
				data.setPrice(inventory.getPrice());
				data.setStock(inventory.getStock());
				data.setToBeSoldOneItemPerOrder(inventory.isOnePerItem());
				// data.setVolumePerItem(inventory.getStock().toString());
				data.setVolumePerItem(inventory.getWeightVolumePerItem());
				if (inventory.getYearlyQuotaPerUser() != null) {
					data.setYearlyQuotaPerUser(inventory.getYearlyQuotaPerUser());

				} else {
					data.setYearlyQuotaPerUser("1");
				}
				data.setItemType(inventory.getItemType());
				data.setGroup(inventory.getGroup());
				if (inventory.getLowStockIndicator() == 1)
					data.setLowStockIndicator(true);
				else
					data.setLowStockIndicator(false);
				
				data.setImageUrl(inventory.getImageUrl());
				data.setItemEntryDate(inventory.getItemEntryDate());
				Date currDate = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				
					Date d1 = sdf.parse(sdf.format(inventory.getItemEntryDate()));
					Date d2 = sdf.parse(sdf.format(currDate));
					long dateDiff = d2.getTime() - d1.getTime();
					long difference_In_Days = (dateDiff / (1000 * 60 * 60 * 24)) % 365;
					if (difference_In_Days <= 5) // new Arrival code
					{
						data.setNewArrival(true);
					} else {
						data.setNewArrival(false);
					}
				inventoryDatasToSend.add(data);
			}
			return inventoryDatasToSend;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

}
