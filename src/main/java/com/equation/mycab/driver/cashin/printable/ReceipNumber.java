package com.equation.mycab.driver.cashin.printable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Wellington
 *
 */
public class ReceipNumber {

	Statement stm, stmt;
	ResultSet rs, rs1;

	public ReceipNumber(Statement stm, Statement stmt, ResultSet rs, ResultSet rs1) {
		super();
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
	}

	public String newReceiptNumber() {
		String receipt = null;
		String query = "SELECT receiptId FROM receipt";
		try {
			rs1 = stmt.executeQuery(query);
			rs1.last();
			int id = rs1.getRow(), rows = 0;
			if (id > 0)
				rows = rs1.getInt(1);
			else
				rows = 1;
			receipt = "" + rows;
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return receipt;
	}

}
