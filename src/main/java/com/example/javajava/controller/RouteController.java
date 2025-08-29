package com.example.javajava.controller;

import com.example.javajava.model.PathResult;
import com.example.javajava.model.RouteResponse;
import com.example.javajava.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RouteController {
	
	private final RouteService routeService;
	
	public RouteController(RouteService routeService) {
		this.routeService = routeService;
	}
	
	@GetMapping("/route")
	public ResponseEntity<?> calculateRoute(@RequestParam String from, @RequestParam String to) {
		PathResult pathResult = routeService.calculateOptimalRoute(from, to);
		
		if (!pathResult.isPathFound()) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "No route found between " + from + " and " + to);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		
		return ResponseEntity.ok(new RouteResponse(pathResult.getPath(), pathResult.getTotalTime()));
	}
}