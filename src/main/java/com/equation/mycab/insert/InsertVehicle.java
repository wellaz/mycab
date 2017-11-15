package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertVehicle {

	public void insertData(Statement stm, String regNumber, String engineNumber, String model, String color,
			String description, String dateReceived) {
		String query = "INSERT INTO vehicle(regNumber,engineNumber,model,color,description,dateReceived)VALUES('"
				+ regNumber + "','" + engineNumber + "','" + model + "','" + color + "','" + description + "','"
				+ dateReceived + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}