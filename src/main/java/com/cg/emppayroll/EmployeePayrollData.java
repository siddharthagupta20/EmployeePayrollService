package com.cg.emppayroll;

import java.time.LocalDate;

public class EmployeePayrollData {

	private int id;
	private String name;
	private double sal;
	private LocalDate startDate;
	private String gender;
	private int compId;
	private String phNo;
	private String address;
	

	public EmployeePayrollData(int id, String name, double sal) {
		this.id = id;
		this.name = name;
		this.sal = sal;
	}

	public EmployeePayrollData(int id2, String name2, double salary, LocalDate startDate) {
		this(id2, name2, salary);
		this.startDate = startDate;
	}

	public EmployeePayrollData(int id2, String name2, String gender, double salary, LocalDate startDate,int compId,String phNo, String address) {
		this(id2, name2, salary, startDate);
		this.gender = gender;
		this.compId=compId;
		this.address=address;
		this.phNo=phNo;
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

	public String getGender() {
		return gender;
	}
	public String getAddress() {
		return address;
	}
	public int getCompId() {
		return compId;
	}
	public String getPhNo() {
		return phNo;
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

	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setCompId(int compId) {
		this.compId = compId;
	}
	public void setPhNo(String phNo) {
		this.phNo = phNo;
	}

	@Override
	public String toString() {
		return "id:" + id + ", name:" + name + ", salary:" + sal;
	}

}
