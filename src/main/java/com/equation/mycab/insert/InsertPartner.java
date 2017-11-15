package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

public class InsertPartner {

	public void insertData(Statement stm, String firstName, String lastName, String nationalId, String mobile,
			String email, String address, String joinDate) {
		String qiery = "INSERT INTO partner(firstName,lastName,nationalId,mobile,email,address,joinDate)VALUES('"
				+ firstName + "','" + lastName + "','" + nationalId + "','" + mobile + "','" + email + "','" + address
				+ "','" + joinDate + "')";
		try {
			stm.execute(qiery);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}