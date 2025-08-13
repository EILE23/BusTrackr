package com.bustrackr.controller;

import com.bustrackr.dto.BusLocationResponse;
import com.bustrackr.service.BusLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus-locations")
@CrossOrigin(origins = "http://localhost:3002")
public class BusLocationController {
    
    @Autowired
    private BusLocationService busLocationService;
    
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<BusLocationResponse>> getBusLocationsByRoute(
            @PathVariable String routeId) {
        
        List<BusLocationResponse> locations = busLocationService.getBusLocationsByRoute(routeId);
        return ResponseEntity.ok(locations);
    }
    
    @GetMapping("/route/{routeId}/recent")
    public ResponseEntity<List<BusLocationResponse>> getRecentBusLocationsByRoute(
            @PathVariable String routeId,
            @RequestParam(defaultValue = "10") int minutesAgo) {
        
        List<BusLocationResponse> locations = busLocationService.getRecentBusLocationsByRoute(
            routeId, minutesAgo);
        return ResponseEntity.ok(locations);
    }
    
    @GetMapping("/bus/{busId}")
    public ResponseEntity<BusLocationResponse> getLatestBusLocation(
            @PathVariable String busId) {
        
        BusLocationResponse location = busLocationService.getLatestBusLocation(busId);
        if (location != null) {
            return ResponseEntity.ok(location);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}