package com.example.javajava.service;

import com.example.javajava.model.PathResult;
import com.example.javajava.model.TravelTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RouteServiceTest {
	
	private RouteService routeService;
	private DataStorageService dataStorageService;
	private GraphService graphService;
	
	@BeforeEach
	void setUp() {
		dataStorageService = new DataStorageService();
		graphService = new GraphService();
		routeService = new RouteService(dataStorageService, graphService);
	}
	
	@Test
	void shouldCalculateOptimalRouteWithCurrentData() {
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20),
			new TravelTime("R12", "R13", 9),
			new TravelTime("R13", "R20", 11)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		PathResult result = routeService.calculateOptimalRoute("R11", "R20");
		
		assertTrue(result.isPathFound());
		assertEquals(Arrays.asList("R11", "R12", "R13", "R20"), result.getPath());
		assertEquals(40, result.getTotalTime());
	}
	
	@Test
	void shouldHandleDataReloadScenario() {
		List<TravelTime> initialTravelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 50)
		);
		dataStorageService.storeTravelTimes(initialTravelTimes);
		
		PathResult initialResult = routeService.calculateOptimalRoute("R11", "R12");
		assertEquals(50, initialResult.getTotalTime());
		
		// reload with new data
		List<TravelTime> newTravelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20),
			new TravelTime("R11", "CP1", 15), // Better route via CP1
			new TravelTime("CP1", "R12", 10)
		);
		dataStorageService.storeTravelTimes(newTravelTimes);
		
		PathResult newResult = routeService.calculateOptimalRoute("R11", "R12");
		
		assertTrue(newResult.isPathFound());
		assertEquals(20, newResult.getTotalTime());
	}
	
	@Test
	void shouldValidateSourceLocationExists() {
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		PathResult result = routeService.calculateOptimalRoute("UNKNOWN", "R12");
		
		assertFalse(result.isPathFound());
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldValidateDestinationLocationExists() {
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		PathResult result = routeService.calculateOptimalRoute("R11", "UNKNOWN");
		
		assertFalse(result.isPathFound());
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldHandleEmptyDataset() {
		List<TravelTime> emptyTravelTimes = Arrays.asList();
		dataStorageService.storeTravelTimes(emptyTravelTimes);
		
		PathResult result = routeService.calculateOptimalRoute("R11", "R12");
		
		assertFalse(result.isPathFound());
		assertTrue(result.isEmpty());
	}
	
	@Test
	void shouldRebuildGraphOnEachCalculation() {
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		PathResult result1 = routeService.calculateOptimalRoute("R11", "R12");
		assertEquals(20, result1.getTotalTime());
		
		List<TravelTime> newTravelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 30) // Different time
		);
		dataStorageService.storeTravelTimes(newTravelTimes);
		
		PathResult result2 = routeService.calculateOptimalRoute("R11", "R12");
		
		assertEquals(30, result2.getTotalTime());
	}
	
	@Test
	void shouldHandleComplexRoutingFromExerciseExample() {
		// data from exercise
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
		dataStorageService.storeTravelTimes(travelTimes);
		
		PathResult result = routeService.calculateOptimalRoute("CP1", "R20");
		
		// exercise expected result
		assertTrue(result.isPathFound());
		assertEquals(74, result.getTotalTime());
		
		// Verify path starts and ends correctly
		List<String> path = result.getPath();
		assertEquals("CP1", path.get(0));
		assertEquals("R20", path.get(path.size() - 1));
	}
	
	@Test
	void shouldHandleSameSourceAndDestination() {
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		PathResult result = routeService.calculateOptimalRoute("R11", "R11");
		
		assertTrue(result.isPathFound());
		assertEquals(Arrays.asList("R11"), result.getPath());
		assertEquals(0, result.getTotalTime());
	}
	
	@Test
	void shouldHandleNullParameters() {
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		assertThrows(IllegalArgumentException.class, () -> {
			routeService.calculateOptimalRoute(null, "R12");
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			routeService.calculateOptimalRoute("R11", null);
		});
	}
	
	@Test
	void shouldHandleEmptyParameters() {
		List<TravelTime> travelTimes = Arrays.asList(
			new TravelTime("R11", "R12", 20)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		assertThrows(IllegalArgumentException.class, () -> {
			routeService.calculateOptimalRoute("", "R12");
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			routeService.calculateOptimalRoute("R11", "");
		});
	}
}