package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertExpense {

	public void insertData(Statement stm, String vehicleId, String expenseType, String amount, String datePosted,
			String description, boolean isOperational) {
		String query = "INSERT INTO expense(vehicleId,expenseType,amount,datePosted,description,isOperational)VALUES('"
				+ vehicleId + "','" + expenseType + "','" + amount + "','" + datePosted + "','" + description + "','"
				+ isOperational + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}