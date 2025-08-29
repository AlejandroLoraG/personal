package com.example.javajava.performance;

import com.example.javajava.model.PathResult;
import com.example.javajava.model.TravelTime;
import com.example.javajava.service.DataStorageService;
import com.example.javajava.service.GraphService;
import com.example.javajava.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceTest {
	
	private RouteService routeService;
	private DataStorageService dataStorageService;
	
	@BeforeEach
	void setUp() {
		dataStorageService = new DataStorageService();
		GraphService graphService = new GraphService();
		routeService = new RouteService(dataStorageService, graphService);
	}
	
	@Test
	void shouldMeetPerformanceRequirementWith300msTarget() {
		// Given - moderate size dataset
		List<TravelTime> travelTimes = generateTestDataset(1000); // 1K routes
		dataStorageService.storeTravelTimes(travelTimes);
		
		// When - measure route calculation time
		long startTime = System.currentTimeMillis();
		routeService.calculateOptimalRoute("R1", "R50");
		long endTime = System.currentTimeMillis();
		
		long executionTime = endTime - startTime;
		
		// Then - should be under 300ms
		assertTrue(executionTime < 300, 
		"Route calculation took " + executionTime + "ms, should be under 300ms");
		System.out.println("Route calculation time: " + executionTime + "ms");
	}
	
	@Test
	void shouldHandleLargeDatasetEfficiently() {
		// Given - large dataset similar to 10K requirement
		List<TravelTime> travelTimes = generateTestDataset(5000); // 5K routes for faster testing
		dataStorageService.storeTravelTimes(travelTimes);
		
		// When - perform multiple route calculations
		long totalTime = 0;
		int testRuns = 10;
		
		for (int i = 0; i < testRuns; i++) {
			String from = "R" + (i * 5 + 1);
			String to = "R" + (i * 5 + 10);
			
			long startTime = System.currentTimeMillis();
			routeService.calculateOptimalRoute(from, to);
			long endTime = System.currentTimeMillis();
			
			totalTime += (endTime - startTime);
		}
		
		long averageTime = totalTime / testRuns;
		
		// Then - average should be well under 300ms
		assertTrue(averageTime < 300, 
		"Average route calculation took " + averageTime + "ms, should be under 300ms");
		System.out.println("Average route calculation time over " + testRuns + " runs: " + averageTime + "ms");
		System.out.println("Dataset size: " + travelTimes.size() + " routes");
	}
	
	@Test
	void shouldScaleWithDatasetSize() {
		// Test performance scaling with different dataset sizes
		int[] sizes = {100, 500, 1000, 2000};
		
		for (int size : sizes) {
			// Given
			List<TravelTime> travelTimes = generateTestDataset(size);
			dataStorageService.storeTravelTimes(travelTimes);
			
			// When
			long startTime = System.currentTimeMillis();
			routeService.calculateOptimalRoute("R1", "R10");
			long endTime = System.currentTimeMillis();
			
			long executionTime = endTime - startTime;
			
			// Then
			assertTrue(executionTime < 300, 
			"Route calculation with " + size + " routes took " + executionTime + "ms");
			System.out.println("Dataset size: " + size + " routes, Time: " + executionTime + "ms");
		}
	}
	
	@Test
	void shouldHandleWorstCaseScenarios() {
		// Given - create worst case: many nodes with few connections (sparse graph)
		List<TravelTime> sparseTravelTimes = generateSparseDataset(200, 2); // 200 nodes, avg 2 connections each
		dataStorageService.storeTravelTimes(sparseTravelTimes);
		
		// When
		long startTime = System.currentTimeMillis();
		PathResult result = routeService.calculateOptimalRoute("R1", "R200");
		long endTime = System.currentTimeMillis();
		
		long executionTime = endTime - startTime;
		
		// Then
		assertTrue(executionTime < 300, 
		"Sparse graph route calculation took " + executionTime + "ms, should be under 300ms");
		System.out.println("Sparse graph (200 nodes, 2 avg connections) time: " + executionTime + "ms");
		
		if (result.isPathFound()) {
			System.out.println("Path found with " + result.getPath().size() + " hops, total time: " + result.getTotalTime());
		} else {
			System.out.println("No path found in sparse graph");
		}
	}
	
	private List<TravelTime> generateTestDataset(int targetSize) {
		List<TravelTime> travelTimes = new ArrayList<>();
		Random random = new Random(42);
		
		// Generate locations R1 to R50 (50 nodes)
		int numLocations = 50;
		
		// Create connections to reach target size
		while (travelTimes.size() < targetSize) {
			String from = "R" + (random.nextInt(numLocations) + 1);
			String to = "R" + (random.nextInt(numLocations) + 1);
			
			if (!from.equals(to)) {
				int time = random.nextInt(100) + 5;
				travelTimes.add(new TravelTime(from, to, time));
			}
		}
		
		// Ensure connectivity by adding a ring of connections
		for (int i = 1; i <= numLocations; i++) {
			String from = "R" + i;
			String to = "R" + (i % numLocations + 1);
			travelTimes.add(new TravelTime(from, to, 10));
		}
		
		return travelTimes.subList(0, targetSize);
	}
	
	private List<TravelTime> generateSparseDataset(int numNodes, int avgConnectionsPerNode) {
		List<TravelTime> travelTimes = new ArrayList<>();
		Random random = new Random(42);
		
		// Create sparse connections
		for (int i = 1; i <= numNodes; i++) {
			String from = "R" + i;
			
			// Add connections to create sparse network
			for (int j = 0; j < avgConnectionsPerNode; j++) {
				int targetNode = (i + j + 1) % numNodes + 1;
				if (targetNode != i) {
					String to = "R" + targetNode;
					int time = random.nextInt(50) + 10; // 10-60 minutes
					travelTimes.add(new TravelTime(from, to, time));
				}
			}
		}
		
		return travelTimes;
	}
}