package com.bustrackr.controller;

import com.bustrackr.service.BusLocationService;
import com.bustrackr.service.BusArrivalService;
import com.bustrackr.service.WebSocketNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    
    @Autowired
    private BusLocationService busLocationService;
    
    @Autowired
    private BusArrivalService busArrivalService;
    
    @Autowired
    private WebSocketNotificationService webSocketNotificationService;
    
    /**
     * 클라이언트가 특정 노선의 버스 위치를 요청
     */
    @MessageMapping("/bus-locations/{routeId}")
    @SendTo("/topic/bus-locations/{routeId}")
    public Object getBusLocationsByRoute(@DestinationVariable String routeId) {
        logger.debug("WebSocket request for bus locations of route: {}", routeId);
        
        try {
            return busLocationService.getBusLocationsByRoute(routeId);
        } catch (Exception e) {
            logger.error("Error handling WebSocket request for bus locations", e);
            return new ErrorResponse("Failed to get bus locations for route: " + routeId);
        }
    }
    
    /**
     * 클라이언트가 특정 정류장의 도착 정보를 요청
     */
    @MessageMapping("/bus-arrivals/{stopId}")
    @SendTo("/topic/bus-arrivals/{stopId}")
    public Object getBusArrivalsByStop(@DestinationVariable String stopId) {
        logger.debug("WebSocket request for bus arrivals at stop: {}", stopId);
        
        try {
            return busArrivalService.getArrivalsByStopId(stopId);
        } catch (Exception e) {
            logger.error("Error handling WebSocket request for bus arrivals", e);
            return new ErrorResponse("Failed to get bus arrivals for stop: " + stopId);
        }
    }
    
    /**
     * 클라이언트가 연결을 확인하는 ping 요청
     */
    @MessageMapping("/ping")
    @SendTo("/topic/system-status")
    public Object ping() {
        logger.debug("WebSocket ping received");
        return new PingResponse("pong", System.currentTimeMillis());
    }
    
    // 응답 DTO 클래스들
    public static class ErrorResponse {
        private final String error;
        private final long timestamp;
        
        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getError() { return error; }
        public long getTimestamp() { return timestamp; }
    }
    
    public static class PingResponse {
        private final String message;
        private final long timestamp;
        
        public PingResponse(String message, long timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }
        
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }
}