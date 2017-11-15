package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertIntocashIn {

	public void insertData(Statement stm, String driverId, String amount, String currency, String datePosted,
			String timePosted, String receiptId) {
		String query = "INSERT INTO cashin(driverId,amount,currency,datePosted,timePosted,receiptId)VALUES('" + driverId
				+ "','" + amount + "','" + currency + "','" + datePosted + "','" + timePosted + "','" + receiptId
				+ "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}

	}

}
