package com.equation.mycab.insert;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Wellington
 *
 */
public class InsertUser {

	public void insertData(Statement stm, String partnerId, String username, String password, String datePosted) {
		String query = "INSERT INTO users(partnerId,username,password,datePosted)VALUES('" + partnerId + "','"
				+ username + "','" + password + "','" + datePosted + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}

	}

}
