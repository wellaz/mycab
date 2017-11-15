package com.equation.mycab.utils.roundoff;

/**
 *
 * @author Wellington
 */
public class Money {
	public static double toMoney(double value) {
		return (double) Math.round(value * 100) / 100;
	}

}
