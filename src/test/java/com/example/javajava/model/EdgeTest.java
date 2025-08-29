package com.example.javajava.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
	
	@Test
	void shouldCreateEdgeWithValidData() {
		// Given
		String destination = "R12";
		int weight = 20;
		
		// When
		Edge edge = new Edge(destination, weight);
		
		// Then
		assertEquals(destination, edge.getDestination());
		assertEquals(weight, edge.getWeight());
	}
	
	@Test
	void shouldThrowExceptionForNullDestination() {
		// Given
		String destination = null;
		int weight = 20;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new Edge(destination, weight);
		});
	}
	
	@Test
	void shouldThrowExceptionForEmptyDestination() {
		// Given
		String destination = "";
		int weight = 20;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new Edge(destination, weight);
		});
	}
	
	@Test
	void shouldThrowExceptionForNegativeWeight() {
		// Given
		String destination = "R12";
		int weight = -5;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new Edge(destination, weight);
		});
	}
	
	@Test
	void shouldThrowExceptionForZeroWeight() {
		// Given
		String destination = "R12";
		int weight = 0;
		
		// When & Then
		assertThrows(IllegalArgumentException.class, () -> {
			new Edge(destination, weight);
		});
	}
	
	@Test
	void shouldHandleDestinationWithSpecialCharacters() {
		// Given
		String destination = "CP-1";
		int weight = 15;
		
		// When
		Edge edge = new Edge(destination, weight);
		
		// Then
		assertEquals(destination, edge.getDestination());
		assertEquals(weight, edge.getWeight());
	}
	
	@Test
	void shouldSupportLargeWeights() {
		// Given
		String destination = "R12";
		int weight = 999;
		
		// When
		Edge edge = new Edge(destination, weight);
		
		// Then
		assertEquals(destination, edge.getDestination());
		assertEquals(weight, edge.getWeight());
	}
	
	@Test
	void shouldCreateMultipleEdgesToSameDestination() {
		// Given
		String destination = "R12";
		
		// When
		Edge edge1 = new Edge(destination, 20);
		Edge edge2 = new Edge(destination, 30);
		
		// Then
		assertEquals(destination, edge1.getDestination());
		assertEquals(destination, edge2.getDestination());
		assertEquals(20, edge1.getWeight());
		assertEquals(30, edge2.getWeight());
	}
}