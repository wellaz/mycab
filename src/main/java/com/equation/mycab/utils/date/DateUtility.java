package com.equation.mycab.utils.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Wellington
 */
public class DateUtility {
	public String getTime() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
		return dateFormatter.format(date);
	}

	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = new Date();
		return dateFormat.format(date1);
	}

	public String getYesterdayDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}

	public String timeStamp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH-mm-ss");
		Date dates = new Date();
		String text = dateFormat.format(dates);
		return text;
	}

	public int getLastYear() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		return Integer.parseInt(dateFormat.format(cal.getTime()));
	}

	public String getYear() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 0);
		return dateFormat.format(cal.getTime());
		// return "2016";
	}

	public String getThirtythDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 30);
		return dateFormat.format(cal.getTime());
	}

	public int getMonth() {
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate.getMonthValue();
	}

	public Date today() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, 0);
		Date when = cal.getTime();
		return when;
	}

	public String thisMonth() {
		return GetCurrentMonth.currentMonth();
	}

	public Date dayThirty() {
		//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, 30);
		Date when = cal.getTime();
		return when;
	}

	public Date firstOfJanuary() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String Jan = getYear() + "-01-1";
		Date january = null;
		try {
			january = dateFormat.parse(Jan);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return january;
	}

	/*public static void main(String[] args) {
		DateUtility dateUtility = new DateUtility();
		System.out.println(dateUtility.dayThirty());
	}*/
}