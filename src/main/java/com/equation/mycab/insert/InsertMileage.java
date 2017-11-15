package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class InsertMileage {

	public void insertData(Statement stm, String vehicleId, String dateofservice, String distance, String unit) {
		String query = "INSERT INTO mileage(	vehicleId,dateofservice,distance,unit)VALUES('" + vehicleId + "','"
				+ dateofservice + "','" + distance + "','" + unit + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}

	}

}
