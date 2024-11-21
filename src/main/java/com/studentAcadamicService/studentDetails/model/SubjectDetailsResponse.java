package com.studentAcadamicService.studentDetails.model;

public class SubjectDetailsResponse {
	
	private Long id;
	private String subjectName;
    private String department;
    private Long credits;
    
    private String error;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
    
}
