package org.covid.inventory.controller;

import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Base64;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.poi.util.IOUtils;
import org.covid.inventory.entity.Inventory;
import org.covid.inventory.entity.Store;
import org.covid.inventory.model.InventoryData;
import org.covid.inventory.service.InventoryService;
import org.covid.inventory.service.StoreService;
import org.covid.inventory.transform.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/items")
public class InventoryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

	@Autowired
	InventoryService inventoryService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	StoreService storeService;

	@Autowired
	private ServletContext servletContext;

	@GetMapping
	public ResponseEntity<ResponseMessage<List<InventoryData>>> getAllItems(
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
		List<InventoryData> inventoryData = null;
		ResponseMessage<List<InventoryData>> responseMessage = new ResponseMessage<List<InventoryData>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		try {
			LOGGER.debug("In controller.....");
			if (page != null)
				inventoryData = inventoryService.getAllInventoryData(page, size);
			else
				inventoryData = inventoryService.getAllInventoryData();
			if (inventoryData != null && inventoryData.size() > 0) {
				responseMessage.setResult(inventoryData);
				responseMessage.setTotalElements(inventoryService.getAllInventoryData().size());
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.OK);
			}

		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.NOT_FOUND);
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<?> uploadInventoryDataFromExcel(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) {
		LOGGER.info("Web Service called : /items/uploadFile");
		List<InventoryData> inventoryDatas = null;
		final String fileName = file.getOriginalFilename();
		// String storeId = "1";
		String[] splitFileName = fileName.split("\\.");
		String firstString = splitFileName[0];
		String[] last = firstString.split("_");
		String storeName;
		if (last.length == 1)
			storeName = null;
		else
			storeName = last[last.length - 1];
		LOGGER.info("Inserting inventory for store");
		Store store = null;
		if (storeName != null)
			store = storeService.getStoreByName(storeName);
		else
			store = storeService.findById(1);

		String currentUrl = request.getRequestURL().toString();
		String[] shortUrl = currentUrl.split("/uploadFile");
		ResponseMessage<List<InventoryData>> responseMessage = new ResponseMessage<List<InventoryData>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		try {
			inventoryDatas = inventoryService.readInventoryListFromFile(file, shortUrl[0],
					String.valueOf(store.getId()));
			inventoryService.insertInventoryData(inventoryDatas, store);
			responseMessage.setResult(inventoryDatas);
			responseMessage.setStatus(200);
			responseMessage.setStatusText("SUCCESS");
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.OK);
	}

	@GetMapping("/images/{storeId}/{imageId}")
	@CrossOrigin
	public void getImage(@PathVariable String imageId, @PathVariable String storeId, HttpServletResponse response)
			throws IOException {
	
					File imgFile  = new File(System.getProperty("user.home") + File.separator+"Images"+File.separator+storeId+File.separator+imageId);
					response.setContentType(MediaType.IMAGE_PNG_VALUE);
					FileInputStream fi=new FileInputStream(imgFile);
					StreamUtils.copy(fi, response.getOutputStream());
		

	}

	@GetMapping("/category")
	public ResponseEntity<ResponseMessage<List<InventoryData>>> getInventoryDataByCategory(
			@RequestParam(name = "categories", required = false) List<String> categories,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
		List<InventoryData> inventoryData = null;
		ResponseMessage<List<InventoryData>> responseMessage = new ResponseMessage<List<InventoryData>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		try {
			LOGGER.debug("In Inventory find by category controller.....");

			if (page != null) {
				if (categories != null && !categories.isEmpty()) {
					inventoryData = inventoryService.getCategoryInventoryData(categories, page, size);
					responseMessage.setTotalElements(inventoryService.getCategoryInventoryData(categories).size());
				} else {
					inventoryData = inventoryService.getAllInventoryData(page, size);
					responseMessage.setTotalElements(inventoryService.getAllInventoryData().size());
				}
			} else {
				if (categories != null && !categories.isEmpty()) {
					inventoryData = inventoryService.getCategoryInventoryData(categories);
					responseMessage.setTotalElements(inventoryService.getCategoryInventoryData(categories).size());
				} else {
					inventoryData = inventoryService.getAllInventoryData();
					responseMessage.setTotalElements(inventoryService.getAllInventoryData().size());
				}
			}
			if (inventoryData != null && inventoryData.size() > 0) {
				responseMessage.setResult(inventoryData);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.OK);
			}

		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseMessage<Inventory>> addInventoryItem(
			@Valid @RequestPart("inventoryData") InventoryData inventoryData,
			@RequestPart("file") MultipartFile ImageFile, HttpServletRequest request) throws Exception {
		LOGGER.info("Web Service called : /api/items");
		ResponseMessage<Inventory> responseMessage = new ResponseMessage<Inventory>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Unable to add item to inventory");
		Inventory item = null;

		Store store = storeService.findById(1);
		if (inventoryData.getStore() == null)
			inventoryData.setStore(store);

		try {
			final String UPLOADED_FOLDER = System.getProperty("user.home") + File.separator+"Images"+File.separator + store.getId()+ File.separator;

			File dir = new File(UPLOADED_FOLDER);

			byte[] bytes = ImageFile.getBytes();

			if (!dir.exists())
				dir.mkdirs();

			File uploadFile = new File(dir.getAbsolutePath() + File.separator + ImageFile.getOriginalFilename());
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(uploadFile));
			outputStream.write(bytes);
			outputStream.close();

			String currentUrl = request.getRequestURL().toString();

			String imageUrl = currentUrl + "/images/" + store.getId() + "/" + ImageFile.getOriginalFilename();
			inventoryData.setImageUrl(imageUrl);
			item = inventoryService.save(inventoryData);
			if (item != null) {
				responseMessage.setResult(item);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<Inventory>>(responseMessage, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<ResponseMessage<Inventory>>(responseMessage, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseMessage<Inventory>> updateInventoryItem(@PathVariable @Min(1) int id,
			@Valid @RequestPart("inventoryData") InventoryData inventoryData,
			@RequestPart("file") MultipartFile ImageFile, HttpServletRequest request) {
		LOGGER.info("Web Service called : /api/items/{id}");
		ResponseMessage<Inventory> responseMessage = new ResponseMessage<Inventory>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Unable to update inventory data.");
		Inventory item = null;

		try {
			String storeId = "1";
			final String UPLOADED_FOLDER = System.getProperty("user.home") + File.separator+"Images"+File.separator + storeId+ File.separator;

			File dir = new File(UPLOADED_FOLDER);

			byte[] bytes = ImageFile.getBytes();

			if (!dir.exists())
				dir.mkdirs();

			File uploadFile = new File(dir.getAbsolutePath() + File.separator + ImageFile.getOriginalFilename());
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(uploadFile));
			outputStream.write(bytes);
			outputStream.close();

			String currentUrl = request.getRequestURL().toString();

			String imageUrl = currentUrl + "/images/" + storeId + "/" + ImageFile.getOriginalFilename();
			inventoryData.setImageUrl(imageUrl);
			item = inventoryService.update(id, inventoryData);
			if (item != null) {
				responseMessage.setResult(item);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<Inventory>>(responseMessage, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<ResponseMessage<Inventory>>(responseMessage, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteInventoryItem(@PathVariable @Min(1) int id) {
		inventoryService.deleteItemById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/groups")
	ResponseEntity<ResponseMessage<List<String>>> getGroups() {
		List<String> groupList = null;
		ResponseMessage<List<String>> responseMessage = new ResponseMessage<List<String>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Groups not found");
		try {
			LOGGER.debug("In groups controller...");
			groupList = inventoryService.getGroupsFromInventory();
			if (groupList != null && groupList.size() > 0) {
				responseMessage.setResult(groupList);
				responseMessage.setTotalElements(inventoryService.getGroupsFromInventory().size());
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<String>>>(responseMessage, HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO: handle exception
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<String>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<String>>>(responseMessage, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/categories")
	ResponseEntity<ResponseMessage<List<String>>> getCategories() {
		List<String> categoryList = null;
		ResponseMessage<List<String>> responseMessage = new ResponseMessage<List<String>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Categories not found");
		try {
			LOGGER.debug("In categories controller...");
			categoryList = inventoryService.getCategoriesFromInventory();
			if (categoryList != null && categoryList.size() > 0) {
				responseMessage.setResult(categoryList);
				responseMessage.setTotalElements(inventoryService.getCategoriesFromInventory().size());
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<String>>>(responseMessage, HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO: handle exception
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<String>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<String>>>(responseMessage, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/search")
	public ResponseEntity<ResponseMessage<List<InventoryData>>> getSearchData(
			@RequestParam(value = "itemName", required = true) String itemName,
			@RequestParam(value = "categories", required = false) List<String> categories) {
		List<InventoryData> inventoryData = null;
		ResponseMessage<List<InventoryData>> responseMessage = new ResponseMessage<List<InventoryData>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Requested item not found");
		try {
			LOGGER.debug("In search controller...");
			if (categories != null && !categories.isEmpty()) {
				inventoryData = inventoryService.searchItem(categories, itemName);
				responseMessage.setTotalElements(inventoryService.searchItem(categories, itemName).size());
			} else {
				inventoryData = inventoryService.searchItem(itemName);
				responseMessage.setTotalElements(inventoryService.searchItem(itemName).size());
			}
			if (inventoryData != null && inventoryData.size() > 0) {
				responseMessage.setResult(inventoryData);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.OK);
			}
		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<InventoryData>>>(responseMessage, HttpStatus.NOT_FOUND);
	}
}
