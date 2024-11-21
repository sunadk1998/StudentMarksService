package com.studentAcadamicService.studentDetails.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentAcadamicService.studentDetails.Entity.StudentMarksDetails;


public interface StudentMarksDetailsRepository extends JpaRepository<StudentMarksDetails, Long> {
    // Custom queries can be added here if needed
	
	List<StudentMarksDetails> findByStudentId(Long studentId);
	
	List<StudentMarksDetails> findByStudentIdAndSemester(Long studentId, Long semester);
	
	List<StudentMarksDetails> findBySubjectId(Long subjectId);
	
	List<StudentMarksDetails> findBySubjectIdAndSemester(Long subjectId, Long semester);
}
