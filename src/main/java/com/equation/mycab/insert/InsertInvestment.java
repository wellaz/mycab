package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertInvestment {

	public void insertData(Statement stm, String partnerId, String amount, String currency, String datePosted,
			String timePosted, String description, String receiptId) {
		String query = "INSERT INTO investment(partnerId,amount,currency,datePosted,timePosted,description,receiptId)VALUES('"
				+ partnerId + "','" + amount + "','" + currency + "','" + datePosted + "','" + timePosted + "','"
				+ description + "','" + receiptId + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}