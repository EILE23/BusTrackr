package com.bustrackr.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus_locations")
public class BusLocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String busId;
    
    @Column(nullable = false)
    private String routeId;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column
    private Double speed;
    
    @Column
    @Enumerated(EnumType.STRING)
    private CongestionLevel congestion;
    
    @Column
    private String nextStopId;
    
    @Column
    private Integer estimatedArrival; // 분 단위
    
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
    
    public enum CongestionLevel {
        LOW, MEDIUM, HIGH
    }
    
    // Constructors
    public BusLocation() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    public BusLocation(String busId, String routeId, Double latitude, Double longitude) {
        this.busId = busId;
        this.routeId = routeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
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
    
    public CongestionLevel getCongestion() { return congestion; }
    public void setCongestion(CongestionLevel congestion) { this.congestion = congestion; }
    
    public String getNextStopId() { return nextStopId; }
    public void setNextStopId(String nextStopId) { this.nextStopId = nextStopId; }
    
    public Integer getEstimatedArrival() { return estimatedArrival; }
    public void setEstimatedArrival(Integer estimatedArrival) { this.estimatedArrival = estimatedArrival; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}