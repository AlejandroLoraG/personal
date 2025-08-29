package com.example.javajava.service;

import com.example.javajava.model.TravelTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvDataServiceTest {
	
	private CsvDataService csvDataService;
	
	@BeforeEach
	void setUp() {
		csvDataService = new CsvDataService();
	}
	
	@Test
	void shouldParseSingleLineCSV() {
		String csvContent = "R11;R12;20";
		
		List<TravelTime> result = csvDataService.parseCSVContent(csvContent);
		
		assertEquals(1, result.size());
		TravelTime travelTime = result.get(0);
		assertEquals("R11", travelTime.getFrom());
		assertEquals("R12", travelTime.getTo());
		assertEquals(20, travelTime.getTime());
	}
	
	@Test
	void shouldParseMultipleLinesCSV() {
		String csvContent = "R11;R12;20\nR12;R13;9\nR13;R20;11";
		
		List<TravelTime> result = csvDataService.parseCSVContent(csvContent);
		
		assertEquals(3, result.size());
		
		TravelTime first = result.get(0);
		assertEquals("R11", first.getFrom());
		assertEquals("R12", first.getTo());
		assertEquals(20, first.getTime());
		
		TravelTime second = result.get(1);
		assertEquals("R12", second.getFrom());
		assertEquals("R13", second.getTo());
		assertEquals(9, second.getTime());
		
		TravelTime third = result.get(2);
		assertEquals("R13", third.getFrom());
		assertEquals("R20", third.getTo());
		assertEquals(11, third.getTime());
	}
	
	@Test
	void shouldHandleCSVWithHeaders() {
		String csvContent = "loc_start;loc_end;time\nR11;R12;20\nCP1;CP2;7";
		
		List<TravelTime> result = csvDataService.parseCSVContent(csvContent);
		
		assertEquals(2, result.size());
		assertEquals("R11", result.get(0).getFrom());
		assertEquals("CP1", result.get(1).getFrom());
	}
	
	@Test
	void shouldHandleEmptyLines() {
		String csvContent = "R11;R12;20\n\nR12;R13;9\n\n";
		
		List<TravelTime> result = csvDataService.parseCSVContent(csvContent);
		
		assertEquals(2, result.size());
	}
	
	@Test
	void shouldHandleWindowsLineEndings() {
		String csvContent = "R11;R12;20\r\nR12;R13;9\r\n";
		
		List<TravelTime> result = csvDataService.parseCSVContent(csvContent);
		
		assertEquals(2, result.size());
	}
	
	@Test
	void shouldThrowExceptionForInvalidNumberOfFields() {
		String csvContent = "R11;R12";  // Missing time field
		
		assertThrows(IllegalArgumentException.class, () -> {
			csvDataService.parseCSVContent(csvContent);
		});
	}
	
	@Test
	void shouldThrowExceptionForNonNumericTime() {
		String csvContent = "R11;R12;abc";
		
		assertThrows(IllegalArgumentException.class, () -> {
			csvDataService.parseCSVContent(csvContent);
		});
	}
	
	@Test
	void shouldThrowExceptionForEmptyFields() {
		String csvContent = ";R12;20";  // Empty from field
		
		assertThrows(IllegalArgumentException.class, () -> {
			csvDataService.parseCSVContent(csvContent);
		});
	}
	
	@Test
	void shouldThrowExceptionForNegativeTime() {
		String csvContent = "R11;R12;-5";
		
		assertThrows(IllegalArgumentException.class, () -> {
			csvDataService.parseCSVContent(csvContent);
		});
	}
	
	@Test
	void shouldReturnEmptyListForEmptyContent() {
		String csvContent = "";
		
		List<TravelTime> result = csvDataService.parseCSVContent(csvContent);
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldReturnEmptyListForNullContent() {
		String csvContent = null;
		
		List<TravelTime> result = csvDataService.parseCSVContent(csvContent);
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldHandleLocationNamesWithSpaces() {
		String csvContent = "Location A;Location B;15";
		
		List<TravelTime> result = csvDataService.parseCSVContent(csvContent);
		
		assertEquals(1, result.size());
		assertEquals("Location A", result.get(0).getFrom());
		assertEquals("Location B", result.get(0).getTo());
	}
}