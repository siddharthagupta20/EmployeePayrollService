package com.cg.emppayroll;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.cg.emppayroll.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchNumberOfEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmployees = { new EmployeePayrollData(1, "John Doe", 1000.0),
				new EmployeePayrollData(2, "Kalyan Arigela", 1100.0),
				new EmployeePayrollData(3, "Anand Kumar", 1500.0) };
		EmployeePayrollService empPayrollService;
		empPayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
		empPayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
		empPayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
		Assert.assertEquals(3, empPayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO));
	}

	@Test
	public void givenEmpDataInFileShouldRead() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		empPayrollService.readEmpPayrollData(EmployeePayrollService.IOService.FILE_IO);
		long entries = empPayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}
	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		System.out.println(employeePayrollData);
		Assert.assertEquals(6, employeePayrollData.size());
	}
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa", 3000000.00);
		Assert.assertTrue(result);
	}
	@Test
	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalaryUsingPrepareStatement("Terisa", 2000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa", 2000000.00);
		Assert.assertTrue(result);
	}
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollForDateRange(IOService.DB_IO, startDate, endDate);
		Assert.assertEquals(6, employeePayrollData.size());
	}
	@Test
	public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(IOService.DB_IO);
		Assert.assertTrue(
				averageSalaryByGender.get("M").equals(2000000.00) && averageSalaryByGender.get("F").equals(3000000.00));
	}
	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWityhDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeeData(IOService.DB_IO);
		employeePayrollService.addEmployeeToPayroll("Mark", 2,5000000.00, LocalDate.now(), "M","1111111111","d");
		System.out.println(employeePayrollService.readEmployeeData(IOService.DB_IO));
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark", 5000000.00);
		Assert.assertTrue(result);
	}
	@Test
	public void givenNewEmployee_WhenAddedToPayroll_ShouldSyncWityhDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeeData(IOService.DB_IO);
		employeePayrollService.addEmployeeToPayrollERDiagram("Glen", 1,5000000.00, LocalDate.now(), "M","2222222222","e");
		System.out.println(employeePayrollService.readEmployeeData(IOService.DB_IO));
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Glen", 5000000.00);
		Assert.assertTrue(result);
	}

	@Test
	public void givenEmployee_WhenRemovedFromPayroll_ShouldSyncWityhDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeeData(IOService.DB_IO);
		employeePayrollService.removeEmployee("Glen");
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Glen", 5000000);
		Assert.assertFalse(result);
	}
	@Test
	public void given6Employees_WhenAddedToDB_ShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(0, "Jeff Bezos", "M", 100000.0, LocalDate.now(),1,"3333333333","f"),
				new EmployeePayrollData(0, "Bill Gates", "M", 200000.0, LocalDate.now(),1,"4444444444","g"),
				new EmployeePayrollData(0, "Mark Zuckerberg", "M", 300000.0, LocalDate.now(),1,"5555555555","h"),
				new EmployeePayrollData(0, "Sunder", "M", 600000.0, LocalDate.now(),1,"6666666666","i"),
				new EmployeePayrollData(0, "Mukesh", "M", 1000000.0, LocalDate.now(),1,"7777777777","j"),
				new EmployeePayrollData(0, "Anil", "M", 1000000.0, LocalDate.now(),1,"8888888888","k") };
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeeData(IOService.DB_IO);
		Instant start = Instant.now();
		employeePayrollService.addEmployeesToPayroll(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Durataion without Thread: " + Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeePayrollService.addEmployeesToPayrollWithThreads(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread: " + Duration.between(threadStart, threadEnd));
		Assert.assertEquals(15, employeePayrollService.countEntries(IOService.DB_IO));
	}
	@Test	
	public void given6Employees_WhenAddedToERDiagramDB_ShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(0, "Jeff Bezos", "M", 100000.0, LocalDate.now(),1,"3333333333","f"),
				new EmployeePayrollData(0, "Bill Gates", "M", 200000.0, LocalDate.now(),1,"4444444444","g"),
				new EmployeePayrollData(0, "Mark Zuckerberg", "M", 300000.0, LocalDate.now(),1,"5555555555","h"),
				new EmployeePayrollData(0, "Sunder", "M", 600000.0, LocalDate.now(),1,"6666666666","i"),
				new EmployeePayrollData(0, "Mukesh", "M", 1000000.0, LocalDate.now(),1,"7777777777","j"),
				new EmployeePayrollData(0, "Anil", "M", 1000000.0, LocalDate.now(),1,"8888888888","j") };
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeeData(IOService.DB_IO);
		Instant start = Instant.now();
		employeePayrollService.addEmployeesToPayroll(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Durataion without Thread: " + Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeePayrollService.addEmployeesToERDBWithThreads(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread: " + Duration.between(threadStart, threadEnd));
		Assert.assertEquals(15, employeePayrollService.countEntries(IOService.DB_IO));
	}

	@Test
	public void given6Employees_WhenUpdatedDataInERDiagramImplementedDB_ShouldBeInSync() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(0, "Jeff Bezos", "M", 10000.0, LocalDate.now(),1,"3333333333","f"),
				new EmployeePayrollData(0, "Bill Gates", "M", 20000.0, LocalDate.now(),1,"4444444444","g"),
				new EmployeePayrollData(0, "Mark Zuckerberg", "M", 30000.0, LocalDate.now(),1,"5555555555","h"),
				new EmployeePayrollData(0, "Sunder", "M", 60000.0, LocalDate.now(),1,"6666666666","i"),
				new EmployeePayrollData(0, "Mukesh", "M", 1000000.0, LocalDate.now(),1,"7777777777","j"),
				new EmployeePayrollData(0, "Anil", "M", 1000000.0, LocalDate.now(),1,"8888888888","j") };
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeeData(IOService.DB_IO);
		Instant threadStart = Instant.now();
		employeePayrollService.UpdateEmployeeDataInERDBWithThreads(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread: " + Duration.between(threadStart, threadEnd));
		int totalUpdated = 0;
		for (EmployeePayrollData data : Arrays.asList(arrayOfEmps)) {
			boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB(data.getName(), data.getSal());
			if (result)
				totalUpdated++;
		}
		Assert.assertEquals(2, totalUpdated);
	}
}
