/**
 * 
 */
package edu.rupp.rmi.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract DAO 
 *   To manage connection, create database, tables
 *   Using SQLite JDBC
 * @author sok.pongsametrey
 * @version 1.0
 */
public abstract class AbstractDAO {
	private Connection connection = null;
	private Statement statement = null;
	public String dbName = "metrey.students.db";
	
	
	/**
	 * 
	 * @throws SQLException
	 */
	private void createDbConnection() throws SQLException {

		try {			
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);			
			
			statement = connection.createStatement();
		    statement.setQueryTimeout(30);  // set timeout to 30 sec.
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to db: " + dbName);
			throw e;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("No driver: " + e.getMessage());
		} 
		System.out.println("Connecting to DB: " + dbName + " via " + connection.toString());
	}
	
	/**
	 * Load and create db
	 * @throws SQLException
	 */
	public abstract void createDBModel() throws SQLException;

	/**
	 * @return the connection
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException {
		if (this.connection == null) {
			this.createDbConnection();
		}
		return connection;
	}

	/**
	 * @return the statement
	 * @throws SQLException 
	 */
	public Statement getStatement() throws SQLException {
		if (this.statement == null) {
			this.createDbConnection();
		}
		return statement;
	}

	/**
	 * 
	 */
	public void close() {		
		try {
			if (this.statement != null) {
				this.statement.close();
				this.statement = null;
			}
			if (this.connection != null) {
				this.connection.close();
				this.connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("DB Connection closed.");
		}
		
	}
	
}
