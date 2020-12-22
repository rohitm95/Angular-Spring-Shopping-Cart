package org.covid.inventory.controller;

import java.util.List;

import org.covid.inventory.model.OrderDto;
import org.covid.inventory.service.SearchService;
import org.covid.inventory.transform.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/search")
public class SearchController {
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

	@Autowired
	private SearchService searchService;

	/*
	 * private SearchDto sDto;
	 * 
	 * @Autowired public SearchController(SearchDto searchDto){ sDto = searchDto; }
	 */

	@GetMapping("/{fname}/{orderNo}/{deliveryDate}/{timeSlot}")
	public ResponseEntity<ResponseMessage<List<OrderDto>>> searchOrderData(
			@PathVariable String fname, @PathVariable String orderNo,
			@PathVariable String deliveryDate,
			@PathVariable String timeSlot) {
		List<OrderDto> searchDto = null;
		ResponseMessage<List<OrderDto>> responseMessage = new ResponseMessage<List<OrderDto>>();
		responseMessage.setStatus(404);
		responseMessage.setStatusText("Data Not Found");
		try {
			LOGGER.debug("In search controller.....");
			searchDto = searchService.searchOrderData(fname, orderNo, deliveryDate, timeSlot);
			if (searchDto != null) {
				responseMessage.setResult(searchDto);
				responseMessage.setStatus(200);
				responseMessage.setStatusText("SUCCESS");
				return new ResponseEntity<ResponseMessage<List<OrderDto>>>(responseMessage, HttpStatus.OK);
			}

		} catch (Exception e) {
			responseMessage.setStatusText(e.getMessage());
			return new ResponseEntity<ResponseMessage<List<OrderDto>>>(responseMessage, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseMessage<List<OrderDto>>>(responseMessage, HttpStatus.NOT_FOUND);
	}
}
