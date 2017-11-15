package com.equation.mycab.utils.withdrawals.unpaid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Wellington
 */

public class AllWithdrawalIDs {
	Statement stm, stmt;
	ResultSet rs, rs1;

	public AllWithdrawalIDs(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
	}

	public ArrayList<Integer> getIds() {
		ArrayList<Integer> data = new ArrayList<>();
		String query = "SELECT withdrawalId FROM withdrawal";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow();
			if (rows > 0) {
				String query1 = "SELECT withdrawalId FROM withdrawal";
				rs = stm.executeQuery(query1);
				while (rs.next()) {
					data.add(rs.getInt(1));
				}

			} else {

			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return data;
	}

}
