package org.covid.inventory.transform.util;

import org.covid.inventory.entity.StoreHoliday;
import org.covid.inventory.entity.StoreTimings;
import org.covid.inventory.model.StoreHolidayDto;
import org.covid.inventory.model.StoreTimeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.bean.ColumnPositionMappingStrategy;

@Component
public class StoreHolidaysUtil extends ColumnPositionMappingStrategy<StoreHolidayDto> {

	@Autowired
	private ModelMapper modelMapper;
	
	public StoreHolidayDto toDto(StoreHoliday storeHoliday) {

		return modelMapper.map(storeHoliday, StoreHolidayDto.class);
	}
}
