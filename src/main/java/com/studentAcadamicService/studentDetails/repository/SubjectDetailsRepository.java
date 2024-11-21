package com.studentAcadamicService.studentDetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentAcadamicService.studentDetails.Entity.SubjectDetails;

public interface SubjectDetailsRepository extends JpaRepository<SubjectDetails, Long> {
    // Custom queries can be added here if needed
}
