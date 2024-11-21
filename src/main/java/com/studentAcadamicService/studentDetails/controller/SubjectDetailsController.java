package com.studentAcadamicService.studentDetails.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentAcadamicService.studentDetails.Entity.SubjectDetails;
import com.studentAcadamicService.studentDetails.model.AddSubjectDetailsRequest;
import com.studentAcadamicService.studentDetails.model.SubjectDetailsResponse;
import com.studentAcadamicService.studentDetails.model.UpdateSubjectDetailsRequest;
import com.studentAcadamicService.studentDetails.repository.SubjectDetailsRepository;

@RestController
@RequestMapping("/studentAcadamics/subject")
public class SubjectDetailsController {
	
	@Autowired
	SubjectDetailsRepository subjectDetailsRepository;
	
	private static final String VALID_API_KEY = "test";
	
	@GetMapping("/all")
    public ResponseEntity<List<SubjectDetailsResponse>> getAllSubjects(@RequestHeader(value = "api-key", required = true) String apiKey) {
    	
		List<SubjectDetailsResponse> subjectDetailsResponses = new ArrayList<SubjectDetailsResponse>();
		try {
			if (!VALID_API_KEY.equals(apiKey)) {
	            throw new Exception("Invalid API key");
	        }
	        List<SubjectDetails> subjectDetailsList = subjectDetailsRepository.findAll();
	        
	        if (Objects.nonNull(subjectDetailsList) && !subjectDetailsList.isEmpty()) {
	        	for (SubjectDetails subjectDetail : subjectDetailsList) {
					
	        		SubjectDetailsResponse subjectDetailsResponse = new SubjectDetailsResponse();
	        		
	        		subjectDetailsResponse.setId(subjectDetail.getId());
	        		subjectDetailsResponse.setSubjectName(subjectDetail.getSubjectName());
	        		subjectDetailsResponse.setDepartment(subjectDetail.getDepartment());
	        		subjectDetailsResponse.setCredits(subjectDetail.getCredits());
	        		
	        		subjectDetailsResponses.add(subjectDetailsResponse);
				}
			}
			return ResponseEntity.ok(subjectDetailsResponses);
			
		} catch (Exception e) {
			// TODO: handle exception
			SubjectDetailsResponse subjectDetailsResponse = new SubjectDetailsResponse();
			subjectDetailsResponse.setError(e.getMessage());	
			subjectDetailsResponses.add(subjectDetailsResponse);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(subjectDetailsResponses);
		}
    }
	
	@PostMapping()
	public SubjectDetailsResponse addSubjectDetails (@RequestHeader(value = "api-key", required = true) String apiKey, @RequestBody AddSubjectDetailsRequest addSubjectDetailsRequest) {
		
		SubjectDetailsResponse subjectDetailsResponse = new SubjectDetailsResponse();
		try {
		
			if (Objects.isNull(addSubjectDetailsRequest) || Objects.isNull(addSubjectDetailsRequest.getSubjectName())
					|| Objects.isNull(addSubjectDetailsRequest.getDepartment()) || Objects.isNull(addSubjectDetailsRequest.getCredits())) {
				throw new Exception("Mandatory Fields are Missing");
			}
			
			SubjectDetails subjectDetails = new SubjectDetails();
			
			subjectDetails.setSubjectName(addSubjectDetailsRequest.getSubjectName());
			subjectDetails.setDepartment(addSubjectDetailsRequest.getDepartment());
			subjectDetails.setCredits(addSubjectDetailsRequest.getCredits());
			
			subjectDetails = subjectDetailsRepository.save(subjectDetails);
			
			subjectDetailsResponse.setId(subjectDetails.getId());
			subjectDetailsResponse.setSubjectName(subjectDetails.getSubjectName());
			subjectDetailsResponse.setDepartment(subjectDetails.getDepartment());
			subjectDetailsResponse.setCredits(subjectDetails.getCredits());
			return subjectDetailsResponse;
		} catch (Exception e) {
			// TODO: handle exception
			subjectDetailsResponse.setError(e.getMessage());
			return subjectDetailsResponse;
		}
		
	}
	
	@PatchMapping()
	public SubjectDetailsResponse updateSubjectDetails (@RequestHeader(value = "api-key", required = true) String apiKey, @RequestBody UpdateSubjectDetailsRequest updateSubjectDetailsRequest) {
		
		SubjectDetailsResponse subjectDetailsResponse = new SubjectDetailsResponse();
		try {
		
			if (Objects.isNull(updateSubjectDetailsRequest) || Objects.isNull(updateSubjectDetailsRequest.getId())) {
				throw new Exception("Mandatory Fields are Missing");
			}
			
			Optional<SubjectDetails> subjectDetailsOptional = subjectDetailsRepository.findById(updateSubjectDetailsRequest.getId());
			SubjectDetails subjectDetails = subjectDetailsOptional.get();
			
			if (Objects.isNull(subjectDetails)) {
				throw new Exception("Subject Details not found!!!");
			}
			
			if (Objects.nonNull(updateSubjectDetailsRequest.getSubjectName())) {
				subjectDetails.setSubjectName(updateSubjectDetailsRequest.getSubjectName());
			}
			if (Objects.nonNull(updateSubjectDetailsRequest.getDepartment())) {
				subjectDetails.setDepartment(updateSubjectDetailsRequest.getDepartment());
			}
			if (Objects.nonNull(updateSubjectDetailsRequest.getCredits())) {
				subjectDetails.setCredits(updateSubjectDetailsRequest.getCredits());
			}
			
			subjectDetails = subjectDetailsRepository.save(subjectDetails);
			
			subjectDetailsResponse.setId(subjectDetails.getId());
			subjectDetailsResponse.setSubjectName(subjectDetails.getSubjectName());
			subjectDetailsResponse.setDepartment(subjectDetails.getDepartment());
			subjectDetailsResponse.setCredits(subjectDetails.getCredits());
			
			return subjectDetailsResponse;
			
		} catch (Exception e) {
			// TODO: handle exception
			subjectDetailsResponse.setError(e.getMessage());
			return subjectDetailsResponse;
		}
		
	}
	
	@DeleteMapping("/{id}")
	public String deleteSubjectDetails (@RequestHeader(value = "api-key", required = true) String apiKey, @PathVariable(name = "id") Long id) {
		
		try {
		
			if (Objects.isNull(id)) {
				throw new Exception("Id not found in URL!!!");
			}
			
			Optional<SubjectDetails> subjectDetailsOptional = subjectDetailsRepository.findById(id);
			SubjectDetails subjectDetails = subjectDetailsOptional.get();
			
			if (Objects.isNull(subjectDetails)) {
				throw new Exception("Subject Details not found!!!");
			}
			
			subjectDetailsRepository.delete(subjectDetails);
			
			return "Deleted Successfully";
			
		} catch (Exception e) {
			// TODO: handle exception
			return "Error : " + e.getMessage();
		}
	}

}
