package com.example.javajava.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RouteResponseTest {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void shouldSerializeToCorrectJsonFormat() throws Exception {
		// Given
		List<String> path = Arrays.asList("CP1", "CP2", "R20");
		int totalTime = 74;
		RouteResponse response = new RouteResponse(path, totalTime);
		
		// When
		String json = objectMapper.writeValueAsString(response);
		
		// Then
		assertTrue(json.contains("\"ruta\":[\"CP1\",\"CP2\",\"R20\"]"));
		assertTrue(json.contains("\"tiempoTotal\":74"));
	}
	
	@Test
	void shouldCreateRouteResponseWithValidData() {
		// Given
		List<String> path = Arrays.asList("CP1", "CP2", "R20");
		int totalTime = 74;
		
		// When
		RouteResponse response = new RouteResponse(path, totalTime);
		
		// Then
		assertEquals(path, response.getPath());
		assertEquals(totalTime, response.getTotalTime());
	}
	
	@Test
	void shouldHandleEmptyPath() {
		// Given
		List<String> emptyPath = Arrays.asList();
		int totalTime = 0;
		
		// When
		RouteResponse response = new RouteResponse(emptyPath, totalTime);
		
		// Then
		assertEquals(emptyPath, response.getPath());
		assertEquals(totalTime, response.getTotalTime());
	}
}