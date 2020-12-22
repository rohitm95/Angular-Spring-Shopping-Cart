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

package org.covid.inventory.schedulingtasks.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.covid.inventory.entity.StoreTimeslots;
import org.covid.inventory.schedulingtasks.dao.ScheduledTasksDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasksServiceImpl {
	
	@Autowired
	private ScheduledTasksDaoImpl scheduledTasksDaoImpl;
	
	public Map<String, Map<String, String>> getAllwrkingHrsFirstTime(Map<String, Map<String, String>> workingHrsMap, Map<String, Map<String, String>> storeHolidayMap) {
		Map<String,Map<String,String>> wrkinghrsAll = new LinkedHashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE yyyy-MM-dd'T'HH:mm:ssZ");
	    Date now = new Date();
	    String todayDate = sdf.format(now);//Thu 04-06-2020 09:17
	    Calendar c = Calendar.getInstance();
	    c.setTime(now);
		int counter = 0;
		for(Entry<String, Map<String,String>> workingHrs : workingHrsMap.entrySet()) {
			if(workingHrs.getKey().equals(todayDate.split(" ")[0])) {
				  wrkinghrsAll.put(todayDate.split(" ")[1],workingHrsMap.get(todayDate.split(" ")[0]));
				  counter = 1;
			}else if(counter > 0 && counter < 3) {
				  c.add(Calendar.DAY_OF_MONTH, 1);  
				  String newSecondDate = sdf.format(c.getTime()); 
				  wrkinghrsAll.put(newSecondDate.split(" ")[1],workingHrsMap.get(newSecondDate.split(" ")[0]));
				  counter ++;
			}
		}
		if(!wrkinghrsAll.isEmpty() && wrkinghrsAll.size() != 3) {
			for(Entry<String, Map<String,String>> workingHrs : workingHrsMap.entrySet()) {
				if(counter > 0 && counter < 3) {
					  c.add(Calendar.DAY_OF_MONTH, 1);  
					  String newSecondDate = sdf.format(c.getTime()); 
					  if(workingHrsMap.containsKey(newSecondDate.split(" ")[0])) {
						  wrkinghrsAll.put(newSecondDate.split(" ")[1],workingHrsMap.get(newSecondDate.split(" ")[0]));
						  counter ++;
					  }
				}
			}
		}
		return wrkinghrsAll;
		
	}
	
	public Map<String, Map<String, String>> getAllwrkingHrs(Map<String, Map<String, String>> workingHrsMap, Map<String, Map<String, String>> storeHolidayMap) throws ParseException {
		Map<String,Map<String,String>> wrkinghrsAll = new LinkedHashMap<>();
		List<StoreTimeslots> timeSlotsList = scheduledTasksDaoImpl.getExistingTimeSLots();
		StoreTimeslots timeSlots = timeSlotsList.get(timeSlotsList.size()-1);
		Date date = timeSlots.getSlotDate();
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE yyyy-MM-dd");
	    //Thu 04-06-2020 09:17  Mon 2020-06-15T00:00:00+0530
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DAY_OF_MONTH, 1);  
	    String newLastDate = sdf.format(c.getTime()); 
		
	     if(workingHrsMap.containsKey(newLastDate.split(" ")[0])) {
			  wrkinghrsAll.put(newLastDate.split(" ")[1],workingHrsMap.get(newLastDate.split(" ")[0]));
		 }else{
			 c.add(Calendar.DAY_OF_MONTH, 1);  
			 newLastDate = sdf.format(c.getTime()); 
			 Boolean isHoliday = isHoiday(storeHolidayMap,sdf,newLastDate);
			 if(isHoliday) {
					newLastDate = calculateNextWorkningDate(c,storeHolidayMap, sdf, newLastDate);
			 }
			 wrkinghrsAll.put(newLastDate.split(" ")[1],workingHrsMap.get(newLastDate.split(" ")[0]));
		 }
	    
		return wrkinghrsAll;
	}

	private String calculateNextWorkningDate(Calendar c,Map<String, Map<String, String>> storeHolidayMap, SimpleDateFormat sdf, String newLastDate) throws ParseException {
		c.add(Calendar.DAY_OF_MONTH, 1);  
		String date = sdf.format(c.getTime());
		Boolean isHoliday = isHoiday(storeHolidayMap,sdf,date);
		if(isHoliday) {
			newLastDate = calculateNextWorkningDate(c,storeHolidayMap, sdf, date);
		}
		return date;
	}

	private Boolean isHoiday(Map<String, Map<String, String>> storeHolidayMap, SimpleDateFormat sdf, String newLastDate) throws ParseException {
		for (Entry<String, Map<String, String>> workingHr : storeHolidayMap.entrySet()) {
			String holiday  = sdf.format(new SimpleDateFormat("dd/MM/yyyy").parse(workingHr.getValue().get("Date")));
	    	if(holiday.equals(newLastDate))
	    		return true;
	    }
		return false;
	}

	public Map<String, List<String>> calculateAllSlots(Map<String, Map<String, String>> wrkinghrsAll, String slot) {
		Map<String, List<String>> allSlots = new LinkedHashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		try {
			for (Entry<String, Map<String, String>> workingHr : wrkinghrsAll.entrySet()) {
				List<String> slotList = new ArrayList<>();
				if (workingHr.getValue() != null) {
					for (Map.Entry<String, String> entry : workingHr.getValue().entrySet()) {
						String wrkhrs = entry.getValue();
						Date startDate = sdf.parse(wrkhrs.split("-")[0]);
						Date endDate =  sdf.parse(wrkhrs.split("-")[1]);
						for (int time = (startDate.getHours() * 60)+startDate.getMinutes(); time < (endDate.getHours()* 60)+endDate.getMinutes(); time += Integer.parseInt(slot)) {
							String slot1 = String.format("%02d:%02d", time / 60, time % 60);
							String slot2 = LocalTime.parse(slot1).plusMinutes(Long.parseLong(slot)).toString();
							if(format.parse(slot2).compareTo(format.parse(wrkhrs.split("-")[1]))<=0) {
								slotList.add(slot1 + " - " + slot2);
							}
							
						}
					}
					allSlots.put(workingHr.getKey(), slotList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allSlots;
	}

	public Long getExistingTimeSlotCount() {
		return	scheduledTasksDaoImpl.getExistingTimeSlotCount();
	}

	public void saveTimeSlotsList(Map<String, List<String>> allSlots, String order_in_slot) {
		scheduledTasksDaoImpl.saveTimeSlotsList(allSlots, order_in_slot);
	}

	public Map<String,Map<String,String>> createWorkingHourseJson(Map<String, Map<String, String>> dailyBreakMap,
			Map<String, Map<String, String>> storeTimingMap) {
		Map<String, Map<String, String>> wrkinghrsAll = new LinkedHashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		try {
			for (Entry<String, Map<String, String>> workingHrs : storeTimingMap.entrySet()) {
				Map<String, String> workingHrsValue = workingHrs.getValue();
				if (workingHrsValue.get("WeeklyOff").equals("No")) {
					Map<String, String> wrkinfTimeMap = new LinkedHashMap<>();
					int count = 1;
					String deliveryStartAt = workingHrsValue.get("DeliveryStartAt");
					String deliveryClosesAt = workingHrsValue.get("DeliveryClosesAt");
					String brkTo = null;
					String brkFrom = null;
					for (Entry<String, Map<String, String>> map : dailyBreakMap.entrySet()) {
						brkFrom = map.getValue().get("BreakFrom");
						if(brkTo == null)
							brkTo = map.getValue().get("BreakTo");
						if(wrkinfTimeMap.isEmpty()) {
							String workingTime1 = deliveryStartAt + "-" +brkFrom;
							wrkinfTimeMap.put("workingTime"+count, workingTime1);
							count++;
						}else {
							if(!deliveryClosesAt.equals(brkTo)) {
								String workingTime2 = brkTo +  "-" +brkFrom;
								wrkinfTimeMap.put("workingTime"+count, workingTime2);
								count++;
							}
						}
						brkTo = map.getValue().get("BreakTo");
					}
					if(format.parse(brkTo).compareTo(format.parse(deliveryClosesAt))<0) {
						String workingTime2 = brkTo +  "-" +deliveryClosesAt;
						wrkinfTimeMap.put("workingTime"+count, workingTime2);
					}
					wrkinghrsAll.put(workingHrs.getKey(), wrkinfTimeMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wrkinghrsAll;

	}

	
}