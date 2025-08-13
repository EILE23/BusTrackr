package com.bustrackr.service;

import com.bustrackr.domain.BusArrival;
import com.bustrackr.dto.BusArrivalResponse;
import com.bustrackr.repository.BusArrivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusArrivalService {
    
    @Autowired
    private BusArrivalRepository busArrivalRepository;
    
    public List<BusArrivalResponse> getArrivalsByStopId(String stopId) {
        List<BusArrival> arrivals = busArrivalRepository.findByStopIdOrderByEstimatedTime(stopId);
        return arrivals.stream()
                .map(BusArrivalResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<BusArrivalResponse> getArrivalsByRouteAndStop(String routeId, String stopId) {
        List<BusArrival> arrivals = busArrivalRepository.findByRouteIdAndStopId(routeId, stopId);
        return arrivals.stream()
                .map(BusArrivalResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<BusArrivalResponse> getRecentArrivalsByStopId(String stopId, int minutesAgo) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(minutesAgo);
        List<BusArrival> arrivals = busArrivalRepository.findRecentArrivalsByStopId(stopId, since);
        return arrivals.stream()
                .map(BusArrivalResponse::new)
                .collect(Collectors.toList());
    }
    
    public void saveBusArrival(BusArrival busArrival) {
        busArrival.setLastUpdated(LocalDateTime.now());
        busArrivalRepository.save(busArrival);
    }
}