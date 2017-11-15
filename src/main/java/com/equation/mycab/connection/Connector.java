package com.equation.mycab.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connector {

	private Statement stmt, stmt1, stm;
	private Connection conn, conn1;
	private ResultSet rs, rs1;
	private PreparedStatement pstmt;

	public Connector() {
		// TODO Auto-generated constructor stub
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public Statement getStmt1() {
		return stmt1;
	}

	public void setStmt1(Statement stmt1) {
		this.stmt1 = stmt1;
	}

	public Statement getStm() {
		return stm;
	}

	public void setStm(Statement stm) {
		this.stm = stm;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Connection getConn1() {
		return conn1;
	}

	public void setConn1(Connection conn1) {
		this.conn1 = conn1;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public ResultSet getRs1() {
		return rs1;
	}

	public void setRs1(ResultSet rs1) {
		this.rs1 = rs1;
	}

	public PreparedStatement getPstmt() {
		return pstmt;
	}

	public void setPstmt(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

}
