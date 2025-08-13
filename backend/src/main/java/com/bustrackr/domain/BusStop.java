package com.bustrackr.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "bus_stops")
public class BusStop {
    
    @Id
    private String stopId;
    
    @Column(nullable = false)
    private String stopName;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column
    private String direction;
    
    @Column
    private String district; // 구역 (강남구, 서초구 등)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private BusRoute route;
    
    @OneToMany(mappedBy = "busStop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BusArrival> arrivals;
    
    // Constructors
    public BusStop() {}
    
    public BusStop(String stopId, String stopName, Double latitude, Double longitude) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
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
    
    public BusRoute getRoute() { return route; }
    public void setRoute(BusRoute route) { this.route = route; }
    
    public List<BusArrival> getArrivals() { return arrivals; }
    public void setArrivals(List<BusArrival> arrivals) { this.arrivals = arrivals; }
}