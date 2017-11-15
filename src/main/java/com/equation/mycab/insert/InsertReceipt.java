package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertReceipt {

	public void inserrData(Statement stm, String amount, String datePosted, String timePosted, String transactionType) {
		String query = "INSERT INTO receipt(amount,datePosted,timePosted,transactionType)VALUES('" + amount + "','"
				+ datePosted + "','" + timePosted + "','" + transactionType + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

}
