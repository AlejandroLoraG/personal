package com.example.javajava.service;

import com.example.javajava.model.TravelTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageServiceTest {
	
	private DataStorageService dataStorageService;
	
	@BeforeEach
	void setUp() {
		dataStorageService = new DataStorageService();
	}
	
	@Test
	void shouldStoreAndRetrieveTravelTimes() {
		List<TravelTime> travelTimes = Arrays.asList(
		new TravelTime("R11", "R12", 20),
		new TravelTime("R12", "R13", 9)
		);
		
		dataStorageService.storeTravelTimes(travelTimes);
		List<TravelTime> retrieved = dataStorageService.getAllTravelTimes();
		
		assertEquals(2, retrieved.size());
		assertEquals("R11", retrieved.get(0).getFrom());
		assertEquals("R12", retrieved.get(0).getTo());
		assertEquals(20, retrieved.get(0).getTime());
	}
	
	@Test
	void shouldReplaceExistingDataOnNewLoad() {
		List<TravelTime> initialData = Arrays.asList(
		new TravelTime("R11", "R12", 20)
		);
		List<TravelTime> newData = Arrays.asList(
		new TravelTime("CP1", "CP2", 7),
		new TravelTime("CP2", "R20", 67)
		);
		
		dataStorageService.storeTravelTimes(initialData);
		assertEquals(1, dataStorageService.getAllTravelTimes().size());
		
		dataStorageService.storeTravelTimes(newData);
		List<TravelTime> retrieved = dataStorageService.getAllTravelTimes();
		
		assertEquals(2, retrieved.size());
		assertEquals("CP1", retrieved.get(0).getFrom());
		assertEquals("CP2", retrieved.get(1).getFrom());
	}
	
	@Test
	void shouldReturnEmptyListWhenNoDataStored() {
		List<TravelTime> retrieved = dataStorageService.getAllTravelTimes();
		
		assertTrue(retrieved.isEmpty());
	}
	
	@Test
	void shouldHandleEmptyTravelTimesList() {
		List<TravelTime> emptyList = Arrays.asList();
		
		dataStorageService.storeTravelTimes(emptyList);
		List<TravelTime> retrieved = dataStorageService.getAllTravelTimes();
		
		assertTrue(retrieved.isEmpty());
	}
	
	@Test
	void shouldHandleNullTravelTimesList() {
		assertThrows(IllegalArgumentException.class, () -> {
			dataStorageService.storeTravelTimes(null);
		});
	}
	
	@Test
	void shouldClearDataSuccessfully() {
		List<TravelTime> travelTimes = Arrays.asList(
		new TravelTime("R11", "R12", 20)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		assertEquals(1, dataStorageService.getAllTravelTimes().size());
		
		dataStorageService.clearAllData();
		
		assertTrue(dataStorageService.getAllTravelTimes().isEmpty());
	}
	
	@Test
	void shouldReturnTravelTimesFromSpecificLocation() {
		List<TravelTime> travelTimes = Arrays.asList(
		new TravelTime("R11", "R12", 20),
		new TravelTime("R11", "CP1", 92),
		new TravelTime("R12", "R13", 9)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		List<TravelTime> fromR11 = dataStorageService.getTravelTimesFrom("R11");
		
		assertEquals(2, fromR11.size());
		assertTrue(fromR11.stream().allMatch(tt -> "R11".equals(tt.getFrom())));
	}
	
	@Test
	void shouldReturnEmptyListForUnknownLocation() {
		List<TravelTime> travelTimes = Arrays.asList(
		new TravelTime("R11", "R12", 20)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		List<TravelTime> fromUnknown = dataStorageService.getTravelTimesFrom("UNKNOWN");
		
		assertTrue(fromUnknown.isEmpty());
	}
	
	@Test
	void shouldReturnAllUniqueLocations() {
		List<TravelTime> travelTimes = Arrays.asList(
		new TravelTime("R11", "R12", 20),
		new TravelTime("R12", "R13", 9),
		new TravelTime("R11", "CP1", 92),  // R11 appears twice
		new TravelTime("CP1", "R11", 84)   // R11 appears again
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		List<String> locations = dataStorageService.getAllLocations();
		
		assertEquals(4, locations.size());
		assertTrue(locations.contains("R11"));
		assertTrue(locations.contains("R12"));
		assertTrue(locations.contains("R13"));
		assertTrue(locations.contains("CP1"));
	}
	
	@Test
	void shouldReturnDataCount() {
		List<TravelTime> travelTimes = Arrays.asList(
		new TravelTime("R11", "R12", 20),
		new TravelTime("R12", "R13", 9)
		);
		dataStorageService.storeTravelTimes(travelTimes);
		
		int count = dataStorageService.getDataCount();
		
		assertEquals(2, count);
	}
}