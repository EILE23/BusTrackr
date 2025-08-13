package com.bustrackr.service.external;

import com.bustrackr.config.BusApiProperties;
import com.bustrackr.dto.external.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class SeoulBusApiService {
    
    private static final Logger logger = LoggerFactory.getLogger(SeoulBusApiService.class);
    
    @Autowired
    private BusApiProperties busApiProperties;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public SeoulBusApiService() {
        this.webClient = WebClient.builder()
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(16 * 1024 * 1024))
            .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 정류소명으로 정류소 검색
     */
    public List<SeoulBusStationInfo> getStationsByName(String stationName) {
        try {
            String url = String.format("%s/stationinfo/getStationByName?serviceKey=%s&stSrch=%s&resultType=json",
                busApiProperties.getBaseUrl(),
                busApiProperties.getServiceKey(),
                stationName);
            
            logger.debug("Calling Seoul Bus API: {}", url);
            
            String response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(busApiProperties.getTimeout()))
                .block();
            
            SeoulBusApiResponse<SeoulBusStationInfo> apiResponse = objectMapper.readValue(
                response, 
                new TypeReference<SeoulBusApiResponse<SeoulBusStationInfo>>() {}
            );
            
            if (apiResponse.getMsgBody() != null && apiResponse.getMsgBody().getItemList() != null) {
                return apiResponse.getMsgBody().getItemList();
            }
            
        } catch (WebClientResponseException e) {
            logger.error("API call failed with status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error calling Seoul Bus API", e);
        }
        
        return Collections.emptyList();
    }
    
    /**
     * 노선ID로 실시간 버스 위치 조회
     */
    public List<SeoulBusLocationInfo> getBusLocationsByRoute(String routeId) {
        try {
            String url = String.format("%s/buspos/getBusPosByRouteSt?serviceKey=%s&busRouteId=%s&resultType=json",
                busApiProperties.getBaseUrl(),
                busApiProperties.getServiceKey(),
                routeId);
            
            logger.debug("Calling Seoul Bus API for locations: {}", url);
            
            String response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(busApiProperties.getTimeout()))
                .block();
            
            SeoulBusApiResponse<SeoulBusLocationInfo> apiResponse = objectMapper.readValue(
                response, 
                new TypeReference<SeoulBusApiResponse<SeoulBusLocationInfo>>() {}
            );
            
            if (apiResponse.getMsgBody() != null && apiResponse.getMsgBody().getItemList() != null) {
                return apiResponse.getMsgBody().getItemList();
            }
            
        } catch (WebClientResponseException e) {
            logger.error("API call failed with status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error calling Seoul Bus API for locations", e);
        }
        
        return Collections.emptyList();
    }
    
    /**
     * 정류소ID로 버스 도착정보 조회
     */
    public List<SeoulBusArrivalInfo> getArrivalsByStation(String stationId) {
        try {
            String url = String.format("%s/stationinfo/getStationByUid?serviceKey=%s&arsId=%s&resultType=json",
                busApiProperties.getBaseUrl(),
                busApiProperties.getServiceKey(),
                stationId);
            
            logger.debug("Calling Seoul Bus API for arrivals: {}", url);
            
            String response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(busApiProperties.getTimeout()))
                .block();
            
            SeoulBusApiResponse<SeoulBusArrivalInfo> apiResponse = objectMapper.readValue(
                response, 
                new TypeReference<SeoulBusApiResponse<SeoulBusArrivalInfo>>() {}
            );
            
            if (apiResponse.getMsgBody() != null && apiResponse.getMsgBody().getItemList() != null) {
                return apiResponse.getMsgBody().getItemList();
            }
            
        } catch (WebClientResponseException e) {
            logger.error("API call failed with status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error calling Seoul Bus API for arrivals", e);
        }
        
        return Collections.emptyList();
    }
    
    /**
     * API 연결 상태 확인
     */
    public boolean isApiHealthy() {
        try {
            // 간단한 API 호출로 연결 상태 확인
            getStationsByName("test");
            return true;
        } catch (Exception e) {
            logger.warn("Seoul Bus API health check failed", e);
            return false;
        }
    }
}