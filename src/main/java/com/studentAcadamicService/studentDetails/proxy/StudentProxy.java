package com.studentAcadamicService.studentDetails.proxy;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentAcadamicService.studentDetails.model.TokenRequest;



public class StudentProxy {
	
	@Value(value = "${student.getById.url}")
	String studentGetByIdUrl;
	
	private RestTemplate restTemplate = null;
	
    public StudentProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    
	
	public ResponseEntity<Object> getStudentDetails(Long id, String token) throws Exception {
		
//		TokenRequest tokenRequest = new TokenRequest();	
//		String tokenURL = "https://us-central1-schoolstudentbasicdetails.cloudfunctions.net/api/student/get-login-admin";
//		HttpHeaders headers = new HttpHeaders();
////		String requestBody = """
////		            {
////		                "username": "admin3",
////		                "password": "e99a18c428cb38d5f260853678922e03"
////		            }
////		            """;
//		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//		ResponseEntity<Object> response = restTemplate.exchange(tokenURL, HttpMethod.POST, entity, Object.class);
//		if (Objects.nonNull(response)) {
//			ObjectMapper objectMapper = new ObjectMapper();
//			JsonNode node = objectMapper.convertValue(response, JsonNode.class);
//			JsonNode code = node.findValue("token");
//			if (Objects.nonNull(code)) {
//				token = code.asText();
//			}
//		}
//		
//		if (Objects.isNull(token)) {
//			throw new Exception("Token wasn't generated");
//		}
		HttpHeaders headers = new HttpHeaders();
//		token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluMyIsImlhdCI6MTczMjE5MDM3NywiZXhwIjoxNzMyMjExOTc3fQ.crZO2UKmpmYzTbJdqEAqT8b29g8eKSUuH_Pd-pThi1Y";
        headers.set("Authorization", token);

        // Create entity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        studentGetByIdUrl = "https://us-central1-schoolstudentbasicdetails.cloudfunctions.net/api/student/get-student-detail-by-id/" + id;
        ResponseEntity<Object>  response = restTemplate.exchange(studentGetByIdUrl, HttpMethod.GET, entity, Object.class);

        return response;
	}

}
