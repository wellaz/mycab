package com.equation.mycab.vehicle.services;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Wellington
 */

public class Distance {
	public static String KM = "km";
	public static String MILES = "Miles";

	public static ArrayList<String> getUnits() {
		return new ArrayList<>(Arrays.asList(KM, MILES));
	}

}
