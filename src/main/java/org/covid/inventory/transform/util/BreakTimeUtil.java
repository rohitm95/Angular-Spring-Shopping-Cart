package org.covid.inventory.transform.util;

import org.covid.inventory.entity.BreakTimings;
import org.covid.inventory.model.BreakTimeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.bean.ColumnPositionMappingStrategy;

@Component
public class BreakTimeUtil extends ColumnPositionMappingStrategy<BreakTimeDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	public BreakTimeDto toDto(BreakTimings breakTimings) {

		return modelMapper.map(breakTimings, BreakTimeDto.class);
	}
}
