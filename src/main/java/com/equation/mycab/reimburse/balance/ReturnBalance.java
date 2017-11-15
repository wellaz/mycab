package com.equation.mycab.reimburse.balance;

/**
 *
 * @author Wellington
 */

public class ReturnBalance {

	public static double getBalance(String prevCurrency, String selectedCurrency, double tendered, double prevBalance,
			double rate) {
		double balance = 0;
		if (prevCurrency.equals(selectedCurrency)) {
			// do normal subtraction
			balance = prevBalance - tendered;
		} else {
			// rating comes in
			double tenderedEquiv = tendered * rate;
			balance = prevBalance - tenderedEquiv;

		}

		return balance;
	}

}
