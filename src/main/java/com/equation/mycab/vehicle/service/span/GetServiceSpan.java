package com.equation.mycab.vehicle.service.span;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class GetServiceSpan {
	ResultSet rs, rs1;
	Statement stm, stmt;

	public GetServiceSpan(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
	}

	public String getSpanFor(String vehicleId) {
		String value = "0";
		String query = "SEELCT distance FROM servicespan WHERE vehicleId = '" + vehicleId + "'";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			if (rs1.getRow() > 0) {
				String query1 = "SEELCT distance FROM servicespan WHERE vehicleId = '" + vehicleId + "'";
				rs = stm.executeQuery(query1);
				rs.last();
				value = rs.getString(1);
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return value;
	}

}
