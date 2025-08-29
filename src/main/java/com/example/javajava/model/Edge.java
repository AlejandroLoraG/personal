package com.example.javajava.model;

public class Edge {
	
	private String destination;
	private int weight;
	
	public Edge(String destination, int weight) {
		validateDestination(destination);
		validateWeight(weight);
		
		this.destination = destination;
		this.weight = weight;
	}
	
	private void validateDestination(String destination) {
		if (destination == null || destination.isEmpty()) {
			throw new IllegalArgumentException("Destination cannot be null or empty");
		}
	}
	
	private void validateWeight(int weight) {
		if (weight <= 0) {
			throw new IllegalArgumentException("Weight must be positive");
		}
	}
	
	public String getDestination() {
		return destination;
	}
	
	public int getWeight() {
		return weight;
	}
}