package com.example.javajava.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RouteResponse {
	
	@JsonProperty("ruta")
	private List<String> path;
	
	@JsonProperty("tiempoTotal")
	private int totalTime;
	
	public RouteResponse() {
	}
	
	public RouteResponse(List<String> path, int totalTime) {
		this.path = path;
		this.totalTime = totalTime;
	}
	
	public List<String> getPath() {
		return path;
	}
	
	public void setPath(List<String> path) {
		this.path = path;
	}
	
	public int getTotalTime() {
		return totalTime;
	}
	
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
}