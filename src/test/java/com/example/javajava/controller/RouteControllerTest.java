package com.example.javajava.controller;

import com.example.javajava.model.RouteResponse;
import com.example.javajava.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RouteControllerTest {
	
	private RouteService routeService;
	
	private RouteController routeController;
	
	@BeforeEach
	void setUp() {
		routeService = new RouteService(new com.example.javajava.service.DataStorageService(), new com.example.javajava.service.GraphService());
		routeController = new RouteController(routeService);
	}
	
	@Test
	void shouldReturnRouteFromRouteService() {
		com.example.javajava.service.DataStorageService dataService = new com.example.javajava.service.DataStorageService();
		com.example.javajava.service.GraphService graphService = new com.example.javajava.service.GraphService();
		routeService = new RouteService(dataService, graphService);
		routeController = new RouteController(routeService);
		
		List<com.example.javajava.model.TravelTime> travelTimes = Arrays.asList(
			new com.example.javajava.model.TravelTime("CP1", "CP2", 7),
			new com.example.javajava.model.TravelTime("CP2", "R20", 67)
		);
		dataService.storeTravelTimes(travelTimes);
		
		ResponseEntity<?> response = routeController.calculateRoute("CP1", "R20");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() instanceof RouteResponse);
		RouteResponse routeResponse = (RouteResponse) response.getBody();
		assertEquals(Arrays.asList("CP1", "CP2", "R20"), routeResponse.getPath());
		assertEquals(74, routeResponse.getTotalTime());
	}
	
	@Test
	void shouldReturnNotFoundWhenNoRouteExists() {
		com.example.javajava.service.DataStorageService dataService = new com.example.javajava.service.DataStorageService();
		com.example.javajava.service.GraphService graphService = new com.example.javajava.service.GraphService();
		routeService = new RouteService(dataService, graphService);
		routeController = new RouteController(routeService);
		
		List<com.example.javajava.model.TravelTime> travelTimes = Arrays.asList(
			new com.example.javajava.model.TravelTime("R11", "R12", 20),
			new com.example.javajava.model.TravelTime("CP1", "CP2", 7) // Separate component
		);
		dataService.storeTravelTimes(travelTimes);
		
		ResponseEntity<?> response = routeController.calculateRoute("R11", "CP2");
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody() instanceof Map);
		Map<?, ?> errorResponse = (Map<?, ?>) response.getBody();
		assertTrue(errorResponse.get("error").toString().contains("No route found between R11 and CP2"));
	}
	
	@Test
	void shouldHandleNullParameters() {
		com.example.javajava.service.DataStorageService dataService = new com.example.javajava.service.DataStorageService();
		com.example.javajava.service.GraphService graphService = new com.example.javajava.service.GraphService();
		routeService = new RouteService(dataService, graphService);
		routeController = new RouteController(routeService);
		
		assertThrows(IllegalArgumentException.class, () -> {
			routeController.calculateRoute(null, "R20");
		});
	}
	
	@Test
	void shouldHandleEmptyParameters() {
		com.example.javajava.service.DataStorageService dataService = new com.example.javajava.service.DataStorageService();
		com.example.javajava.service.GraphService graphService = new com.example.javajava.service.GraphService();
		routeService = new RouteService(dataService, graphService);
		routeController = new RouteController(routeService);
		
		assertThrows(IllegalArgumentException.class, () -> {
			routeController.calculateRoute("", "R20");
		});
	}
}