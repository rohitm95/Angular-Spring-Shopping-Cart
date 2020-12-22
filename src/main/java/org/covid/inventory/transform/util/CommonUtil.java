package org.covid.inventory.transform.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author mayank_gupta
 *
 */
public class CommonUtil {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);
	
	/**
	 * this method return date as per format yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateString
	 * @param format
	 * @return
	 */
	public static Date getDateFromString(String dateString, String format) {
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			LOGGER.debug("date time format not correct : " + e.getCause());
		}
		return date;
	}
}
