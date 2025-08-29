package com.example.javajava.service;

import com.example.javajava.model.Edge;
import com.example.javajava.model.TravelTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphServiceTest {

	private GraphService graphService;
	
	@BeforeEach
	void setUp() {
		graphService = new GraphService();
	}
	
	@Test
	void shouldBuildGraphFromTravelTimes() {
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20),
			new TravelTime("R12", "R13", 9),
			new TravelTime("R11", "CP1", 92)
		);
		
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		assertTrue(graphService.hasLocation("R11"));
		assertTrue(graphService.hasLocation("R12"));
		assertTrue(graphService.hasLocation("R13"));
		assertTrue(graphService.hasLocation("CP1"));
		
		List<Edge> r11Neighbors = graphService.getNeighbors("R11");
		assertEquals(2, r11Neighbors.size());
		
		// Check R11 -> R12 edge
		Edge edgeToR12 = r11Neighbors.stream()
			.filter(e -> "R12".equals(e.getDestination()))
			.findFirst()
			.orElse(null);
		assertNotNull(edgeToR12);
		assertEquals(20, edgeToR12.getWeight());
		
		// Check R11 -> CP1 edge
		Edge edgeToCP1 = r11Neighbors.stream()
			.filter(e -> "CP1".equals(e.getDestination()))
			.findFirst()
			.orElse(null);
		assertNotNull(edgeToCP1);
		assertEquals(92, edgeToCP1.getWeight());
	}
	
	@Test
	void shouldAddEdgeCorrectly() {
		graphService.addEdge("R11", "R12", 20);
		
		assertTrue(graphService.hasLocation("R11"));
		assertTrue(graphService.hasLocation("R12"));
		
		List<Edge> neighbors = graphService.getNeighbors("R11");
		assertEquals(1, neighbors.size());
		assertEquals("R12", neighbors.get(0).getDestination());
		assertEquals(20, neighbors.get(0).getWeight());
	}
	
	@Test
	void shouldHandleMultipleEdgesFromSameSource() {
		graphService.addEdge("R11", "R12", 20);
		graphService.addEdge("R11", "R13", 15);
		graphService.addEdge("R11", "CP1", 92);
		
		List<Edge> neighbors = graphService.getNeighbors("R11");
		assertEquals(3, neighbors.size());
		
		// Verify all destinations are present
		List<String> destinations = neighbors.stream()
			.map(Edge::getDestination)
			.toList();
		assertTrue(destinations.contains("R12"));
		assertTrue(destinations.contains("R13"));
		assertTrue(destinations.contains("CP1"));
	}
	
	@Test
	void shouldReturnEmptyListForLocationWithNoNeighbors() {
		graphService.addEdge("R11", "R12", 20);
		
		List<Edge> neighbors = graphService.getNeighbors("R13");
		
		assertNotNull(neighbors);
		assertTrue(neighbors.isEmpty());
	}
	
	@Test
	void shouldReturnEmptyListForUnknownLocation() {
		List<Edge> neighbors = graphService.getNeighbors("UNKNOWN");
		
		assertNotNull(neighbors);
		assertTrue(neighbors.isEmpty());
	}
	
	@Test
	void shouldReturnFalseForUnknownLocation() {
		graphService.addEdge("R11", "R12", 20);
		
		assertFalse(graphService.hasLocation("UNKNOWN"));
	}
	
	@Test
	void shouldHandleEmptyTravelTimesList() {
		List<TravelTime> emptyList = Arrays.asList();
		
		graphService.buildGraphFromTravelTimes(emptyList);
		
		assertFalse(graphService.hasLocation("R11"));
		assertTrue(graphService.getNeighbors("R11").isEmpty());
	}
	
	@Test
	void shouldThrowExceptionForNullTravelTimesList() {
		assertThrows(IllegalArgumentException.class, () -> {
			graphService.buildGraphFromTravelTimes(null);
		});
	}
	
	@Test
	void shouldClearExistingGraphWhenBuildingNew() {
		List<TravelTime> initialTravelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		graphService.buildGraphFromTravelTimes(initialTravelTimes);
		assertTrue(graphService.hasLocation("R11"));
		
		List<TravelTime> newTravelTimes = Arrays.asList(
			new TravelTime("CP1", "CP2", 7)
		);
		graphService.buildGraphFromTravelTimes(newTravelTimes);
		
		assertFalse(graphService.hasLocation("R11"));
		assertFalse(graphService.hasLocation("R12"));
		assertTrue(graphService.hasLocation("CP1"));
		assertTrue(graphService.hasLocation("CP2"));
	}
	
	@Test
	void shouldHandleSelfLoops() {
		graphService.addEdge("R11", "R11", 1);
		
		assertTrue(graphService.hasLocation("R11"));
		List<Edge> neighbors = graphService.getNeighbors("R11");
		assertEquals(1, neighbors.size());
		assertEquals("R11", neighbors.get(0).getDestination());
		assertEquals(1, neighbors.get(0).getWeight());
	}
	
	@Test
	void shouldHandleDirectedGraphCorrectly() {
		// Given - add edge in one direction only
		graphService.addEdge("R11", "R12", 20);
		
		// Then - edge exists only in one direction
		assertEquals(1, graphService.getNeighbors("R11").size());
		assertEquals(0, graphService.getNeighbors("R12").size());
		assertTrue(graphService.hasLocation("R11"));
		assertTrue(graphService.hasLocation("R12"));
	}
}