package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class InsertServiceSpan {

	public void insertData(Statement stm, String vehicleId, String distance, String unit, String datePosted) {
		String query = "INSERT INTO servicespan(vehicleId,distance,unit,datePosted)VALUES('" + vehicleId + "','"
				+ distance + "','" + unit + "','" + datePosted + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}