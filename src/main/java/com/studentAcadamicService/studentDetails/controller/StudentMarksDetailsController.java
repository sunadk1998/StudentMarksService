package com.studentAcadamicService.studentDetails.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentAcadamicService.studentDetails.Entity.StudentMarksDetails;
import com.studentAcadamicService.studentDetails.Entity.SubjectDetails;
import com.studentAcadamicService.studentDetails.model.AddStudentMarksRequest;
import com.studentAcadamicService.studentDetails.model.StudentMarksDetailsReponse;
import com.studentAcadamicService.studentDetails.model.UpdateStudentMarksRequest;
import com.studentAcadamicService.studentDetails.proxy.StudentProxy;
import com.studentAcadamicService.studentDetails.repository.StudentMarksDetailsRepository;
import com.studentAcadamicService.studentDetails.repository.SubjectDetailsRepository;


@RestController
@RequestMapping("/studentAcadamics/marks")
public class StudentMarksDetailsController {
	
	StudentProxy studentProxy = new StudentProxy(new RestTemplate());
	
	@Autowired
	SubjectDetailsRepository subjectDetailsRepository;
	
	@Autowired
	StudentMarksDetailsRepository studentMarksDetailsRepository;
	
	private static final String VALID_API_KEY = "test";
	
	@GetMapping
	public String getStudentId(Long id, String token) throws Exception{
		String resultString = null;
		ResponseEntity<Object> objectss = studentProxy.getStudentDetails(id, token);
		if (Objects.nonNull(objectss)) {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode node = objectMapper.convertValue(objectss, JsonNode.class);
			for (JsonNode jsonNode : node) {
				JsonNode code = jsonNode.findValue("StudentID");
				if (Objects.nonNull(code)) {
					resultString = code.asText();
				}
			}
		}
		return resultString;
	}
	
	@PostMapping
	public ResponseEntity<StudentMarksDetailsReponse> addMarks(@RequestHeader(value = "Authorization", required = true) String value, @RequestHeader(value = "api-key", required = true) String apiKey, @RequestBody AddStudentMarksRequest addStudentMarksRequest) {
		try {
			if (!VALID_API_KEY.equals(apiKey)) {
	            throw new Exception("Invalid API key");
	        }
			
			if (Objects.isNull(addStudentMarksRequest.getStudentId()) ||
					Objects.isNull(addStudentMarksRequest.getSubjectId()) ||
					Objects.isNull(addStudentMarksRequest.getSemester()) ||
					Objects.isNull(addStudentMarksRequest.getGrade()) ||
					Objects.isNull(addStudentMarksRequest.getMarks())) {
				throw new Exception("Mandatory fields are missing");
			}
			
			String studentId = getStudentId(addStudentMarksRequest.getStudentId(), value);
			
			if (Objects.isNull(studentId)) {
				throw new Exception("Student Details not found");
			}
			
			Optional<SubjectDetails> subjectDetailsOptional = subjectDetailsRepository.findById(addStudentMarksRequest.getSubjectId());
			if (Objects.nonNull(subjectDetailsOptional)) {
				SubjectDetails subjectDetails = subjectDetailsOptional.get();
				if (Objects.isNull(subjectDetails)) {
					throw new Exception("Subject Details not found");
				}
						
			} else {
				throw new Exception("Subject Details not found");
			}
			
			StudentMarksDetails studentMarksDetails = new StudentMarksDetails();
			
			studentMarksDetails.setStudentId(addStudentMarksRequest.getStudentId());
			studentMarksDetails.setSubjectId(addStudentMarksRequest.getSubjectId());
			studentMarksDetails.setSemester(addStudentMarksRequest.getSemester());
			studentMarksDetails.setGrade(addStudentMarksRequest.getGrade());
			studentMarksDetails.setMarks(addStudentMarksRequest.getMarks());
			
			studentMarksDetails = studentMarksDetailsRepository.save(studentMarksDetails);
			StudentMarksDetailsReponse studentMarksDetailsReponse = new StudentMarksDetailsReponse();
			if (Objects.nonNull(studentMarksDetails)) {
				
				studentMarksDetailsReponse.setId(studentMarksDetails.getId());
				studentMarksDetailsReponse.setStudentId(studentMarksDetails.getStudentId());
				studentMarksDetailsReponse.setSubjectId(studentMarksDetails.getSubjectId());
				studentMarksDetailsReponse.setSemester(studentMarksDetails.getSemester());
				studentMarksDetailsReponse.setGrade(studentMarksDetails.getGrade());
				studentMarksDetailsReponse.setMarks(studentMarksDetails.getMarks());
			}
			return ResponseEntity.ok(studentMarksDetailsReponse);
		} catch (Exception e) {
			StudentMarksDetailsReponse studentMarksDetailsReponse = new StudentMarksDetailsReponse();
			studentMarksDetailsReponse.setError(e.getMessage());
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(studentMarksDetailsReponse);
		}
		
	}
	
	@PatchMapping
	public ResponseEntity<StudentMarksDetailsReponse> updateMarks(@RequestHeader(value = "Authorization", required = true) String value, @RequestHeader(value = "api-key", required = true) String apiKey, @RequestBody UpdateStudentMarksRequest updateStudentMarksRequest) {
		try {
			if (!VALID_API_KEY.equals(apiKey)) {
	            throw new Exception("Invalid API key");
	        }
			
			if (Objects.isNull(updateStudentMarksRequest.getId())) {
				throw new Exception("ID is missing");
			}
			
			Optional<StudentMarksDetails> studentMarksDetailsOptional = studentMarksDetailsRepository.findById(updateStudentMarksRequest.getId());
			StudentMarksDetails studentMarksDetails = null;
			StudentMarksDetailsReponse studentMarksDetailsReponse = new StudentMarksDetailsReponse();
			if (Objects.nonNull(studentMarksDetailsOptional)) {
				studentMarksDetails = studentMarksDetailsOptional.get();
				if (Objects.isNull(studentMarksDetails)) {
					throw new Exception("Student Marks Details not found");
				}
			} else {
				throw new Exception("Student Marks Details not found");
			}
			
			if (Objects.nonNull(updateStudentMarksRequest.getStudentId())) {
				String studentId = getStudentId(updateStudentMarksRequest.getStudentId(), value);
				
				if (Objects.isNull(studentId)) {
					throw new Exception("Student Details not found");
				}
				
				studentMarksDetails.setStudentId(updateStudentMarksRequest.getStudentId());
			}
			
			if (Objects.nonNull(updateStudentMarksRequest.getSubjectId())) {
				Optional<SubjectDetails> subjectDetailsOptional = subjectDetailsRepository.findById(updateStudentMarksRequest.getSubjectId());
				if (Objects.nonNull(subjectDetailsOptional)) {
					SubjectDetails subjectDetails = subjectDetailsOptional.get();
					if (Objects.isNull(subjectDetails)) {
						throw new Exception("Subject Details not found");
					}							
				} else {
					throw new Exception("Subject Details not found");
				}
				studentMarksDetails.setSubjectId(updateStudentMarksRequest.getSubjectId());
			}
			
			if (Objects.nonNull(updateStudentMarksRequest.getSemester())) {
				studentMarksDetails.setSemester(updateStudentMarksRequest.getSemester());
			}
			
			
			if (Objects.nonNull(updateStudentMarksRequest.getGrade())) {
				studentMarksDetails.setGrade(updateStudentMarksRequest.getGrade());
			}
			
			if (Objects.nonNull(updateStudentMarksRequest.getMarks())) {
				studentMarksDetails.setMarks(updateStudentMarksRequest.getMarks());
			}
			
			studentMarksDetails = studentMarksDetailsRepository.save(studentMarksDetails);

			if (Objects.nonNull(studentMarksDetails)) {
				
				studentMarksDetailsReponse.setId(studentMarksDetails.getId());
				studentMarksDetailsReponse.setStudentId(studentMarksDetails.getStudentId());
				studentMarksDetailsReponse.setSubjectId(studentMarksDetails.getSubjectId());
				studentMarksDetailsReponse.setSemester(studentMarksDetails.getSemester());
				studentMarksDetailsReponse.setGrade(studentMarksDetails.getGrade());
				studentMarksDetailsReponse.setMarks(studentMarksDetails.getMarks());
			}
			return ResponseEntity.ok(studentMarksDetailsReponse);
		} catch (Exception e) {
			StudentMarksDetailsReponse studentMarksDetailsReponse = new StudentMarksDetailsReponse();
			studentMarksDetailsReponse.setError(e.getMessage());
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(studentMarksDetailsReponse);
		}
		
	}
	
	@GetMapping("/student/{studentId}")
	public ResponseEntity<List<StudentMarksDetailsReponse>> getStudentMarksDetailsByStudentId (@RequestHeader(value = "Authorization", required = true) String value, @RequestHeader(value = "api-key", required = true) String apiKey, @PathVariable(name = "studentId") Long studentId) {
		try {
			if (!VALID_API_KEY.equals(apiKey)) {
	            throw new Exception("Invalid API key");
	        }
			
			if (Objects.nonNull(studentId)) {
				String studentIdVal = getStudentId(studentId, value);
				
				if (Objects.isNull(studentIdVal)) {
					throw new Exception("Student Details not found");
				}
			}	
			List<StudentMarksDetailsReponse> studentMarksDetailsReponses = new ArrayList<StudentMarksDetailsReponse>();
			List<StudentMarksDetails> studentMarksDetailsList = studentMarksDetailsRepository.findByStudentId(studentId);
			if (Objects.isNull(studentMarksDetailsList) || studentMarksDetailsList.isEmpty()) {
				throw new Exception("Student Marks Details not found");
			}
			for (StudentMarksDetails studentMarksDetails : studentMarksDetailsList) {
				StudentMarksDetailsReponse studentMarksDetailsReponse = new StudentMarksDetailsReponse();
				studentMarksDetailsReponse.setId(studentMarksDetails.getId());
				studentMarksDetailsReponse.setStudentId(studentMarksDetails.getStudentId());
				studentMarksDetailsReponse.setSubjectId(studentMarksDetails.getSubjectId());
				studentMarksDetailsReponse.setSemester(studentMarksDetails.getSemester());
				studentMarksDetailsReponse.setGrade(studentMarksDetails.getGrade());
				studentMarksDetailsReponse.setMarks(studentMarksDetails.getMarks());
				studentMarksDetailsReponses.add(studentMarksDetailsReponse);
			}
			return ResponseEntity.ok(studentMarksDetailsReponses);
		} catch (Exception e) {
			// TODO: handle exception
			List<StudentMarksDetailsReponse> studentMarksDetailsReponses = new ArrayList<StudentMarksDetailsReponse>();
			StudentMarksDetailsReponse studentMarksDetailsReponse = new StudentMarksDetailsReponse();
			studentMarksDetailsReponse.setError(e.getMessage());
			studentMarksDetailsReponses.add(studentMarksDetailsReponse);
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(studentMarksDetailsReponses);
		}
		
	}
	
	@GetMapping("/subject/{subjectId}")
	public ResponseEntity<List<StudentMarksDetailsReponse>> getStudentMarksDetailsBySubjectId (@RequestHeader(value = "Authorization", required = true) String value, @RequestHeader(value = "api-key", required = true) String apiKey, @PathVariable(name = "subjectId") Long subjectId) {
		try {
			if (!VALID_API_KEY.equals(apiKey)) {
	            throw new Exception("Invalid API key");
	        }
			
			if (Objects.nonNull(subjectId)) {
				Optional<SubjectDetails> subjectDetailsOptional = subjectDetailsRepository.findById(subjectId);
				if (Objects.nonNull(subjectDetailsOptional)) {
					SubjectDetails subjectDetails = subjectDetailsOptional.get();
					if (Objects.isNull(subjectDetails)) {
						throw new Exception("Subject Details not found");
					}							
				} else {
					throw new Exception("Subject Details not found");
				}
			}	
			List<StudentMarksDetailsReponse> studentMarksDetailsReponses = new ArrayList<StudentMarksDetailsReponse>();
			List<StudentMarksDetails> studentMarksDetailsList = studentMarksDetailsRepository.findBySubjectId(subjectId);
			if (Objects.isNull(studentMarksDetailsList) || studentMarksDetailsList.isEmpty()) {
				throw new Exception("Student Marks Details not found");
			}
			for (StudentMarksDetails studentMarksDetails : studentMarksDetailsList) {
				StudentMarksDetailsReponse studentMarksDetailsReponse = new StudentMarksDetailsReponse();
				studentMarksDetailsReponse.setId(studentMarksDetails.getId());
				studentMarksDetailsReponse.setStudentId(studentMarksDetails.getStudentId());
				studentMarksDetailsReponse.setSubjectId(studentMarksDetails.getSubjectId());
				studentMarksDetailsReponse.setSemester(studentMarksDetails.getSemester());
				studentMarksDetailsReponse.setGrade(studentMarksDetails.getGrade());
				studentMarksDetailsReponse.setMarks(studentMarksDetails.getMarks());
				studentMarksDetailsReponses.add(studentMarksDetailsReponse);
			}
			return ResponseEntity.ok(studentMarksDetailsReponses);
		} catch (Exception e) {
			// TODO: handle exception
			List<StudentMarksDetailsReponse> studentMarksDetailsReponses = new ArrayList<StudentMarksDetailsReponse>();
			StudentMarksDetailsReponse studentMarksDetailsReponse = new StudentMarksDetailsReponse();
			studentMarksDetailsReponse.setError(e.getMessage());
			studentMarksDetailsReponses.add(studentMarksDetailsReponse);
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(studentMarksDetailsReponses);
		}
		
	}
	
	@GetMapping("/average/subject/{subjectId}/{semester}")
	public ResponseEntity<String> getAverageStudentMarksDetailsBySubjectId (@RequestHeader(value = "Authorization", required = true) String value, @RequestHeader(value = "api-key", required = true) String apiKey, @PathVariable(name = "subjectId") Long subjectId, @PathVariable(name = "semester") Long semester ) {
		try {
			if (!VALID_API_KEY.equals(apiKey)) {
	            throw new Exception("Invalid API key");
	        }
			
			if (Objects.nonNull(subjectId)) {
				Optional<SubjectDetails> subjectDetailsOptional = subjectDetailsRepository.findById(subjectId);
				if (Objects.nonNull(subjectDetailsOptional)) {
					SubjectDetails subjectDetails = subjectDetailsOptional.get();
					if (Objects.isNull(subjectDetails)) {
						throw new Exception("Subject Details not found");
					}							
				} else {
					throw new Exception("Subject Details not found");
				}
			} else {
				throw new Exception("Subject ID not found");
			}
			
			if (Objects.isNull(semester)) {
				throw new Exception("Semester value not found");
			}
			List<StudentMarksDetails> studentMarksDetailsList = studentMarksDetailsRepository.findBySubjectIdAndSemester(subjectId, semester);
			Float sum = Float.parseFloat("0");
			if (Objects.isNull(studentMarksDetailsList) || studentMarksDetailsList.isEmpty()) {
				throw new Exception("Student Marks Details not found");
			}
			for (StudentMarksDetails studentMarksDetails : studentMarksDetailsList) {
				sum += studentMarksDetails.getMarks();
			}
			return ResponseEntity.ok(String.valueOf(sum/studentMarksDetailsList.size()));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/average/student/{studentId}/{semester}")
	public ResponseEntity<String> getAverageStudentMarksDetailsByStudentId (@RequestHeader(value = "Authorization", required = true) String value, @RequestHeader(value = "api-key", required = true) String apiKey, @PathVariable(name = "studentId") Long studentId, @PathVariable(name = "semester") Long semester ) {
		try {
			if (!VALID_API_KEY.equals(apiKey)) {
	            throw new Exception("Invalid API key");
	        }
			
			if (Objects.nonNull(studentId)) {
				String studentIdVal = getStudentId(studentId, value);
				
				if (Objects.isNull(studentIdVal)) {
					throw new Exception("Student Details not found");
				}
			} else {
				throw new Exception("Student ID not found");
			}
			
			if (Objects.isNull(semester)) {
				throw new Exception("Semester value not found");
			}
			List<StudentMarksDetails> studentMarksDetailsList = studentMarksDetailsRepository.findByStudentIdAndSemester(studentId, semester);
			Float sum = Float.parseFloat("0");
			if (Objects.isNull(studentMarksDetailsList) || studentMarksDetailsList.isEmpty()) {
				throw new Exception("Student Marks Details not found");
			}
			for (StudentMarksDetails studentMarksDetails : studentMarksDetailsList) {
				sum += studentMarksDetails.getMarks();
			}
			return ResponseEntity.ok(String.valueOf(sum/studentMarksDetailsList.size()));
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
	
	

}
