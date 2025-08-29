package com.example.javajava.service;

import com.example.javajava.model.TravelTime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataStorageService {
	
	private List<TravelTime> travelTimes = new ArrayList<>();
	
	public void storeTravelTimes(List<TravelTime> travelTimes) {
		if (travelTimes == null) {
			throw new IllegalArgumentException("Travel times list cannot be null");
		}
		
		this.travelTimes = new ArrayList<>(travelTimes);
	}
	
	public List<TravelTime> getAllTravelTimes() {
		return new ArrayList<>(travelTimes);
	}
	
	// Testing functions

	public void clearAllData() {
		travelTimes.clear();
	}

	public List<TravelTime> getTravelTimesFrom(String fromLocation) {
		return travelTimes.stream()
			.filter(tt -> fromLocation.equals(tt.getFrom()))
			.collect(Collectors.toList());
	}
	
	public List<String> getAllLocations() {
		Set<String> locations = new HashSet<>();
		
		for (TravelTime travelTime : travelTimes) {
			locations.add(travelTime.getFrom());
			locations.add(travelTime.getTo());
		}
		
		return new ArrayList<>(locations);
	}
	
	public int getDataCount() {
		return travelTimes.size();
	}
}