package com.example.javajava.service;

import com.example.javajava.model.TravelTime;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvDataService {
	
	private static final String FIELD_SEPARATOR = ";";
	private static final int EXPECTED_FIELD_COUNT = 3;
	private static final String HEADER_PREFIX = "loc_start";
	
	public List<TravelTime> parseCSVContent(String csvContent) {
		List<TravelTime> travelTimes = new ArrayList<>();
		
		if (csvContent == null || csvContent.trim().isEmpty()) {
			return travelTimes;
		}
		
		String[] lines = csvContent.split("\\r?\\n");
		
		for (String line : lines) {
			line = line.trim();
			
			if (line.isEmpty()) continue;
			if (line.startsWith(HEADER_PREFIX)) continue;
			
			String[] fields = line.split(FIELD_SEPARATOR);
			
			if (fields.length != EXPECTED_FIELD_COUNT) {
				throw new IllegalArgumentException("Invalid CSV format. Expected 3 fields separated by ';', got " + fields.length + " fields in line: " + line);
			}
			
			String from = fields[0].trim();
			String to = fields[1].trim();
			String timeStr = fields[2].trim();
			
			if (from.isEmpty() || to.isEmpty() || timeStr.isEmpty()) {
				throw new IllegalArgumentException("Empty fields are not allowed in line: " + line);
			}
			
			int time;
			try {
				time = Integer.parseInt(timeStr);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid time value. Must be a number: " + timeStr);
			}
			
			// TravelTime constructor will validate the data
			travelTimes.add(new TravelTime(from, to, time));
		}
		
		return travelTimes;
	}
}