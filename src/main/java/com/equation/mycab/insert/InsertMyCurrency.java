package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertMyCurrency {

	public void insertData(Statement stm, String currencyName, String datePosted) {
		String query = "INSERT INTO mycurrency(currencyName,datePosted)VALUES('" + currencyName + "','" + datePosted
				+ "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

}
