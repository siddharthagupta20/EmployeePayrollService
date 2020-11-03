package com.cg.emppayroll;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {

	public static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
	public static final String USERNAME = System.getenv("DB_USER");
	public static final String PASS = System.getenv("DB_PASS");
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;

	private EmployeePayrollDBService() {
		this.employeePayrollDataStatement = null;
	}

	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}

	private Connection getConnection() throws SQLException {
		Connection connection = null;
		System.out.println("Connecting to database:" + JDBC_URL);
		connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASS);
		System.out.println("Connection is successfull!!!" + connection);
		return connection;
	}

	private static void listDrivers() {
		System.out.println("Listing Drivers:");
		Enumeration<Driver> driversList = DriverManager.getDrivers();
		while (driversList.hasMoreElements())
			System.out.println(driversList.nextElement().getClass().getSimpleName());
	}

	public List<EmployeePayrollData> readData() {
		String sql = "select * from employee_payroll;";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			try {
				while (result.next()) {
					int id = result.getInt("id");
					String name = result.getString("name");
					double salary = result.getDouble("basic_pay");
					LocalDate startDate = result.getDate("start").toLocalDate();
					employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return employeePayrollList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}
	public List<EmployeePayrollData> readEmployeeData() {
		String sql = "select * from employee where status =1;";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			try {
				while (result.next()) {
					int id = result.getInt("emp_id");
					String name = result.getString("name");
					double salary=0.0;
					Statement tempStatement=connection.createStatement();
					ResultSet payrollResult=tempStatement.executeQuery("select * from payroll;");
					while(payrollResult.next())
						if(id==payrollResult.getInt(1))
								salary = payrollResult.getDouble("basic_pay");
					LocalDate startDate = result.getDate("start").toLocalDate();
					employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return employeePayrollList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	public int updateEmployeeData(String name, double salary) {
		return this.updateEmployeeDataUsingStatement(name, salary);
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) {
		String sql = String.format("update employee_payroll set basic_pay= %.2f where name = '%s';", salary, name);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
		return this.updateEmployeeDataUsingPrepareStatement(name, salary);
	}

	private int updateEmployeeDataUsingPrepareStatement(String name, double salary) {
		String sql = String.format("update employee_payroll set basic_pay= %.2f where name = '%s';", salary, name);
		try (Connection connection = this.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		List<EmployeePayrollData> employeePayrollData = null;
		if (this.employeePayrollDataStatement == null)
			this.preparedStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollData = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollData;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet result) {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("basic_pay");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private void preparedStatementForEmployeeData() {
		try {
			Connection con = this.getConnection();
			String sql = "select * from employee_payroll where name = ?";
			employeePayrollDataStatement = con.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<EmployeePayrollData> getEmployeeForGivenDateRange(LocalDate startDate, LocalDate endDate) {
		String sql = String.format("select * from employee_payroll where start between '%s' and '%s';", startDate,
				endDate);
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			try {
				while (result.next()) {
					int id = result.getInt("id");
					String name = result.getString("name");
					double salary = result.getDouble("basic_pay");
					LocalDate start = result.getDate("start").toLocalDate();
					employeePayrollList.add(new EmployeePayrollData(id, name, salary, start));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return employeePayrollList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	public Map<String, Double> getAverageSalaryByGender() {
		String sql = "select gender,avg(basic_pay) as avg_salary from employee_payroll group by gender;";
		Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String gender = result.getString("gender");
				double salary = result.getDouble("avg_salary");
				genderToAverageSalaryMap.put(gender, salary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return genderToAverageSalaryMap;
	}

	public EmployeePayrollData addEmployee(String name, double salary, LocalDate startDate, String gender) {
		String sql = String.format(
				"insert into employee_payroll (name,basic_pay,start,gender) values ('%s',%.2f,'%s','%s');", name,
				salary, startDate.toString(), gender);
		int empId = -1;
		EmployeePayrollData employeePayrollData = null;
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowsAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					empId = resultSet.getInt(1);
			}
			employeePayrollData = new EmployeePayrollData(empId, name, salary, startDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollData;
	}

	public EmployeePayrollData addEmployeeToPayroll(String name,int compId, double salary, LocalDate startDate, String gender,String ph_no,String add) {
		int empId = -1;
		EmployeePayrollData employeePayrollData = null;
		Connection connection = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"insert into employee (name,comp_id,phone_number,address,start,gender) values ('%s',%d,'%s','%s','%s','%s');", name,compId,
					ph_no,add, startDate.toString(), gender);
			int rowsAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowsAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					empId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
				return employeePayrollData;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format(
					"insert into payroll (emp_id,basic_pay,deductions,taxable_pay,tax,net_pay) values (%d,%s,%s,%s,%s,%s);",
					empId,salary, deductions, taxablePay, tax, netPay);
			int rowsAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowsAffected == 1) {
				employeePayrollData = new EmployeePayrollData(empId, name, salary, startDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public EmployeePayrollData addEmployeeToPayrollERDiagram(String name,int compId, double salary, LocalDate startDate,
			String gender,String ph_no, String address) {
		int empId = -1;
		EmployeePayrollData employeePayrollData = null;
		Connection connection = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"insert into employee (name,comp_id,phone_number,address,start,gender) values ('%s',%d,'%s','%s','%s','%s');", name,compId,
					ph_no,address, startDate.toString(), gender);
			int rowsAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowsAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					empId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
				return employeePayrollData;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format(
					"insert into payroll (emp_id,basic_pay,deductions,taxable_pay,tax,net_pay) values (%d,%s,%s,%s,%s,%s);",
					empId,salary, deductions, taxablePay, tax, netPay);
			int rowsAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowsAffected == 1) {
				employeePayrollData = new EmployeePayrollData(empId, name, salary, startDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return employeePayrollData;
	}

	public void removeEmployee(String name) {
		int empId = -1;
		EmployeePayrollData employeePayrollData = null;
		Connection connection = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		int employeeId = 0;
//		try (Statement statement = connection.createStatement()) {
//			String sql = String.format("select * from employee where name = '%s';", name);
//			ResultSet resultSet = statement.executeQuery(sql);
//			if (resultSet.next()) {
//				employeeId = resultSet.getInt(1);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			try {
//				connection.rollback();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format("update employee set status = 0 where name = %s;", name);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
}
