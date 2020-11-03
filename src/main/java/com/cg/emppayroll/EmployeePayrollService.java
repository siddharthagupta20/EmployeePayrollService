package com.cg.emppayroll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeePayrollService {

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	private List<EmployeePayrollData> list;
	private EmployeePayrollDBService employeePayrollDBService;

	public EmployeePayrollService() {
		list = new ArrayList<EmployeePayrollData>();
		employeePayrollDBService = EmployeePayrollDBService.getInstance();

	}

	public EmployeePayrollService(List<EmployeePayrollData> list) {
		this.list = list;
	}

	public void readEmpPayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO)) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter employee id: ");
			int id = sc.nextInt();
			sc.nextLine();
			System.out.println("Enter employee name: ");
			String name = sc.nextLine();
			System.out.println("Enter employee salary: ");
			double sal = sc.nextDouble();
			list.add(new EmployeePayrollData(id, name, sal));
		} else if (ioService.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().readData();
		else if (ioService.equals(IOService.DB_IO))
			list = employeePayrollDBService.readData();
	}

	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
		readEmpPayrollData(ioService);
		return list;
	}

	public List<EmployeePayrollData> readEmployeeData(IOService ioService) {
		list = employeePayrollDBService.readEmployeeData();
		return list;
	}

	public void updateEmployeeSalary(String name, double salary) {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSal(salary);
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		for (EmployeePayrollData data : list) {
			if (data.getName().equals(name)) {
				return data;
			}
		}
		return null;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name, double salary) {
		for (EmployeePayrollData data : list) {
			if (data.getName().equals(name)) {
				if (Double.compare(data.getSal(), salary) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public void updateEmployeeSalaryUsingPrepareStatement(String name, double salary) {
		int result = employeePayrollDBService.updateEmployeeDataUsingPreparedStatement(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSal(salary);
	}

	public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService dbIo, LocalDate startDate,
			LocalDate endDate) {
		if (dbIo.equals(IOService.DB_IO)) {
			return employeePayrollDBService.getEmployeeForGivenDateRange(startDate, endDate);
		}
		return null;
	}

	public Map<String, Double> readAverageSalaryByGender(IOService dbIo) {
		if (dbIo.equals(IOService.DB_IO)) {
			return employeePayrollDBService.getAverageSalaryByGender();
		}
		return null;
	}

	public void addEmployeeToPayroll(String name, int compId, double salary, LocalDate startDate, String gender,
			String ph_no, String address) {
		EmployeePayrollData employeePayrollData = employeePayrollDBService.addEmployeeToPayroll(name, compId, salary,
				startDate, gender, ph_no, address);
		list.add(employeePayrollData);
	}

	public void addEmployeeToPayrollERDiagram(String name,int compId, double salary, LocalDate startDate, String gender,
			String ph_no, String address) {
		EmployeePayrollData employeePayrollData = employeePayrollDBService.addEmployeeToPayrollERDiagram(name, compId,salary,
				startDate, gender, ph_no, address);
		list.add(employeePayrollData);
	}

	public void removeEmployee(String name) {
		list.remove(this.getEmployeePayrollData(name));
		employeePayrollDBService.removeEmployee(name);
	}

	public void writeEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println(list);
		else if (ioService.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().writeData(this.list);
	}

	public long countEntries(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			return new EmployeePayrollFileIOService().countEntries();
		if(ioService.equals(IOService.DB_IO)){
			return employeePayrollDBService.countEntries();
		}
		return 0;
	}

	public void printData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			new EmployeePayrollFileIOService().printData();
	}
	public void addEmployeesToPayroll(List<EmployeePayrollData> employeePayrollDataList) {
		employeePayrollDataList.forEach(employeePayrollData -> {
			System.out.println("Employee Being Added: " + employeePayrollData.getName());
			this.addEmployeeToPayroll(employeePayrollData.getName(),employeePayrollData.getCompId(),employeePayrollData.getSal(),
					employeePayrollData.getStartDate(), employeePayrollData.getGender(),employeePayrollData.getPhNo(),employeePayrollData.getAddress());
			System.out.println("Employee Added: " + employeePayrollData.getName());
		});
		System.out.println(this.list);
	}

	public void addEmployeesToPayrollWithThreads(List<EmployeePayrollData> employeePayrollDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		employeePayrollDataList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
				System.out.println("Employee Being Added: " + Thread.currentThread().getName());
				this.addEmployeeToPayroll(employeePayrollData.getName(),employeePayrollData.getCompId(),employeePayrollData.getSal(),
						employeePayrollData.getStartDate(), employeePayrollData.getGender(),employeePayrollData.getPhNo(),employeePayrollData.getAddress());
				employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
				System.out.println("Employee Added: " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, employeePayrollData.getName());
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public void addEmployeesToERDBWithThreads(List<EmployeePayrollData> employeePayrollDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		employeePayrollDataList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
				System.out.println("Employee Being Added: " + Thread.currentThread().getName());
				this.addEmployeeToPayroll(employeePayrollData.getName(),employeePayrollData.getCompId(),employeePayrollData.getSal(),
						employeePayrollData.getStartDate(), employeePayrollData.getGender(),employeePayrollData.getPhNo(),employeePayrollData.getAddress());
				System.out.println("Employee Added: " + Thread.currentThread().getName());
				employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
			};
			Thread thread = new Thread(task, employeePayrollData.getName());
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public void UpdateEmployeeDataInERDBWithThreads(List<EmployeePayrollData> employeePayrollDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		employeePayrollDataList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
				System.out.println("Employee Being Updated: " + Thread.currentThread().getName());
				this.updateEmployeeSalary(employeePayrollData.getName(), employeePayrollData.getSal());
				System.out.println("Employee Updated: " + Thread.currentThread().getName());
				employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
			};
			Thread thread = new Thread(task, employeePayrollData.getName());
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
}
