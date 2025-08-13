package com.bustrackr.scheduler;

import com.bustrackr.domain.BusRoute;
import com.bustrackr.repository.BusRouteRepository;
import com.bustrackr.service.BusDataSyncService;
import com.bustrackr.service.BusLocationService;
import com.bustrackr.service.BusArrivalService;
import com.bustrackr.service.WebSocketNotificationService;
import com.bustrackr.service.external.SeoulBusApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class BusDataScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(BusDataScheduler.class);
    
    @Autowired
    private BusDataSyncService busDataSyncService;
    
    @Autowired
    private BusRouteRepository busRouteRepository;
    
    @Autowired
    private BusLocationService busLocationService;
    
    @Autowired
    private BusArrivalService busArrivalService;
    
    @Autowired
    private WebSocketNotificationService webSocketNotificationService;
    
    @Autowired
    private SeoulBusApiService seoulBusApiService;
    
    /**
     * 실시간 버스 위치 업데이트 (30초마다)
     */
    @Scheduled(fixedRate = 30000, initialDelay = 10000)
    public void updateBusLocations() {
        if (!seoulBusApiService.isApiHealthy()) {
            logger.warn("Seoul Bus API is not healthy, skipping location update");
            return;
        }
        
        logger.debug("Starting scheduled bus location update");
        
        try {
            List<BusRoute> routes = busRouteRepository.findAll();
            
            // 비동기로 각 노선의 버스 위치 업데이트
            CompletableFuture<?>[] futures = routes.stream()
                .map(route -> CompletableFuture.runAsync(() -> {
                    try {
                        // 데이터 동기화
                        busDataSyncService.syncBusLocations(route.getRouteId());
                        
                        // WebSocket으로 실시간 전송
                        var locations = busLocationService.getBusLocationsByRoute(route.getRouteId());
                        if (!locations.isEmpty()) {
                            webSocketNotificationService.broadcastBusLocations(route.getRouteId(), locations);
                        }
                        
                    } catch (Exception e) {
                        logger.error("Error updating locations for route: " + route.getRouteId(), e);
                    }
                }))
                .toArray(CompletableFuture[]::new);
                
            CompletableFuture.allOf(futures).join();
            
            logger.debug("Completed scheduled bus location update for {} routes", routes.size());
            
        } catch (Exception e) {
            logger.error("Error in scheduled bus location update", e);
        }
    }
    
    /**
     * 버스 도착정보 업데이트 (1분마다)
     */
    @Scheduled(fixedRate = 60000, initialDelay = 15000)
    public void updateBusArrivals() {
        if (!seoulBusApiService.isApiHealthy()) {
            logger.warn("Seoul Bus API is not healthy, skipping arrival update");
            return;
        }
        
        logger.debug("Starting scheduled bus arrival update");
        
        try {
            // 활성 정류장들의 도착정보 업데이트 (예시로 샘플 데이터의 정류장들)
            String[] activeStations = {"23001", "23002", "23003", "23004"};
            
            CompletableFuture<?>[] futures = java.util.Arrays.stream(activeStations)
                .map(stationId -> CompletableFuture.runAsync(() -> {
                    try {
                        // 데이터 동기화
                        busDataSyncService.syncBusArrivals(stationId);
                        
                        // WebSocket으로 실시간 전송
                        var arrivals = busArrivalService.getArrivalsByStopId(stationId);
                        if (!arrivals.isEmpty()) {
                            webSocketNotificationService.broadcastBusArrivals(stationId, arrivals);
                        }
                        
                    } catch (Exception e) {
                        logger.error("Error updating arrivals for station: " + stationId, e);
                    }
                }))
                .toArray(CompletableFuture[]::new);
                
            CompletableFuture.allOf(futures).join();
            
            logger.debug("Completed scheduled bus arrival update for {} stations", activeStations.length);
            
        } catch (Exception e) {
            logger.error("Error in scheduled bus arrival update", e);
        }
    }
    
    /**
     * 정류장 정보 동기화 (1시간마다)
     */
    @Scheduled(fixedRate = 3600000, initialDelay = 5000)
    public void syncStationData() {
        if (!seoulBusApiService.isApiHealthy()) {
            logger.warn("Seoul Bus API is not healthy, skipping station sync");
            return;
        }
        
        logger.info("Starting scheduled station data sync");
        
        try {
            // 주요 지역의 정류장 정보 동기화
            String[] majorAreas = {"강남", "시청", "역삼", "광화문"};
            
            for (String area : majorAreas) {
                try {
                    busDataSyncService.syncBusStations(area);
                    Thread.sleep(1000); // API 호출 간격 조절
                } catch (Exception e) {
                    logger.error("Error syncing stations for area: " + area, e);
                }
            }
            
            logger.info("Completed scheduled station data sync");
            
        } catch (Exception e) {
            logger.error("Error in scheduled station data sync", e);
        }
    }
    
    /**
     * API 상태 확인 (5분마다)
     */
    @Scheduled(fixedRate = 300000, initialDelay = 0)
    public void checkApiHealth() {
        boolean isHealthy = seoulBusApiService.isApiHealthy();
        
        if (!isHealthy) {
            logger.warn("Seoul Bus API health check failed");
        } else {
            logger.debug("Seoul Bus API health check passed");
        }
    }
}