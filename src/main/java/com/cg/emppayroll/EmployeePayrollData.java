package com.cg.emppayroll;

import java.time.LocalDate;

public class EmployeePayrollData {

	private int id;
	private String name;
	private double sal;
	private LocalDate startDate;

	public EmployeePayrollData(int id, String name, double sal) {
		this.id = id;
		this.name = name;
		this.sal = sal;
	}

	public EmployeePayrollData(int id2, String name2, double salary, LocalDate startDate) {
		this(id2, name2, salary);
		this.startDate = startDate;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getSal() {
		return sal;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSal(double sal) {
		this.sal = sal;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Override
	public String toString() {
		return "id:" + id + ", name:" + name + ", salary:" + sal;
	}

}
