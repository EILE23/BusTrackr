package com.bustrackr.controller;

import com.bustrackr.dto.BusArrivalResponse;
import com.bustrackr.service.BusArrivalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus-arrivals")
@CrossOrigin(origins = "http://localhost:3002")
public class BusArrivalController {
    
    @Autowired
    private BusArrivalService busArrivalService;
    
    @GetMapping("/stop/{stopId}")
    public ResponseEntity<List<BusArrivalResponse>> getArrivalsByStopId(
            @PathVariable String stopId) {
        
        List<BusArrivalResponse> arrivals = busArrivalService.getArrivalsByStopId(stopId);
        return ResponseEntity.ok(arrivals);
    }
    
    @GetMapping("/route/{routeId}/stop/{stopId}")
    public ResponseEntity<List<BusArrivalResponse>> getArrivalsByRouteAndStop(
            @PathVariable String routeId,
            @PathVariable String stopId) {
        
        List<BusArrivalResponse> arrivals = busArrivalService.getArrivalsByRouteAndStop(
            routeId, stopId);
        return ResponseEntity.ok(arrivals);
    }
    
    @GetMapping("/stop/{stopId}/recent")
    public ResponseEntity<List<BusArrivalResponse>> getRecentArrivalsByStopId(
            @PathVariable String stopId,
            @RequestParam(defaultValue = "30") int minutesAgo) {
        
        List<BusArrivalResponse> arrivals = busArrivalService.getRecentArrivalsByStopId(
            stopId, minutesAgo);
        return ResponseEntity.ok(arrivals);
    }
}