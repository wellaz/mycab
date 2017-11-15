package com.equation.mycab.utils.currency.check;

public class CheckValue {

	public static boolean isMoney(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
