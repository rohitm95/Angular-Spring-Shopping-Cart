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

package org.covid.inventory.schedulingtasks;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.covid.inventory.datajpa.StoreRepository;
import org.covid.inventory.entity.Orders;
import org.covid.inventory.entity.Store;
import org.covid.inventory.entity.StoreHoliday;
import org.covid.inventory.entity.StoreTimings;
import org.covid.inventory.schedulingtasks.service.ScheduledTasksServiceImpl;
import org.covid.inventory.service.OrdersService;
import org.covid.inventory.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ScheduledTasks {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Value("${DurationOfTimeSlot}")
	private String durationOfTimeSlot;
	
	@Value("${NoOfDeliveriesInSlot}")
	private String noOfDeliveriesInSlot;
	
	@Value("${DailyBreak}")
	private String dailyBreak;
	
	@Value("${StoreTiming}")
	private String storeTiming;
	
	@Value("${StoreHoliday}")
	private String storeHoliday;
	
	@Autowired
	private ScheduledTasksServiceImpl scheduledTasksServiceImpl;
	
	@Autowired
	private OrdersService  ordersService;
	
	@Scheduled(fixedRate = 5000000)
	   public void fixedRateSch() {
			LOGGER.info("Scheduler runs at "+new Date());
			ObjectMapper mapper = null;
			Map<String, Map<String,String>> workingHrsMap = null;
			Map<String,Map<String,String>> wrkinghrsAll = null;
			Map<String,Map<String,String>> dailyBreakMap = null;
			Map<String,Map<String,String>> storeTimingMap = null;
			Map<String,Map<String,String>> storeHolidayMap = null;

			try {
					mapper = new ObjectMapper();
					
					dailyBreakMap = mapper.readValue(dailyBreak, new TypeReference<Map<String, Map<String,String>>>() {});
					storeTimingMap = mapper.readValue(storeTiming, new TypeReference<Map<String, Map<String,String>>>() {});
					storeHolidayMap = mapper.readValue(storeHoliday, new TypeReference<Map<String, Map<String,String>>>() {});
					
					workingHrsMap = scheduledTasksServiceImpl.createWorkingHourseJson(dailyBreakMap,storeTimingMap);
					
					// get existing time slots in tale time_slots
					Long timeSlotsCount = scheduledTasksServiceImpl.getExistingTimeSlotCount();
					
					if(timeSlotsCount == 0L) {
						//This will execute at very first time when project is deployed and create working hrs for 3 working days
						wrkinghrsAll = scheduledTasksServiceImpl.getAllwrkingHrsFirstTime(workingHrsMap,storeHolidayMap);
					}else {
						wrkinghrsAll = scheduledTasksServiceImpl.getAllwrkingHrs(workingHrsMap,storeHolidayMap);
					}
				    
				    Map<String,List<String>> allSlots = scheduledTasksServiceImpl.calculateAllSlots(wrkinghrsAll,durationOfTimeSlot);
				  
					// save slots in table 
				    scheduledTasksServiceImpl.saveTimeSlotsList(allSlots,noOfDeliveriesInSlot);
				    
					
	        	} catch (Exception e) {
	        		LOGGER.info("Exception "+e.getMessage());
	        	}
	   }
	@Scheduled(cron = "${order.change.status.time}")
	public boolean updateOrdersForLapsed() {
		LOGGER.info("Scheduler started for updateOrdersForLapsed " + new Date());
		
		try {
			final List<Orders> orders = ordersService.updateReadyPendingOrdersList();
			if (orders.size()<0)
			{
				return true;
			}
			LOGGER.info("Scheduler ended for updateOrdersForLapsed " + new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("Errors while  updateing Orders to Lapsed" + e.getMessage());
			return false;
		}
		return false;
	}
}