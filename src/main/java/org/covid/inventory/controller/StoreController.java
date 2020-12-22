package org.covid.inventory.controller;

import org.covid.inventory.entity.Store;
import org.covid.inventory.model.ChangeOrderDto;
import org.covid.inventory.model.OrderDetailsDto;
import org.covid.inventory.model.OrderDto;
import org.covid.inventory.model.RoleDto;
import org.covid.inventory.model.StoreDto;
import org.covid.inventory.service.StoreService;
import org.covid.inventory.transform.util.ResponseMessage;
import org.covid.inventory.transform.util.StoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/stores")
public class StoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private StoreUtil storeUtil;

    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<?> createStore(@Valid @RequestBody StoreDto storeDto) throws Exception {
        LOGGER.info("Web Service called : /api/stores [POST]");
        ResponseMessage<Object> responseMessage = new ResponseMessage<Object>();
		responseMessage.setStatus(500);
		responseMessage.setStatusText("Store Not Added!!!");
		
		try {
			String storeName = storeDto.getStoreName();
			Store existingStoreObj = storeService.getStoreByName(storeName);
			if (existingStoreObj != null) {
				responseMessage.setStatus(409);
				responseMessage.setStatusText("Store name already exists");
				return new ResponseEntity<ResponseMessage<Object>>(responseMessage, HttpStatus.CONFLICT);
			}
			
			Store store = storeService.save(storeUtil.toEntity(storeDto));
			if (store != null) {
				responseMessage.setResult(storeUtil.toDto(store));
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
			}
		} catch (Exception e) {
			LOGGER.error("Exception : /api/stores [POST]");
			responseMessage.setStatusText(e.getMessage());
			responseMessage.setStatus(500);
			return new ResponseEntity<ResponseMessage<Object>>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ResponseMessage<Object>>(responseMessage, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateStore(@Valid @RequestBody StoreDto storeDto) throws Exception {
    	LOGGER.info("Web Service called : /api/stores [PUT]");
    	
    	ResponseMessage<Object> responseMessage = new ResponseMessage<Object>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Store Details Not Changed!!!");
		
		try {
			
			
		
			Store store = storeService.update(storeUtil.toEntity(storeDto));
			if (store != null) {
				responseMessage.setResult(storeUtil.toDto(store));
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Exception : /api/stores [PUT] for storeId:- {0}", storeDto.getId()));
			responseMessage.setStatusText(e.getMessage());
			responseMessage.setStatus(500);
			return new ResponseEntity<ResponseMessage<Object>>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ResponseMessage<Object>>(responseMessage, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<?> findAll() {
    	LOGGER.info("Web Service called : /api/stores [GET]");
    	List<Store> stores = null;
		ResponseMessage<Object> responseMessage = new ResponseMessage<Object>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Stores Not Found");

		try {
			stores = storeService.findAll();
			if (stores.size() > 0) {
				responseMessage.setResult(
						stores.stream().map(storeUtil::toDto).collect(Collectors.toList())
				);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return ResponseEntity.ok(responseMessage);
			}
		} catch (Exception e) {
			LOGGER.error("Exception : /api/stores [GET]");
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<Object>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(responseMessage, HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
    	LOGGER.info("Web Service called : /api/stores/{storeId} [GET]");
    	Store store = null;
		ResponseMessage<Object> responseMessage = new ResponseMessage<Object>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");

		try {
			store = storeService.findById(id);
			if (store != null) {
				responseMessage.setResult(storeUtil.toDto(store));
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return ResponseEntity.ok(responseMessage);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Exception : /api/stores/{0} [GET]"), id);
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<Object>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Object>(responseMessage, HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/{id}/status")
    public ResponseEntity<?> changeActiveStatus(@PathVariable("id") Integer id, @RequestParam("active") Boolean active) {
    	LOGGER.info("Web Service called : /api/stores/{id}/status [GET]");
    	
		ResponseMessage<Object> responseMessage = new ResponseMessage<Object>();
		responseMessage.setStatus(500);
		responseMessage.setStatusText("Status Not Changed");

		try {
			Boolean result = storeService.changeActiveStatus(id, active);
			if(result) {
				responseMessage.setResult(null);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return ResponseEntity.ok(responseMessage);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Exception : /api/stores/{0}/status [GET]"), id);
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<Object>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
