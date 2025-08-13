package com.bustrackr.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus_arrivals")
public class BusArrival {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String routeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stop_id")
    private BusStop busStop;
    
    @Column(nullable = false)
    private Integer estimatedTime; // 분 단위
    
    @Column(nullable = false)
    private Integer remainingStops;
    
    @Column
    @Enumerated(EnumType.STRING)
    private CongestionLevel congestion;
    
    @Column
    private String plateNumber; // 차량 번호
    
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
    
    public enum CongestionLevel {
        LOW, MEDIUM, HIGH
    }
    
    // Constructors
    public BusArrival() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    public BusArrival(String routeId, BusStop busStop, Integer estimatedTime, Integer remainingStops) {
        this.routeId = routeId;
        this.busStop = busStop;
        this.estimatedTime = estimatedTime;
        this.remainingStops = remainingStops;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public BusStop getBusStop() { return busStop; }
    public void setBusStop(BusStop busStop) { this.busStop = busStop; }
    
    public Integer getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(Integer estimatedTime) { this.estimatedTime = estimatedTime; }
    
    public Integer getRemainingStops() { return remainingStops; }
    public void setRemainingStops(Integer remainingStops) { this.remainingStops = remainingStops; }
    
    public CongestionLevel getCongestion() { return congestion; }
    public void setCongestion(CongestionLevel congestion) { this.congestion = congestion; }
    
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}