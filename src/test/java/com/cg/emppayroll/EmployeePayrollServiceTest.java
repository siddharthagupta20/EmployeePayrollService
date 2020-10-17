package com.cg.emppayroll;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollServiceTest {
	
	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchNumberOfEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmployees = { 
				new EmployeePayrollData(1, "John Doe", 1000.0),
				new EmployeePayrollData(2, "Kalyan Arigela", 1100.0),
				new EmployeePayrollData(3, "Anand Kumar", 1500.0) };
		EmployeePayrollService empPayrollService; 
		empPayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
		empPayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
		empPayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
		Assert.assertEquals(3, empPayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO));
	}
}
