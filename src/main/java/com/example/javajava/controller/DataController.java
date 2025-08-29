package com.example.javajava.controller;

import com.example.javajava.model.TravelTime;
import com.example.javajava.service.CsvDataService;
import com.example.javajava.service.DataStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DataController {
	
	private final CsvDataService csvDataService;
	private final DataStorageService dataStorageService;
	
	public DataController(CsvDataService csvDataService, DataStorageService dataStorageService) {
		this.csvDataService = csvDataService;
		this.dataStorageService = dataStorageService;
	}
	
	@PostMapping("/load-data")
	public ResponseEntity<Map<String, Object>> loadData(@RequestParam("file") MultipartFile file) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (file == null) {
				response.put("error", "No file provided");
				return ResponseEntity.badRequest().body(response);
			}
			if (file.isEmpty()) {
				response.put("error", "File is empty");
				return ResponseEntity.badRequest().body(response);
			}
			
			byte[] fileBytes = file.getBytes();
			String csvContent = new String(fileBytes, StandardCharsets.UTF_8);
			
			List<TravelTime> travelTimes = csvDataService.parseCSVContent(csvContent);
			dataStorageService.storeTravelTimes(travelTimes);
			
			response.put("message", "Data loaded successfully");
			response.put("recordsProcessed", travelTimes.size());
			
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			response.put("error", e.getMessage());
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.put("error", "An error occurred while processing the file");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}