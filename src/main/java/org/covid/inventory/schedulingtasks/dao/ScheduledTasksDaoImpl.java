/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.covid.inventory.schedulingtasks.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.covid.inventory.datajpa.StoreTimeslotsRepository;
import org.covid.inventory.entity.StoreTimeslots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasksDaoImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasksDaoImpl.class);
	
	@Autowired
	private StoreTimeslotsRepository timeSlotsRepository;

	public void saveTimeSlotsList(Map<String, List<String>> allSlots, String order_in_slot) {
		try {
			 List<StoreTimeslots> timeSlotsList = new ArrayList<StoreTimeslots>();
			 for (Map.Entry<String,List<String>> entry : allSlots.entrySet()) {
					for(String slot : entry.getValue()) {
							StoreTimeslots timeSlots = new StoreTimeslots();
								timeSlots.setAvailableCount(Integer.parseInt(order_in_slot));
								
								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
								Date date = dateFormat.parse(entry.getKey());
								timeSlots.setStoreId(1);
								timeSlots.setSlotDate(date);
								timeSlots.setSlotFrom(slot.split("-")[0].trim());
								timeSlots.setSlotTo(slot.split("-")[1].trim());
								timeSlots.setInsertedOn(new Date());
								timeSlots.setUpdatedOn(new Date());
								timeSlots.setCustomerId(1);
								timeSlots.setIsDeleted('N');
								timeSlotsList.add(timeSlots);
							}
				}
			    if(!timeSlotsList.isEmpty())
			    	timeSlotsRepository.saveAll(timeSlotsList);
			} catch (Exception e) {
				LOGGER.info("Exception "+e.getMessage());
			}
			   
		}

	public Long getExistingTimeSlotCount() {
		return timeSlotsRepository.count();
	}

	public  List<StoreTimeslots> getExistingTimeSLots() {
		return timeSlotsRepository.findAll();		
	}

}