package com.bustrackr.repository;

import com.bustrackr.domain.BusArrival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BusArrivalRepository extends JpaRepository<BusArrival, Long> {
    
    @Query("SELECT ba FROM BusArrival ba WHERE ba.busStop.stopId = :stopId ORDER BY ba.estimatedTime ASC")
    List<BusArrival> findByStopIdOrderByEstimatedTime(@Param("stopId") String stopId);
    
    @Query("SELECT ba FROM BusArrival ba WHERE ba.routeId = :routeId AND ba.busStop.stopId = :stopId")
    List<BusArrival> findByRouteIdAndStopId(@Param("routeId") String routeId, @Param("stopId") String stopId);
    
    @Query("SELECT ba FROM BusArrival ba WHERE ba.busStop.stopId = :stopId AND ba.lastUpdated > :since")
    List<BusArrival> findRecentArrivalsByStopId(@Param("stopId") String stopId, @Param("since") LocalDateTime since);
}