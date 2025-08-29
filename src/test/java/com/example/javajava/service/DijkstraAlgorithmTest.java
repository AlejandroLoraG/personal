package com.example.javajava.service;

import com.example.javajava.model.PathResult;
import com.example.javajava.model.TravelTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraAlgorithmTest {
	
	private GraphService graphService;
	
	@BeforeEach
	void setUp() {
		graphService = new GraphService();
	}
	
	@Test
	void shouldFindDirectPath() {
		// Given - simple direct connection
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		PathResult result = graphService.findShortestPath("R11", "R12");
		
		assertTrue(result.isPathFound());
		assertEquals(Arrays.asList("R11", "R12"), result.getPath());
		assertEquals(20, result.getTotalTime());
	}
	
	@Test
	void shouldFindMultiHopPath() {
		// Given - path through intermediate node
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20),
			new TravelTime("R12", "R13", 9),
			new TravelTime("R13", "R20", 11)
		);
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		PathResult result = graphService.findShortestPath("R11", "R20");
		
		assertTrue(result.isPathFound());
		assertEquals(Arrays.asList("R11", "R12", "R13", "R20"), result.getPath());
		assertEquals(40, result.getTotalTime()); // 20 + 9 + 11
	}
	
	@Test
	void shouldChooseOptimalPathWhenMultipleExist() {
		// Given - two paths: R11->CP1->CP2 (99) vs R11->R12->R13->R20->CP2 (100)
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "CP1", 92),
			new TravelTime("CP1", "CP2", 7),    // Total: 99
			new TravelTime("R11", "R12", 20),
			new TravelTime("R12", "R13", 9),
			new TravelTime("R13", "R20", 11),
			new TravelTime("R20", "CP2", 60)    // Total: 100
		);
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		PathResult result = graphService.findShortestPath("R11", "CP2");
		
		assertTrue(result.isPathFound());
		assertEquals(Arrays.asList("R11", "CP1", "CP2"), result.getPath());
		assertEquals(99, result.getTotalTime());
	}
	
	@Test
	void shouldReturnEmptyPathForUnreachableDestination() {
		// Given - disconnected components
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20),
			new TravelTime("CP1", "CP2", 7)  // Separate component
		);
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		PathResult result = graphService.findShortestPath("R11", "CP2");
		
		assertFalse(result.isPathFound());
		assertTrue(result.isEmpty());
		assertTrue(result.getPath().isEmpty());
		assertEquals(0, result.getTotalTime());
	}
	
	@Test
	void shouldHandleSameSourceAndDestination() {
		// Given
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		PathResult result = graphService.findShortestPath("R11", "R11");
		
		assertTrue(result.isPathFound());
		assertEquals(Arrays.asList("R11"), result.getPath());
		assertEquals(0, result.getTotalTime());
	}
	
	@Test
	void shouldReturnEmptyPathForUnknownSource() {
		// Given
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		PathResult result = graphService.findShortestPath("UNKNOWN", "R12");
		
		assertFalse(result.isPathFound());
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldReturnEmptyPathForUnknownDestination() {
		// Given
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		PathResult result = graphService.findShortestPath("R11", "UNKNOWN");
		
		assertFalse(result.isPathFound());
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldHandleComplexNetworkFromExerciseExample() {
		// Given - exact data from exercise example
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20),
			new TravelTime("R12", "R13", 9),
			new TravelTime("R13", "R12", 11),
			new TravelTime("R13", "R20", 9),
			new TravelTime("R20", "R13", 11),
			new TravelTime("CP1", "R11", 84),
			new TravelTime("R11", "CP1", 92),
			new TravelTime("CP1", "CP2", 7),
			new TravelTime("CP2", "CP1", 10),
			new TravelTime("CP2", "R20", 67),
			new TravelTime("R20", "CP2", 60)
		);
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		PathResult result = graphService.findShortestPath("CP1", "R20");
		
		// Then - should find optimal path
		assertTrue(result.isPathFound());
		assertEquals(74, result.getTotalTime()); // Expected from exercise
		
		// Path should be CP1->CP2->R20 or equivalent optimal path
		List<String> path = result.getPath();
		assertEquals("CP1", path.get(0));
		assertEquals("R20", path.get(path.size() - 1));
	}
	
	@Test
	void shouldHandleSelfLoop() {
		// Given - location with self-loop
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R11", 1),
			new TravelTime("R11", "R12", 20)
		);
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		PathResult result = graphService.findShortestPath("R11", "R12");
		
		// Then - should not use self-loop for longer path
		assertTrue(result.isPathFound());
		assertEquals(Arrays.asList("R11", "R12"), result.getPath());
		assertEquals(20, result.getTotalTime());
	}
	
	@Test
	void shouldHandleEmptyGraph() {
		List<TravelTime> emptyTravelTimes = Arrays.asList();
		graphService.buildGraphFromTravelTimes(emptyTravelTimes);
		
		PathResult result = graphService.findShortestPath("R11", "R12");
		
		assertFalse(result.isPathFound());
		assertTrue(result.isEmpty());
	}
}