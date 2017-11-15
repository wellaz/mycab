package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertReimbursement {

	public void insertData(Statement stm, String withdrawald, String amount, String currency, String balance,
			String datePosted, String timePosted, String rate, String receiptId) {
		String query = "INSERT INTO reimbursement(withdrawalId,amount,currency,balance,datePosted,timePosted,rate,receiptId)VALUES('"
				+ withdrawald + "','" + amount + "','" + currency + "','" + balance + "','" + datePosted + "','"
				+ timePosted + "','" + rate + "','" + receiptId + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

}
