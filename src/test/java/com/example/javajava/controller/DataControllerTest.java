package com.example.javajava.controller;

import com.example.javajava.model.TravelTime;
import com.example.javajava.service.CsvDataService;
import com.example.javajava.service.DataStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DataControllerTest {
	
	@Mock
	private CsvDataService csvDataService;
	
	@Mock
	private DataStorageService dataStorageService;
	
	private DataController dataController;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		dataController = new DataController(csvDataService, dataStorageService);
	}
	
	@Test
	void shouldLoadDataFromCSVFile() throws Exception {
		String csvContent = "R11;R12;20\nR12;R13;9";
		MockMultipartFile file = new MockMultipartFile(
		"file", 
		"travel_times.csv", 
		"text/csv", 
		csvContent.getBytes()
		);
		
		List<TravelTime> mockTravelTimes = Arrays.asList(
		new TravelTime("R11", "R12", 20),
		new TravelTime("R12", "R13", 9)
		);
		
		when(csvDataService.parseCSVContent(csvContent)).thenReturn(mockTravelTimes);
		
		ResponseEntity<Map<String, Object>> response = dataController.loadData(file);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("Data loaded successfully", body.get("message"));
		assertEquals(2, body.get("recordsProcessed"));
		
		verify(csvDataService).parseCSVContent(csvContent);
		verify(dataStorageService).storeTravelTimes(mockTravelTimes);
	}
	
	@Test
	void shouldReturnBadRequestForNullFile() {
		ResponseEntity<Map<String, Object>> response = dataController.loadData(null);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("No file provided", body.get("error"));
		
		verify(csvDataService, never()).parseCSVContent(anyString());
	}
	
	@Test
	void shouldReturnBadRequestForEmptyFile() throws Exception {
		MockMultipartFile emptyFile = new MockMultipartFile(
		"file", 
		"empty.csv", 
		"text/csv", 
		new byte[0]
		);
		
		ResponseEntity<Map<String, Object>> response = dataController.loadData(emptyFile);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("File is empty", body.get("error"));
		
		verify(csvDataService, never()).parseCSVContent(anyString());
	}
	
	@Test
	void shouldReturnBadRequestForInvalidCSVFormat() throws Exception {
		String invalidCsvContent = "R11;R12";  // Missing time field
		MockMultipartFile file = new MockMultipartFile(
		"file", 
		"invalid.csv", 
		"text/csv", 
		invalidCsvContent.getBytes()
		);
		
		when(csvDataService.parseCSVContent(invalidCsvContent))
		.thenThrow(new IllegalArgumentException("Invalid CSV format"));
		
		ResponseEntity<Map<String, Object>> response = dataController.loadData(file);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("Invalid CSV format", body.get("error"));
		
		verify(csvDataService).parseCSVContent(invalidCsvContent);
	}
	
	@Test
	void shouldReturnInternalServerErrorForUnexpectedException() throws Exception {
		String csvContent = "R11;R12;20";
		MockMultipartFile file = new MockMultipartFile(
		"file", 
		"test.csv", 
		"text/csv", 
		csvContent.getBytes()
		);
		
		when(csvDataService.parseCSVContent(csvContent))
		.thenThrow(new RuntimeException("Unexpected error"));
		
		ResponseEntity<Map<String, Object>> response = dataController.loadData(file);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("An error occurred while processing the file", body.get("error"));
		
		verify(csvDataService).parseCSVContent(csvContent);
	}
	
	@Test
	void shouldHandleFileReadError() throws Exception {
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockFile.isEmpty()).thenReturn(false);
		when(mockFile.getBytes()).thenThrow(new RuntimeException("File read error"));
		
		ResponseEntity<Map<String, Object>> response = dataController.loadData(mockFile);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertNotNull(body);
		assertEquals("An error occurred while processing the file", body.get("error"));
	}
	
	@Test
	void shouldHandleUTF8EncodedFile() throws Exception {
		String csvContent = "Rüte1;Rüte2;25"; // German characters
		MockMultipartFile file = new MockMultipartFile(
		"file", 
		"utf8.csv", 
		"text/csv", 
		csvContent.getBytes("UTF-8")
		);
		
		List<TravelTime> mockTravelTimes = Arrays.asList(
		new TravelTime("Rüte1", "Rüte2", 25)
		);
		
		when(csvDataService.parseCSVContent(csvContent)).thenReturn(mockTravelTimes);
		
		ResponseEntity<Map<String, Object>> response = dataController.loadData(file);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, Object> body = response.getBody();
		assertEquals(1, body.get("recordsProcessed"));
		
		verify(csvDataService).parseCSVContent(csvContent);
	}
}