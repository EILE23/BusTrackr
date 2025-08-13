package com.bustrackr.controller;

import com.bustrackr.dto.BusStopSearchResponse;
import com.bustrackr.service.BusStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus-stops")
@CrossOrigin(origins = "http://localhost:3002")
public class BusStopController {
    
    @Autowired
    private BusStopService busStopService;
    
    @GetMapping("/search")
    public ResponseEntity<List<BusStopSearchResponse>> searchBusStops(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String district) {
        
        List<BusStopSearchResponse> results;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            results = busStopService.searchBusStops(keyword.trim());
        } else if (name != null && !name.trim().isEmpty()) {
            results = busStopService.searchBusStopsByName(name.trim());
        } else if (district != null && !district.trim().isEmpty()) {
            results = busStopService.getBusStopsByDistrict(district.trim());
        } else {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/nearby")
    public ResponseEntity<List<BusStopSearchResponse>> getNearbyBusStops(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "1.0") Double radiusKm) {
        
        if (latitude == null || longitude == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<BusStopSearchResponse> results = busStopService.getBusStopsNearLocation(
            latitude, longitude, radiusKm);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/district/{district}")
    public ResponseEntity<List<BusStopSearchResponse>> getBusStopsByDistrict(
            @PathVariable String district) {
        
        List<BusStopSearchResponse> results = busStopService.getBusStopsByDistrict(district);
        return ResponseEntity.ok(results);
    }
}