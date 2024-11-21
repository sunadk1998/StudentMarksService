package com.studentAcadamicService.studentDetails.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "StudentMarksDetails")
@Table(name = "StudentMarksDetails")
public class StudentMarksDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long studentId;
    private Long subjectId;
    private Long semester;
    private Long marks;
    private String grade;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
