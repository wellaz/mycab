package com.equation.mycab.netcash;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author Wellington
 */

public class TotalCashIn {
	ResultSet rs, rs1;
	Statement stm, stmt;
	HashMap<String, Double> collection;
	double total = 0;

	public TotalCashIn(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		getTotalCashIn();
	}

	private void getTotalCashIn() {
		collection = new HashMap<>();

		String query = "SELECT currency, SUM(amount) FROM cashin GROUP BY currency";

		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow();
			if (rows > 0) {
				String query1 = "SELECT currency, SUM(amount) FROM cashin GROUP BY currency";
				rs = stm.executeQuery(query1);
				while (rs.next()) {
					String currency = rs.getString(1);
					double sum = rs.getDouble(2);
					collection.put(currency, sum);
					total += sum;
				}

			} else {

			}

		} catch (SQLException ee) {
			ee.printStackTrace();
		}

	}

	public HashMap<String, Double> getCollection() {
		return collection;
	}

	public double getTotal() {
		return total;
	}

}
