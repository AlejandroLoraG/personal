package com.example.javajava.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TravelTimeTest {
	
	@Test
	void shouldCreateTravelTimeWithValidData() {
		// Given
		String from = "R11";
		String to = "R12";
		int time = 20;
		
		// When
		TravelTime travelTime = new TravelTime(from, to, time);
		
		// Then
		assertEquals(from, travelTime.getFrom());
		assertEquals(to, travelTime.getTo());
		assertEquals(time, travelTime.getTime());
	}
	
	@Test
	void shouldThrowExceptionForNullFromLocation() {
		// Given
		String from = null;
		String to = "R12";
		int time = 20;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new TravelTime(from, to, time);
		});
	}
	
	@Test
	void shouldThrowExceptionForEmptyFromLocation() {
		// Given
		String from = "";
		String to = "R12";
		int time = 20;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new TravelTime(from, to, time);
		});
	}
	
	@Test
	void shouldThrowExceptionForNullToLocation() {
		// Given
		String from = "R11";
		String to = null;
		int time = 20;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new TravelTime(from, to, time);
		});
	}
	
	@Test
	void shouldThrowExceptionForEmptyToLocation() {
		// Given
		String from = "R11";
		String to = "";
		int time = 20;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new TravelTime(from, to, time);
		});
	}
	
	@Test
	void shouldThrowExceptionForNegativeTime() {
		// Given
		String from = "R11";
		String to = "R12";
		int time = -5;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new TravelTime(from, to, time);
		});
	}
	
	@Test
	void shouldThrowExceptionForZeroTime() {
		// Given
		String from = "R11";
		String to = "R12";
		int time = 0;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new TravelTime(from, to, time);
		});
	}
	
	@Test
	void shouldAllowSameFromAndToLocation() {
		// Given
		String from = "R11";
		String to = "R11";
		int time = 1;
		
		// When
		TravelTime travelTime = new TravelTime(from, to, time);
		
		// Then
		assertEquals(from, travelTime.getFrom());
		assertEquals(to, travelTime.getTo());
		assertEquals(time, travelTime.getTime());
	}
	
	@Test
	void shouldHandleLocationNamesWithSpecialCharacters() {
		// Given
		String from = "CP-1";
		String to = "R_20";
		int time = 15;
		
		// When
		TravelTime travelTime = new TravelTime(from, to, time);
		
		// Then
		assertEquals(from, travelTime.getFrom());
		assertEquals(to, travelTime.getTo());
		assertEquals(time, travelTime.getTime());
	}
}