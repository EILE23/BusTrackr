package com.bustrackr.dto;

import com.bustrackr.domain.BusStop;

public class BusStopSearchResponse {
    private String stopId;
    private String stopName;
    private Double latitude;
    private Double longitude;
    private String direction;
    private String district;
    
    // Constructors
    public BusStopSearchResponse() {}
    
    public BusStopSearchResponse(BusStop busStop) {
        this.stopId = busStop.getStopId();
        this.stopName = busStop.getStopName();
        this.latitude = busStop.getLatitude();
        this.longitude = busStop.getLongitude();
        this.direction = busStop.getDirection();
        this.district = busStop.getDistrict();
    }
    
    // Getters and Setters
    public String getStopId() { return stopId; }
    public void setStopId(String stopId) { this.stopId = stopId; }
    
    public String getStopName() { return stopName; }
    public void setStopName(String stopName) { this.stopName = stopName; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
}