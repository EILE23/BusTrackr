package com.bustrackr.repository;

import com.bustrackr.domain.BusLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BusLocationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BusLocationRepository busLocationRepository;

    private BusLocation location1;
    private BusLocation location2;

    @BeforeEach
    void setUp() {
        location1 = new BusLocation("BUS001", "ROUTE001", 37.4981, 127.0276);
        location1.setSpeed(25.0);
        location1.setCongestion(BusLocation.CongestionLevel.MEDIUM);
        location1.setNextStopId("STOP001");
        location1.setEstimatedArrival(5);
        location1.setLastUpdated(LocalDateTime.now().minusMinutes(1));

        location2 = new BusLocation("BUS002", "ROUTE001", 37.5006, 127.0366);
        location2.setSpeed(30.0);
        location2.setCongestion(BusLocation.CongestionLevel.LOW);
        location2.setNextStopId("STOP002");
        location2.setEstimatedArrival(3);
        location2.setLastUpdated(LocalDateTime.now());

        entityManager.persistAndFlush(location1);
        entityManager.persistAndFlush(location2);
    }

    @Test
    void findByRouteId_shouldReturnLocationsForRoute() {
        // When
        List<BusLocation> found = busLocationRepository.findByRouteId("ROUTE001");

        // Then
        assertThat(found).hasSize(2);
        assertThat(found)
            .extracting(BusLocation::getBusId)
            .containsExactlyInAnyOrder("BUS001", "BUS002");
    }

    @Test
    void findByRouteId_shouldReturnEmptyList_whenRouteNotExists() {
        // When
        List<BusLocation> found = busLocationRepository.findByRouteId("NONEXISTENT");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void findByRouteIdAndLastUpdatedAfter_shouldReturnRecentLocations() {
        // Given
        LocalDateTime cutoff = LocalDateTime.now().minusSeconds(30);

        // When
        List<BusLocation> found = busLocationRepository
            .findByRouteIdAndLastUpdatedAfter("ROUTE001", cutoff);

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getBusId()).isEqualTo("BUS002");
    }

    @Test
    void findLatestLocationsByRoute_shouldReturnLatestForEachBus() {
        // 같은 버스의 이전 위치 추가
        BusLocation oldLocation = new BusLocation("BUS001", "ROUTE001", 37.4900, 127.0200);
        oldLocation.setLastUpdated(LocalDateTime.now().minusHours(1));
        entityManager.persistAndFlush(oldLocation);

        // When
        List<BusLocation> found = busLocationRepository.findLatestLocationsByRoute("ROUTE001");

        // Then
        assertThat(found).hasSize(2);
        
        // BUS001의 최신 위치가 반환되어야 함
        BusLocation bus001Location = found.stream()
            .filter(loc -> "BUS001".equals(loc.getBusId()))
            .findFirst()
            .orElse(null);
        
        assertThat(bus001Location).isNotNull();
        assertThat(bus001Location.getLatitude()).isEqualTo(37.4981);
    }

    @Test
    void save_shouldPersistBusLocation() {
        // Given
        BusLocation newLocation = new BusLocation("BUS003", "ROUTE002", 37.5500, 127.0800);
        newLocation.setSpeed(20.0);
        newLocation.setCongestion(BusLocation.CongestionLevel.HIGH);

        // When
        BusLocation saved = busLocationRepository.save(newLocation);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBusId()).isEqualTo("BUS003");
        assertThat(saved.getRouteId()).isEqualTo("ROUTE002");
        assertThat(saved.getCongestion()).isEqualTo(BusLocation.CongestionLevel.HIGH);
    }
}