package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertRevenue {

	public void insertDebit(Statement stm, String datePosted, String timePosted, String debit, String narration) {
		String query = "INSERT INTO revenue(datePosted,timePosted,debit,credit,narration)VALUES('" + datePosted + "','"
				+ timePosted + "','" + debit + "','" + 0 + "','" + narration + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void insertCredit(Statement stm, String datePosted, String timePosted, String credit, String narration) {
		String query = "INSERT INTO revenue(datePosted,timePosted,debit,credit,narration)VALUES('" + datePosted + "','"
				+ timePosted + "','" + 0 + "','" + credit + "','" + narration + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}