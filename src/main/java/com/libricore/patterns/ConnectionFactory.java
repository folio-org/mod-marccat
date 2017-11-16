/*
 * (c) LibriCore
 * 
 * Created on 12 Jun 2007
 * 
 * ConnectionFactory.java
 */
package com.libricore.patterns;

import java.sql.Connection;
import java.sql.SQLException;


import com.libricore.librisuite.common.HibernateUtil;

// TODO: ANDREA COMMENT
public class ConnectionFactory {
	public static Connection theConnection = null;

	public static Connection getConnection(String connectionString,
			String user, String password) throws SQLException {
		/*
		if (theConnection == null) {
			if (connectionString == null) {
				theConnection = new OracleDriver().defaultConnection();
			} else {
				DriverManager
						.registerDriver(new oracle.jdbc.driver.OracleDriver());
				theConnection = DriverManager.getConnection(connectionString,
						user, password);
			}
		}
		return theConnection;
		*/;
		throw new IllegalArgumentException("EXPLICIT CALL TO O\\\\ DRIVER");
	}

	public static Connection getConnection() throws SQLException {
		try {
			return new HibernateUtil().currentSession().connection();
		} catch (Exception e) {
			throw new SQLException();
		}
	}
}
