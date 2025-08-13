package com.bustrackr.repository;

import com.bustrackr.domain.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, String> {
    
    List<BusStop> findByStopNameContaining(String stopName);
    
    List<BusStop> findByDistrict(String district);
    
    @Query("SELECT s FROM BusStop s WHERE s.stopName LIKE %:keyword% OR s.stopId LIKE %:keyword%")
    List<BusStop> findByKeyword(@Param("keyword") String keyword);
    
    // H2에서는 지리적 함수를 지원하지 않으므로 Service 레이어에서 거리 계산으로 처리
    // List<BusStop> findByLocationWithinRadius(Double latitude, Double longitude, Double radius);
}