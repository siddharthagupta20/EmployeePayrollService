package com.cg.emppayroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	private List<EmployeePayrollData> list;

	public EmployeePayrollService() {
		list = new ArrayList<EmployeePayrollData>();
	}

	public EmployeePayrollService(List<EmployeePayrollData> list) {
		this.list = list;
	}

	public void readEmpPayrollData(Scanner sc) {
		System.out.println("Enter employee id: ");
		int id = sc.nextInt();
		sc.nextLine();
		System.out.println("Enter employee name: ");
		String name = sc.nextLine();
		System.out.println("Enter employee salary: ");
		double sal = sc.nextDouble();
		list.add(new EmployeePayrollData(id, name, sal));
	}

	public void writeEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println(list);
		else if (ioService.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().writeData(this.list);
	}
	public long countEntries(IOService ioService) {
		if(ioService.equals(IOService.FILE_IO))
			return new EmployeePayrollFileIOService().countEntries();
		return 0;
	}
}
