package com.equation.mycab.driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DriversCollection {

	Statement stm, stmt;
	ResultSet rs, rs1;
	HashMap<Integer, String> driverAndId;
	ArrayList<String> drivernames;

	public DriversCollection(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		processData();
	}

	private void processData() {
		drivernames = new ArrayList<>();
		driverAndId = new HashMap<>();
		String query = "SELECT driverId,firstName,lastName FROM driver";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow();
			if (rows > 0) {
				String query1 = "SELECT driverId,firstName,lastName FROM driver";
				rs = stm.executeQuery(query1);
				while (rs.next()) {
					int id = rs.getInt(1);
					String firstname = rs.getString(2);
					String lastname = rs.getString(3);
					drivernames.add(firstname + " " + lastname);

					driverAndId.put(id, firstname + " " + lastname);
				}

			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public static Object getDriverId(HashMap<Integer, String> hashmap, Object value) {
		for (Object o : hashmap.keySet()) {
			if (hashmap.get(o).equals(value)) {
				return o;
			}
		}
		return null;
	}

	public HashMap<Integer, String> getDriverAndId() {
		return driverAndId;
	}

	public ArrayList<String> getDrivernames() {
		return drivernames;
	}
}
