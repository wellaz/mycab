package com.equation.mycab.netcash;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class TotalReimbursement {
	ResultSet rs, rs1;
	Statement stm, stmt;
	double total = 0;

	public TotalReimbursement(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		fetchData();
	}

	private void fetchData() {
		String query = "SELECT SUM(amount) FROM reimbursement";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow();
			if (rows > 0) {
				String query1 = "SELECT SUM(amount) FROM reimbursement";
				rs = stm.executeQuery(query1);
				rs.next();
				total = rs.getDouble(1);

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
