package com.example.javajava.model;

import java.util.ArrayList;
import java.util.List;

public class PathResult {
	
	private List<String> path;
	private int totalTime;
	private boolean pathFound;
	
	public PathResult() {
		this.path = new ArrayList<>();
		this.totalTime = 0;
		this.pathFound = false;
	}
	
	public PathResult(List<String> path, int totalTime) {
		this.path = path != null ? new ArrayList<>(path) : new ArrayList<>();
		this.totalTime = totalTime;
		this.pathFound = path != null && !path.isEmpty();
	}
	
	public List<String> getPath() {
		return new ArrayList<>(path);
	}
	
	public int getTotalTime() {
		return totalTime;
	}
	
	public boolean isPathFound() {
		return pathFound;
	}
	
	public boolean isEmpty() {
		return !pathFound || path.isEmpty();
	}
}