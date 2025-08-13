package com.bustrackr.service;

import com.bustrackr.dto.BusArrivalResponse;
import com.bustrackr.dto.BusLocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketNotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketNotificationService.class);
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * 특정 노선의 버스 위치 업데이트를 클라이언트에 브로드캐스트
     */
    public void broadcastBusLocations(String routeId, List<BusLocationResponse> locations) {
        try {
            String destination = "/topic/bus-locations/" + routeId;
            messagingTemplate.convertAndSend(destination, locations);
            
            logger.debug("Broadcasted {} bus locations for route {} to {}", 
                locations.size(), routeId, destination);
            
        } catch (Exception e) {
            logger.error("Error broadcasting bus locations for route: " + routeId, e);
        }
    }
    
    /**
     * 특정 정류장의 버스 도착 정보를 클라이언트에 브로드캐스트
     */
    public void broadcastBusArrivals(String stopId, List<BusArrivalResponse> arrivals) {
        try {
            String destination = "/topic/bus-arrivals/" + stopId;
            messagingTemplate.convertAndSend(destination, arrivals);
            
            logger.debug("Broadcasted {} bus arrivals for stop {} to {}", 
                arrivals.size(), stopId, destination);
            
        } catch (Exception e) {
            logger.error("Error broadcasting bus arrivals for stop: " + stopId, e);
        }
    }
    
    /**
     * 일반적인 알림을 모든 클라이언트에 브로드캐스트
     */
    public void broadcastNotification(String message) {
        try {
            messagingTemplate.convertAndSend("/topic/notifications", message);
            logger.debug("Broadcasted notification: {}", message);
            
        } catch (Exception e) {
            logger.error("Error broadcasting notification: " + message, e);
        }
    }
    
    /**
     * 시스템 상태 정보를 브로드캐스트
     */
    public void broadcastSystemStatus(Object status) {
        try {
            messagingTemplate.convertAndSend("/topic/system-status", status);
            logger.debug("Broadcasted system status");
            
        } catch (Exception e) {
            logger.error("Error broadcasting system status", e);
        }
    }
}