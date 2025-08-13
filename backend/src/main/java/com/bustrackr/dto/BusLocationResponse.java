package com.bustrackr.dto;

import com.bustrackr.domain.BusLocation;
import java.time.LocalDateTime;

public class BusLocationResponse {
    private String busId;
    private String routeId;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private String congestion;
    private String nextStopId;
    private Integer estimatedArrival;
    private LocalDateTime lastUpdated;
    
    // Constructors
    public BusLocationResponse() {}
    
    public BusLocationResponse(BusLocation busLocation) {
        this.busId = busLocation.getBusId();
        this.routeId = busLocation.getRouteId();
        this.latitude = busLocation.getLatitude();
        this.longitude = busLocation.getLongitude();
        this.speed = busLocation.getSpeed();
        this.congestion = busLocation.getCongestion() != null ? busLocation.getCongestion().name().toLowerCase() : null;
        this.nextStopId = busLocation.getNextStopId();
        this.estimatedArrival = busLocation.getEstimatedArrival();
        this.lastUpdated = busLocation.getLastUpdated();
    }
    
    // Getters and Setters
    public String getBusId() { return busId; }
    public void setBusId(String busId) { this.busId = busId; }
    
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public Double getSpeed() { return speed; }
    public void setSpeed(Double speed) { this.speed = speed; }
    
    public String getCongestion() { return congestion; }
    public void setCongestion(String congestion) { this.congestion = congestion; }
    
    public String getNextStopId() { return nextStopId; }
    public void setNextStopId(String nextStopId) { this.nextStopId = nextStopId; }
    
    public Integer getEstimatedArrival() { return estimatedArrival; }
    public void setEstimatedArrival(Integer estimatedArrival) { this.estimatedArrival = estimatedArrival; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}