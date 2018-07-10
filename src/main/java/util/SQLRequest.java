package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SQLRequest {
	
	
	private static final String DB_URL =  "jdbc:postgresql://localhost:5432/jenkins";
	private static final String DB_USERNAME = "user";
	private static final String DB_PASSWORD = "password";

	public static Connection connectToJenkinsDB() {
		//create a connection to the DB
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME,DB_PASSWORD);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return conn;

	}
	public static List<String> jenkinsDBCheck(String query) {
		List<String> emptyList = new ArrayList<>();
		try {
			// create an Object of statement;
			Statement stmt = connectToJenkinsDB().createStatement();
			System.out.println("Connection success");

			// Executing the query;
			ResultSet rs = stmt.executeQuery(query);
		
			//browse the data from the request and save it into a List
			rs.next();
			ResultSetMetaData rsmd = rs.getMetaData();
			List<String> actualResult = new ArrayList<String>(rsmd.getColumnCount());
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				
				actualResult.add(rs.getString(i).trim());
			}
			System.out.println("Actual result" + actualResult);

			return actualResult;
		} catch (Exception e) {
			System.out.println("The result is empty");
		}
		return emptyList;
	}
	
}
