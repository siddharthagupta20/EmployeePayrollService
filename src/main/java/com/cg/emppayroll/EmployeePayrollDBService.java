package com.cg.emppayroll;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;


public class EmployeePayrollDBService {
	
	public static final String JDBC_URL="jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
	public static final String USERNAME=System.getenv("DB_USER");
	public static final String PASS=System.getenv("DB_PASS");
	
	public static void main(String[] args) {
		Connection con=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded");
		}
		catch(ClassNotFoundException e) {
			throw new IllegalStateException("Driver not loaded.");
		}
		listDrivers();
		
		try {
			System.out.print("Connecting to database.");
			con=DriverManager.getConnection(JDBC_URL, USERNAME, PASS);
			System.out.println("\t Connection successful.");
		}catch(SQLException e) {
			System.out.println("\t Connection failure.");
		}
	}

	private static void listDrivers() {
		System.out.println("Listing Drivers:");
		Enumeration<Driver> driversList=DriverManager.getDrivers();
		while(driversList.hasMoreElements())
			System.out.println(driversList.nextElement().getClass().getSimpleName());
	}

}
