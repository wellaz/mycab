package com.equation.mycab.partners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Wellington
 *
 */
public class Partner {

	ResultSet rs, rs1;
	Statement stm, stmt;

	public Partner(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		super();
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;

	}

	public String getFirstname(String id) {
		String name = null;
		String quer = "SELECT firstName FROM partner WHERE partnerId = '" + id + "'";
		try {
			rs1 = stmt.executeQuery(quer);
			if (rs1.next()) {
				name = rs1.getString(1);
			} else {
				name = "Nil";
			}

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return name;
	}

	public String getLasttname(String id) {
		String name = null;
		String quer = "SELECT lastName FROM partner WHERE partnerId = '" + id + "'";
		try {
			rs1 = stmt.executeQuery(quer);
			if (rs1.next()) {
				name = rs1.getString(1);
			} else {
				name = "Nil";
			}

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return name;
	}
}
