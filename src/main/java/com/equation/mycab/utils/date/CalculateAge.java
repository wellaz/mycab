package com.equation.mycab.utils.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Wellington
 */
public class CalculateAge {

	public CalculateAge() {

	}

	public int getAge(String dateofbirth) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(dateofbirth, formatter);
		LocalDate now = LocalDate.now();
		return (int) ((now.toEpochDay() - date.toEpochDay()) / 365);
	}
}