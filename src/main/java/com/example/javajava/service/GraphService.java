package com.example.javajava.service;

import com.example.javajava.model.Edge;
import com.example.javajava.model.PathResult;
import com.example.javajava.model.TravelTime;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphService {
	
	private Map<String, List<Edge>> adjacencyList = new HashMap<>();
	
	public void buildGraphFromTravelTimes(List<TravelTime> travelTimes) {
		if (travelTimes == null) {
			throw new IllegalArgumentException("Travel times list cannot be null");
		}
		
		adjacencyList.clear();
		for (TravelTime travelTime : travelTimes) {
			addEdge(travelTime.getFrom(), travelTime.getTo(), travelTime.getTime());
		}
	}
	
	public void addEdge(String from, String to, int weight) {
		adjacencyList.computeIfAbsent(from, k -> new ArrayList<>());
		adjacencyList.computeIfAbsent(to, k -> new ArrayList<>());
		
		Edge edge = new Edge(to, weight);
		adjacencyList.get(from).add(edge);
	}
	
	public List<Edge> getNeighbors(String location) {
		return adjacencyList.getOrDefault(location, new ArrayList<>());
	}
	
	public boolean hasLocation(String location) {
		return adjacencyList.containsKey(location);
	}
	
	public PathResult findShortestPath(String source, String destination) {
		if (!hasLocation(source) || !hasLocation(destination)) {
			return new PathResult();
		}
		
		if (source.equals(destination)) {
			return new PathResult(Arrays.asList(source), 0);
		}
		
		// Dijkstra's
		Map<String, Integer> distances = new HashMap<>();
		Map<String, String> previous = new HashMap<>();
		PriorityQueue<Node> unvisited = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));
		Set<String> visited = new HashSet<>();
		
		// Initialize distances
		for (String location : adjacencyList.keySet()) {
			distances.put(location, Integer.MAX_VALUE);
		}
		distances.put(source, 0);
		unvisited.add(new Node(source, 0));
		
		while (!unvisited.isEmpty()) {
			Node current = unvisited.poll();
			String currentLocation = current.location;
			
			if (visited.contains(currentLocation)) continue;
			
			visited.add(currentLocation);
			
			// Found destination
			if (currentLocation.equals(destination)) break;
			
			// Process neighbors
			List<Edge> neighbors = getNeighbors(currentLocation);
			for (Edge edge : neighbors) {
				String neighbor = edge.getDestination();
				int newDistance = distances.get(currentLocation) + edge.getWeight();
				
				if (newDistance < distances.get(neighbor)) {
					distances.put(neighbor, newDistance);
					previous.put(neighbor, currentLocation);
					unvisited.add(new Node(neighbor, newDistance));
				}
			}
		}
		
		// Reconstruct path
		if (!previous.containsKey(destination) && !source.equals(destination)) {
			return new PathResult(); // No path found
		}
		
		List<String> path = new ArrayList<>();
		String current = destination;
		
		while (current != null) {
			path.add(0, current);
			current = previous.get(current);
		}
		
		return new PathResult(path, distances.get(destination));
	}
	
	private static class Node {
		String location;
		int distance;
		
		Node(String location, int distance) {
			this.location = location;
			this.distance = distance;
		}
	}
}