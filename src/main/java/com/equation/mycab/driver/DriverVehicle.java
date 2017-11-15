package com.equation.mycab.driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Wellington
 *
 */
public class DriverVehicle {

	Statement stm, stmt;
	ResultSet rs, rs1;

	public DriverVehicle(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
	}

	public String getCarName(String firstName, String lastName) {
		String name = null;
		String query = "SELECT regNumber,model FROM vehicle,driver WHERE driver.firstName = '" + firstName
				+ "' AND driver.lastName= '" + lastName + "'";
		try {
			rs = stm.executeQuery(query);
			if (rs.next()) {
				name = rs.getString(1) + " " + rs.getString(2);
			} else {
				name = "Nil";
			}

		} catch (SQLException ee) {
			ee.printStackTrace();
		}

		return name;
	}

	public String getDriverName(String vehicleId) {
		String drivername = null;

		String query = "SELECT firstName,lastName FROM driver WHERE vehicleId = '" + vehicleId + "'";
		try {
			rs = stm.executeQuery(query);
			rs.next();
			drivername = rs.getString(1) + " " + rs.getString(2);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return drivername;

	}

}
