package org.covid.inventory.transform.util;

import org.covid.inventory.entity.StoreTimings;
import org.covid.inventory.model.StoreTimeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.bean.ColumnPositionMappingStrategy;

@Component
public class StoreTimingsUtil extends ColumnPositionMappingStrategy<StoreTimeDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	public StoreTimeDto toDto(StoreTimings storeTimings) {

		return modelMapper.map(storeTimings, StoreTimeDto.class);
	}
}
