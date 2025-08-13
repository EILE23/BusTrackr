package com.bustrackr.repository;

import com.bustrackr.domain.BusLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BusLocationRepository extends JpaRepository<BusLocation, Long> {
    
    List<BusLocation> findByRouteId(String routeId);
    
    List<BusLocation> findByRouteIdAndLastUpdatedAfter(String routeId, LocalDateTime since);
    
    @Query("SELECT bl FROM BusLocation bl WHERE bl.routeId = :routeId " +
           "AND bl.lastUpdated = (SELECT MAX(bl2.lastUpdated) FROM BusLocation bl2 WHERE bl2.busId = bl.busId)")
    List<BusLocation> findLatestLocationsByRoute(@Param("routeId") String routeId);
    
    @Query(value = "SELECT * FROM bus_locations bl WHERE bl.bus_id = :busId " +
           "ORDER BY bl.last_updated DESC LIMIT 1", nativeQuery = true)
    BusLocation findLatestLocationByBusId(@Param("busId") String busId);
}