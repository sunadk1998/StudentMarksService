package com.studentAcadamicService.studentDetails.model;

public class AddSubjectDetailsRequest {
	
	private String subjectName;
	private String department;
	private Long credits;
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Long getCredits() {
		return credits;
	}
	public void setCredits(Long credits) {
		this.credits = credits;
	}
	
}
