package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertWithdrawal {

	public void insertData(Statement stm, String amount, String currency, String datePosted, String timePosted,
			String dueDate, String description, String partnerId, String receiptId) {
		String query = "INSERT INTO withdrawal(amount,currency,datePosted,timePosted,dueDate,description,partnerId,receiptId)VALUES('"
				+ amount + "','" + currency + "','" + datePosted + "','" + timePosted + "','" + dueDate + "','"
				+ description + "','" + partnerId + "','" + receiptId + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}