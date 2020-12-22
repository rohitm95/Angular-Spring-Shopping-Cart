package org.covid.inventory.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryUtil.class);

	/*
	 * This method gets age based on birth date
	 */
	public static Integer getAge(Date birthDate) {
		LocalDate today = LocalDate.now();
		LocalDate localDate = birthDate.toLocalDate();
		Period period = Period.between(localDate, today);
		return period.getYears();
	}

	/*
	 * This method gets current calendar date
	 */
	public static Date getDate() {
		Calendar currenttime = Calendar.getInstance();
		return new Date(currenttime.getTime().getTime());
	}

	public static String shuffle(String input) {
		List<Character> characters = new ArrayList<Character>();
		for (char c : input.toCharArray()) {
			characters.add(c);
		}
		StringBuilder output = new StringBuilder(input.length());
		while (characters.size() != 0) {
			int randPicker = (int) (Math.random() * characters.size());
			output.append(characters.remove(randPicker));
		}
		return output.toString();
	}

	public String convertDateTo12HrsTime(String slotFrom, String slotTo) {
		String newTimeSlot = null;
		SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
		SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
		try {
			if (slotFrom!=null) {
				java.util.Date _24HourDtslotFrom = _24HourSDF.parse(slotFrom);
				newTimeSlot = _12HourSDF.format(_24HourDtslotFrom);
				return newTimeSlot;
			}
			if (slotTo!=null) {
				java.util.Date _24HourDtslotTo = _24HourSDF.parse(slotTo);
				newTimeSlot = _12HourSDF.format(_24HourDtslotTo);
				return newTimeSlot;
			}

		} catch (ParseException e) {
			LOGGER.info("Exception " + e.getMessage());
		}
		return newTimeSlot;
	}
}
