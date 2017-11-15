package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertDriver {

	public void insertData(Statement stm, String firstName, String lastName, String nationalId, String address,
			String email, String mobile, String dateStarted, String vehicleId) {
		String query = "INSERT INTO driver(firstName,lastName,nationalId,address,email,mobile,dateStarted,vehicleId)VALUES('"
				+ firstName + "','" + lastName + "','" + nationalId + "','" + address + "','" + email + "','" + mobile
				+ "','" + dateStarted + "','" + vehicleId + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}