package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertIntoDividend {

	public void insertData(Statement stm, String amount, String currency, String partnerId, String datePosted,
			String timePosted, String receiptId) {
		String query = "INSERT INTO dividend(amount,currency,partnerId,datePosted,timePosted,receiptId)VALUES('"
				+ amount + "','" + currency + "','" + partnerId + "','" + datePosted + "','" + timePosted + "','"
				+ receiptId + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}