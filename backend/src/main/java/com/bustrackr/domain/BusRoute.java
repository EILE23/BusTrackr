package com.bustrackr.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "bus_routes")
public class BusRoute {
    
    @Id
    private String routeId;
    
    @Column(nullable = false)
    private String routeName;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RouteType routeType;
    
    @Column
    private String direction;
    
    @Column
    private String startStop;
    
    @Column
    private String endStop;
    
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BusStop> busStops;
    
    public enum RouteType {
        EXPRESS, TRUNK, BRANCH, CIRCULAR, WIDE
    }
    
    // Constructors
    public BusRoute() {}
    
    public BusRoute(String routeId, String routeName, RouteType routeType, String direction) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeType = routeType;
        this.direction = direction;
    }
    
    // Getters and Setters
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    
    public RouteType getRouteType() { return routeType; }
    public void setRouteType(RouteType routeType) { this.routeType = routeType; }
    
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    
    public String getStartStop() { return startStop; }
    public void setStartStop(String startStop) { this.startStop = startStop; }
    
    public String getEndStop() { return endStop; }
    public void setEndStop(String endStop) { this.endStop = endStop; }
    
    public List<BusStop> getBusStops() { return busStops; }
    public void setBusStops(List<BusStop> busStops) { this.busStops = busStops; }
}