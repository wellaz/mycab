package com.equation.mycab.netcash;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class TotalExpense {
	ResultSet rs, rs1;
	Statement stm, stmt;
	double total = 0;

	public TotalExpense(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		getTotalExpense();
	}

	private void getTotalExpense() {

		String query = "SELECT SUM(amount) FROM expense WHERE isOperational='" + true + "' GROUP By isOperational";

		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow();
			if (rows > 0) {
				String query1 = "SELECT SUM(amount) FROM expense WHERE isOperational='" + true
						+ "' GROUP By isOperational";
				rs = stm.executeQuery(query1);
				while (rs.next()) {

					double sum = rs.getDouble(1);

					total += sum;
				}

			} else {

			}

		} catch (SQLException ee) {
			ee.printStackTrace();
		}

	}

	public double getTotal() {
		return total;
	}

}
