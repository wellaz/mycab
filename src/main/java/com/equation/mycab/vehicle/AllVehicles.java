package com.equation.mycab.vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Wellington
 */

public class AllVehicles {
	Statement stm, stmt;
	ResultSet rs, rs1;

	HashMap<Integer, String> regNumberAndId;
	ArrayList<String> vehicleRegNumbers;

	public AllVehicles(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		processData();
	}

	private void processData() {
		vehicleRegNumbers = new ArrayList<>();
		regNumberAndId = new HashMap<>();
		String query = "SELECT vehicleId,regNumber FROM vehicle";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow();
			if (rows > 0) {
				String query1 = "SELECT vehicleId,regNumber FROM vehicle";
				rs = stm.executeQuery(query1);
				while (rs.next()) {
					int id = rs.getInt(1);
					String regNumber = rs.getString(2);

					vehicleRegNumbers.add(regNumber);

					regNumberAndId.put(id, regNumber);
				}

			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public static Object getVehicleId(HashMap<Integer, String> hashmap, Object value) {
		for (Object o : hashmap.keySet()) {
			if (hashmap.get(o).equals(value)) {
				return o;
			}
		}
		return null;
	}

	public HashMap<Integer, String> getRegNumberAndId() {
		return regNumberAndId;
	}

	public ArrayList<String> getRegNumbers() {
		return vehicleRegNumbers;
	}

}
