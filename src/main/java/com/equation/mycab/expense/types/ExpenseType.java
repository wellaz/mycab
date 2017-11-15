package com.equation.mycab.expense.types;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Wellington
 */

public class ExpenseType {
	public static String CAR_PRICE = "Car Price";

	public static String CARRIAGE = "Carriage";
	public static String DUTY = "Duty";
	public static String AGENT = "Agent";
	public static String STORAGE = "Storage";
	public static String INSURANCE = "Insurance";
	public static String TEMPORARY_PLATES = "Temporary PLates";
	public static String FUEL = "Fuel";
	public static String REFLECTORS = "Reflectors";
	public static String TRANSPORT_TO_BEIT = "Transport To Beit";
	public static String FOOD = "FOOD";
	public static String TOLL_GATES = "Toll Gates";
	public static String ACCOMMODATION = "Accommodation";
	public static String DRIVER_APPREC = "Driver Appreciation";
	public static String PLATES_AND_BOOK = "Plates and book";
	public static String TRANS_TO_FROM_H_FOR_BOOK_AND_PLATES = "Transport to HARARE for Book and plates";
	public static String LICENCE = "Lisence";
	public static String CID_CLEAR = "CID Clearance";
	public static String TYRES = "Tyres";
	public static String SUSPENSION = "Suspensions";
	public static String POLICE_FINE = "Police Fine";
	public static String OTHER_PARTS = "Other Vehicle Parts";
	public static String TRANSPORT_FOR_BUS_OP = "Transport for daily operations";
	public static String AIRTIME = "Airtime";
	public static String OTHERS = "Others";

	public static ArrayList<String> expenseList() {
		ArrayList<String> data = new ArrayList<>(Arrays.asList(CAR_PRICE, CARRIAGE, DUTY, AGENT, STORAGE, INSURANCE,
				TEMPORARY_PLATES, FUEL, REFLECTORS, TRANSPORT_TO_BEIT, FOOD, TOLL_GATES, ACCOMMODATION, DRIVER_APPREC,
				PLATES_AND_BOOK, TRANS_TO_FROM_H_FOR_BOOK_AND_PLATES, LICENCE, CID_CLEAR, TYRES, SUSPENSION,
				POLICE_FINE, OTHER_PARTS, TRANSPORT_FOR_BUS_OP, AIRTIME, OTHERS));
		return data;

	}
}