package com.bustrackr.config;

import com.bustrackr.domain.*;
import com.bustrackr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private BusRouteRepository busRouteRepository;
    
    @Autowired
    private BusStopRepository busStopRepository;
    
    @Autowired
    private BusLocationRepository busLocationRepository;
    
    @Autowired
    private BusArrivalRepository busArrivalRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadSampleData();
    }

    private void loadSampleData() {
        // 노선 데이터
        BusRoute route472 = new BusRoute("472", "472번", BusRoute.RouteType.BRANCH, "강남 → 시청");
        route472.setStartStop("강남구청");
        route472.setEndStop("시청역");
        
        BusRoute route143 = new BusRoute("143", "143번", BusRoute.RouteType.TRUNK, "역삼 → 광화문");
        route143.setStartStop("역삼역");
        route143.setEndStop("광화문");
        
        busRouteRepository.save(route472);
        busRouteRepository.save(route143);

        // 정류장 데이터
        BusStop stop1 = new BusStop("23001", "강남구청", 37.5172, 127.0473);
        stop1.setDistrict("강남구");
        stop1.setDirection("시청방향");
        stop1.setRoute(route472);
        
        BusStop stop2 = new BusStop("23002", "시청역", 37.5658, 126.9779);
        stop2.setDistrict("중구");
        stop2.setDirection("강남방향");
        stop2.setRoute(route472);
        
        BusStop stop3 = new BusStop("23003", "역삼역", 37.5006, 127.0366);
        stop3.setDistrict("강남구");
        stop3.setDirection("광화문방향");
        stop3.setRoute(route143);
        
        BusStop stop4 = new BusStop("23004", "광화문", 37.5720, 126.9769);
        stop4.setDistrict("종로구");
        stop4.setDirection("역삼방향");
        stop4.setRoute(route143);
        
        busStopRepository.save(stop1);
        busStopRepository.save(stop2);
        busStopRepository.save(stop3);
        busStopRepository.save(stop4);

        // 버스 위치 데이터
        BusLocation location1 = new BusLocation("472001", "472", 37.5665, 126.9780);
        location1.setSpeed(25.0);
        location1.setCongestion(BusLocation.CongestionLevel.MEDIUM);
        location1.setNextStopId("23002");
        location1.setEstimatedArrival(3);
        
        BusLocation location2 = new BusLocation("143001", "143", 37.5635, 126.9758);
        location2.setSpeed(30.0);
        location2.setCongestion(BusLocation.CongestionLevel.LOW);
        location2.setNextStopId("23004");
        location2.setEstimatedArrival(8);
        
        busLocationRepository.save(location1);
        busLocationRepository.save(location2);

        // 도착 예정 시간 데이터
        BusArrival arrival1 = new BusArrival("472", stop2, 3, 2);
        arrival1.setCongestion(BusArrival.CongestionLevel.MEDIUM);
        arrival1.setPlateNumber("서울70바1234");
        
        BusArrival arrival2 = new BusArrival("143", stop4, 8, 5);
        arrival2.setCongestion(BusArrival.CongestionLevel.LOW);
        arrival2.setPlateNumber("서울70사5678");
        
        BusArrival arrival3 = new BusArrival("472", stop1, 12, 7);
        arrival3.setCongestion(BusArrival.CongestionLevel.HIGH);
        arrival3.setPlateNumber("서울70아9012");
        
        busArrivalRepository.save(arrival1);
        busArrivalRepository.save(arrival2);
        busArrivalRepository.save(arrival3);
        
        System.out.println("샘플 데이터 로딩 완료!");
    }
}