package com.bustrackr.service;

import com.bustrackr.domain.*;
import com.bustrackr.dto.external.*;
import com.bustrackr.repository.*;
import com.bustrackr.service.external.SeoulBusApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BusDataSyncService {
    
    private static final Logger logger = LoggerFactory.getLogger(BusDataSyncService.class);
    
    @Autowired
    private SeoulBusApiService seoulBusApiService;
    
    @Autowired
    private BusStopRepository busStopRepository;
    
    @Autowired
    private BusLocationRepository busLocationRepository;
    
    @Autowired
    private BusArrivalRepository busArrivalRepository;
    
    @Autowired
    private BusRouteRepository busRouteRepository;
    
    /**
     * 정류장 정보 동기화
     */
    @Transactional
    public void syncBusStations(String stationName) {
        logger.info("Syncing bus stations for: {}", stationName);
        
        try {
            List<SeoulBusStationInfo> stations = seoulBusApiService.getStationsByName(stationName);
            
            for (SeoulBusStationInfo stationInfo : stations) {
                syncSingleBusStation(stationInfo);
            }
            
            logger.info("Successfully synced {} stations", stations.size());
            
        } catch (Exception e) {
            logger.error("Error syncing bus stations for: " + stationName, e);
        }
    }
    
    /**
     * 단일 정류장 정보 동기화
     */
    private void syncSingleBusStation(SeoulBusStationInfo stationInfo) {
        Optional<BusStop> existingStop = busStopRepository.findById(stationInfo.getStId());
        
        BusStop busStop;
        if (existingStop.isPresent()) {
            busStop = existingStop.get();
        } else {
            busStop = new BusStop();
            busStop.setStopId(stationInfo.getStId());
        }
        
        busStop.setStopName(stationInfo.getStNm());
        busStop.setDirection(stationInfo.getDirection());
        
        // 좌표 변환 (서울시 API는 TM 좌표계 사용)
        if (stationInfo.getPosX() != null && stationInfo.getPosY() != null) {
            try {
                busStop.setLatitude(Double.parseDouble(stationInfo.getPosY()));
                busStop.setLongitude(Double.parseDouble(stationInfo.getPosX()));
            } catch (NumberFormatException e) {
                logger.warn("Invalid coordinates for station: {}", stationInfo.getStId());
            }
        }
        
        busStopRepository.save(busStop);
    }
    
    /**
     * 특정 노선의 실시간 버스 위치 동기화
     */
    @Transactional
    public void syncBusLocations(String routeId) {
        logger.debug("Syncing bus locations for route: {}", routeId);
        
        try {
            List<SeoulBusLocationInfo> locations = seoulBusApiService.getBusLocationsByRoute(routeId);
            
            for (SeoulBusLocationInfo locationInfo : locations) {
                syncSingleBusLocation(locationInfo);
            }
            
            logger.debug("Successfully synced {} bus locations for route {}", locations.size(), routeId);
            
        } catch (Exception e) {
            logger.error("Error syncing bus locations for route: " + routeId, e);
        }
    }
    
    /**
     * 단일 버스 위치 정보 동기화
     */
    private void syncSingleBusLocation(SeoulBusLocationInfo locationInfo) {
        BusLocation busLocation = new BusLocation();
        busLocation.setBusId(locationInfo.getPlateNo());
        busLocation.setRouteId(locationInfo.getBusRouteId());
        
        // 좌표 설정
        if (locationInfo.getPosX() != null && locationInfo.getPosY() != null) {
            try {
                busLocation.setLatitude(Double.parseDouble(locationInfo.getPosY()));
                busLocation.setLongitude(Double.parseDouble(locationInfo.getPosX()));
            } catch (NumberFormatException e) {
                logger.warn("Invalid coordinates for bus: {}", locationInfo.getPlateNo());
                return;
            }
        }
        
        // 혼잡도 설정
        if (locationInfo.getCongetion() != null) {
            try {
                int congestionLevel = Integer.parseInt(locationInfo.getCongetion());
                if (congestionLevel <= 1) {
                    busLocation.setCongestion(BusLocation.CongestionLevel.LOW);
                } else if (congestionLevel <= 3) {
                    busLocation.setCongestion(BusLocation.CongestionLevel.MEDIUM);
                } else {
                    busLocation.setCongestion(BusLocation.CongestionLevel.HIGH);
                }
            } catch (NumberFormatException e) {
                busLocation.setCongestion(BusLocation.CongestionLevel.LOW);
            }
        }
        
        busLocation.setNextStopId(locationInfo.getStationId());
        busLocation.setLastUpdated(LocalDateTime.now());
        
        busLocationRepository.save(busLocation);
    }
    
    /**
     * 특정 정류장의 버스 도착정보 동기화
     */
    @Transactional
    public void syncBusArrivals(String stationId) {
        logger.debug("Syncing bus arrivals for station: {}", stationId);
        
        try {
            List<SeoulBusArrivalInfo> arrivals = seoulBusApiService.getArrivalsByStation(stationId);
            
            // 기존 도착 정보 삭제 (최신 정보로 교체)
            Optional<BusStop> busStop = busStopRepository.findById(stationId);
            if (busStop.isPresent()) {
                // 기존 도착 정보 삭제는 별도의 로직으로 구현 필요
                for (SeoulBusArrivalInfo arrivalInfo : arrivals) {
                    syncSingleBusArrival(arrivalInfo, busStop.get());
                }
            }
            
            logger.debug("Successfully synced {} bus arrivals for station {}", arrivals.size(), stationId);
            
        } catch (Exception e) {
            logger.error("Error syncing bus arrivals for station: " + stationId, e);
        }
    }
    
    /**
     * 단일 버스 도착정보 동기화
     */
    private void syncSingleBusArrival(SeoulBusArrivalInfo arrivalInfo, BusStop busStop) {
        // 첫 번째 버스 정보
        if (arrivalInfo.getArrmsgSec1() != null && !arrivalInfo.getArrmsgSec1().isEmpty()) {
            BusArrival arrival1 = new BusArrival();
            arrival1.setRouteId(arrivalInfo.getBusRouteId());
            arrival1.setBusStop(busStop);
            arrival1.setPlateNumber(arrivalInfo.getPlainNo1());
            
            try {
                int arrivalSeconds = Integer.parseInt(arrivalInfo.getArrmsgSec1());
                arrival1.setEstimatedTime(arrivalSeconds / 60); // 분으로 변환
            } catch (NumberFormatException e) {
                arrival1.setEstimatedTime(0);
            }
            
            try {
                arrival1.setRemainingStops(Integer.parseInt(arrivalInfo.getRerideNum1()));
            } catch (NumberFormatException e) {
                arrival1.setRemainingStops(0);
            }
            
            arrival1.setCongestion(BusArrival.CongestionLevel.LOW); // 기본값
            arrival1.setLastUpdated(LocalDateTime.now());
            
            busArrivalRepository.save(arrival1);
        }
        
        // 두 번째 버스 정보
        if (arrivalInfo.getArrmsgSec2() != null && !arrivalInfo.getArrmsgSec2().isEmpty()) {
            BusArrival arrival2 = new BusArrival();
            arrival2.setRouteId(arrivalInfo.getBusRouteId());
            arrival2.setBusStop(busStop);
            arrival2.setPlateNumber(arrivalInfo.getPlainNo2());
            
            try {
                int arrivalSeconds = Integer.parseInt(arrivalInfo.getArrmsgSec2());
                arrival2.setEstimatedTime(arrivalSeconds / 60); // 분으로 변환
            } catch (NumberFormatException e) {
                arrival2.setEstimatedTime(0);
            }
            
            try {
                arrival2.setRemainingStops(Integer.parseInt(arrivalInfo.getRerideNum2()));
            } catch (NumberFormatException e) {
                arrival2.setRemainingStops(0);
            }
            
            arrival2.setCongestion(BusArrival.CongestionLevel.LOW); // 기본값
            arrival2.setLastUpdated(LocalDateTime.now());
            
            busArrivalRepository.save(arrival2);
        }
    }
}