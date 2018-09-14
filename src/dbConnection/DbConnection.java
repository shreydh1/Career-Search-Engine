package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	static Connection con;

	public static Connection getConnection(String username, String password) { 
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@dbsvcs.cs.uno.edu:1521:orcl",
					username, password);
			if (con != null)
				System.out.println("*****Connection " + username + " established*****");
		} catch (SQLException e) {
			e.getMessage();
		} catch (ClassNotFoundException e) {
			e.getMessage();
		}
		return con;
	}

	public static Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		DbConnection.con = con;
	}
	
}



