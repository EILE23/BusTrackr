package com.bustrackr.repository;

import com.bustrackr.domain.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, String> {
    
    List<BusRoute> findByRouteNameContaining(String routeName);
    
    List<BusRoute> findByRouteType(BusRoute.RouteType routeType);
    
    @Query("SELECT r FROM BusRoute r WHERE r.routeName LIKE %:keyword% OR r.routeId LIKE %:keyword%")
    List<BusRoute> findByKeyword(@Param("keyword") String keyword);
}