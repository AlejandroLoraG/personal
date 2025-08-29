package com.example.javajava.model;

public class TravelTime {
	
	private String from;
	private String to;
	private int time;
	
	public TravelTime(String from, String to, int time) {
		validateLocation(from, "From location");
		validateLocation(to, "To location");
		validateTime(time);
		
		this.from = from;
		this.to = to;
		this.time = time;
	}
	
	private void validateLocation(String location, String fieldName) {
		if (location == null || location.isEmpty()) {
			throw new IllegalArgumentException(fieldName + " cannot be null or empty");
		}
	}
	
	private void validateTime(int time) {
		if (time <= 0) {
			throw new IllegalArgumentException("Time must be positive");
		}
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
	public int getTime() {
		return time;
	}
}