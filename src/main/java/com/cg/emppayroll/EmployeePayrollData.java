package com.cg.emppayroll;

public class EmployeePayrollData {
	
	private int id;
	private String name;
	private double sal;
	public EmployeePayrollData(int id, String name, double sal) {
		this.id = id;
		this.name = name;
		this.sal = sal;
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
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSal(double sal) {
		this.sal = sal;
	}
	@Override
	public String toString() {
		return "id:"+id+", name:"+name+", salary:"+sal;
	}
	
	

}
