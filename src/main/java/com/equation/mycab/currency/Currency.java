package com.equation.mycab.currency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Currency {
	ResultSet rs, rs1;
	Statement stm, stmt;

	public Currency(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
	}

	public ArrayList<String> getCurrency() {
		ArrayList<String> data = new ArrayList<>();

		String query = "SELECT currencyName FROM mycurrency";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int rows = rs1.getRow();
			if (rows > 0) {
				String query1 = "SELECT currencyName FROM mycurrency";
				rs = stm.executeQuery(query1);
				while (rs.next()) {
					data.add(rs.getString(1));
				}
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return data;
	}
}