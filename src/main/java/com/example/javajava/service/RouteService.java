package com.example.javajava.service;

import com.example.javajava.model.PathResult;
import com.example.javajava.model.TravelTime;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
	
	private final DataStorageService dataStorageService;
	private final GraphService graphService;
	
	public RouteService(DataStorageService dataStorageService, GraphService graphService) {
		this.dataStorageService = dataStorageService;
		this.graphService = graphService;
	}
	
	public PathResult calculateOptimalRoute(String from, String to) {
		// Validate input parameters
		if (from == null || from.isEmpty()) {
			throw new IllegalArgumentException("From parameter cannot be null or empty");
		}
		if (to == null || to.isEmpty()) {
			throw new IllegalArgumentException("To parameter cannot be null or empty");
		}
		
		// Get current travel times and rebuild graph
		List<TravelTime> travelTimes = dataStorageService.getAllTravelTimes();
		graphService.buildGraphFromTravelTimes(travelTimes);
		
		// Calculate shortest path
		return graphService.findShortestPath(from, to);
	}
}