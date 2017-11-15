package com.equation.mycab.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

public class ConnectionPool {

	public Statement stmt, stmt1, stm;
	public Connection conn, conn1;
	public ResultSet rs, rs1;
	public PreparedStatement pstmt;

	public ConnectionPool() {
		super();
		return;
	}

	public void connectionDb() {
		try {
			String url = "jdbc:mysql://localhost:3306/mycab1";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, "root", "");
			conn1 = DriverManager.getConnection(url, "root", "");
			stmt = conn1.createStatement();
			stm = conn.createStatement();
			stmt1 = conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			/*new Notification("Connected", "Connection Established", Notification.Type.TRAY_NOTIFICATION, true)
					.show(Page.getCurrent());*/
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException sqle) {
			sqle.printStackTrace();
			new Notification(
					"<h1 style='color:white;'>Connection Failed!<br/>This session will not proceed because the database server is offline!<br/>Report this error to the administrator for assistance.<h1/>",
					"", Notification.Type.ERROR_MESSAGE, true).show(Page.getCurrent());
		}
	}
}