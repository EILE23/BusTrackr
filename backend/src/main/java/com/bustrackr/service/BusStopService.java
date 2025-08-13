package com.bustrackr.service;

import com.bustrackr.domain.BusStop;
import com.bustrackr.dto.BusStopSearchResponse;
import com.bustrackr.repository.BusStopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusStopService {
    
    @Autowired
    private BusStopRepository busStopRepository;
    
    public List<BusStopSearchResponse> searchBusStops(String keyword) {
        List<BusStop> busStops = busStopRepository.findByKeyword(keyword);
        return busStops.stream()
                .map(BusStopSearchResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<BusStopSearchResponse> searchBusStopsByName(String stopName) {
        List<BusStop> busStops = busStopRepository.findByStopNameContaining(stopName);
        return busStops.stream()
                .map(BusStopSearchResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<BusStopSearchResponse> getBusStopsByDistrict(String district) {
        List<BusStop> busStops = busStopRepository.findByDistrict(district);
        return busStops.stream()
                .map(BusStopSearchResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<BusStopSearchResponse> getBusStopsNearLocation(Double latitude, Double longitude, Double radiusKm) {
        // H2 데이터베이스에서는 지리적 쿼리가 제한적이므로, 간단한 거리 계산으로 대체
        List<BusStop> allStops = busStopRepository.findAll();
        return allStops.stream()
                .filter(stop -> calculateDistance(latitude, longitude, stop.getLatitude(), stop.getLongitude()) <= radiusKm)
                .map(BusStopSearchResponse::new)
                .collect(Collectors.toList());
    }
    
    // 두 지점 간의 거리를 계산하는 메서드 (Haversine formula)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}