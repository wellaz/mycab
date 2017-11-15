package com.equation.mycab.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Wellington
 */
public class MyDateConverter {

	public static Date getDate(String dateString) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		try {
			startDate = df.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return startDate;
	}
}