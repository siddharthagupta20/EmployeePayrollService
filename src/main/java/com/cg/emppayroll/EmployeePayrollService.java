package com.cg.emppayroll;

import java.util.ArrayList;
import java.util.List;

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

}
