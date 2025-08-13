package com.bustrackr.dto;

import com.bustrackr.domain.BusArrival;
import java.time.LocalDateTime;

public class BusArrivalResponse {
    private String routeId;
    private String stopId;
    private String stopName;
    private Integer estimatedTime;
    private Integer remainingStops;
    private String congestion;
    private String plateNumber;
    private LocalDateTime lastUpdated;
    
    // Constructors
    public BusArrivalResponse() {}
    
    public BusArrivalResponse(BusArrival busArrival) {
        this.routeId = busArrival.getRouteId();
        this.stopId = busArrival.getBusStop().getStopId();
        this.stopName = busArrival.getBusStop().getStopName();
        this.estimatedTime = busArrival.getEstimatedTime();
        this.remainingStops = busArrival.getRemainingStops();
        this.congestion = busArrival.getCongestion() != null ? busArrival.getCongestion().name().toLowerCase() : null;
        this.plateNumber = busArrival.getPlateNumber();
        this.lastUpdated = busArrival.getLastUpdated();
    }
    
    // Getters and Setters
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getStopId() { return stopId; }
    public void setStopId(String stopId) { this.stopId = stopId; }
    
    public String getStopName() { return stopName; }
    public void setStopName(String stopName) { this.stopName = stopName; }
    
    public Integer getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(Integer estimatedTime) { this.estimatedTime = estimatedTime; }
    
    public Integer getRemainingStops() { return remainingStops; }
    public void setRemainingStops(Integer remainingStops) { this.remainingStops = remainingStops; }
    
    public String getCongestion() { return congestion; }
    public void setCongestion(String congestion) { this.congestion = congestion; }
    
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}