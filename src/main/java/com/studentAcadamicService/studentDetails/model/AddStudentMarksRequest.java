package com.studentAcadamicService.studentDetails.model;

public class AddStudentMarksRequest {
	
	private Long studentId;
    private Long subjectId;
    private Long semester;
    private Long marks;
    private String grade;
    
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	public Long getSemester() {
		return semester;
	}
	public void setSemester(Long semester) {
		this.semester = semester;
	}
	public Long getMarks() {
		return marks;
	}
	public void setMarks(Long marks) {
		this.marks = marks;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
    
}
