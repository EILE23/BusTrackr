package com.bustrackr.service;

import com.bustrackr.domain.BusLocation;
import com.bustrackr.dto.BusLocationResponse;
import com.bustrackr.repository.BusLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusLocationService {
    
    @Autowired
    private BusLocationRepository busLocationRepository;
    
    public List<BusLocationResponse> getBusLocationsByRoute(String routeId) {
        List<BusLocation> locations = busLocationRepository.findLatestLocationsByRoute(routeId);
        return locations.stream()
                .map(BusLocationResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<BusLocationResponse> getRecentBusLocationsByRoute(String routeId, int minutesAgo) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(minutesAgo);
        List<BusLocation> locations = busLocationRepository.findByRouteIdAndLastUpdatedAfter(routeId, since);
        return locations.stream()
                .map(BusLocationResponse::new)
                .collect(Collectors.toList());
    }
    
    public BusLocationResponse getLatestBusLocation(String busId) {
        BusLocation location = busLocationRepository.findLatestLocationByBusId(busId);
        return location != null ? new BusLocationResponse(location) : null;
    }
    
    public void saveBusLocation(BusLocation busLocation) {
        busLocation.setLastUpdated(LocalDateTime.now());
        busLocationRepository.save(busLocation);
    }
}