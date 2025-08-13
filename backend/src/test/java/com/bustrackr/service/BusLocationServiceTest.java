package com.bustrackr.service;

import com.bustrackr.domain.BusLocation;
import com.bustrackr.dto.BusLocationResponse;
import com.bustrackr.repository.BusLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusLocationServiceTest {

    @Mock
    private BusLocationRepository busLocationRepository;

    @InjectMocks
    private BusLocationService busLocationService;

    private BusLocation testLocation1;
    private BusLocation testLocation2;

    @BeforeEach
    void setUp() {
        testLocation1 = new BusLocation("BUS001", "ROUTE001", 37.4981, 127.0276);
        testLocation1.setSpeed(25.0);
        testLocation1.setCongestion(BusLocation.CongestionLevel.MEDIUM);
        testLocation1.setNextStopId("STOP001");
        testLocation1.setEstimatedArrival(5);
        testLocation1.setLastUpdated(LocalDateTime.now());

        testLocation2 = new BusLocation("BUS002", "ROUTE001", 37.5006, 127.0366);
        testLocation2.setSpeed(30.0);
        testLocation2.setCongestion(BusLocation.CongestionLevel.LOW);
        testLocation2.setNextStopId("STOP002");
        testLocation2.setEstimatedArrival(3);
        testLocation2.setLastUpdated(LocalDateTime.now());
    }

    @Test
    void getBusLocationsByRoute_shouldReturnLocations_whenRouteExists() {
        // Given
        when(busLocationRepository.findLatestLocationsByRoute("ROUTE001"))
            .thenReturn(Arrays.asList(testLocation1, testLocation2));

        // When
        List<BusLocationResponse> result = busLocationService.getBusLocationsByRoute("ROUTE001");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result)
            .extracting(BusLocationResponse::getBusId)
            .containsExactlyInAnyOrder("BUS001", "BUS002");
        
        BusLocationResponse location1 = result.stream()
            .filter(loc -> "BUS001".equals(loc.getBusId()))
            .findFirst()
            .orElse(null);
        
        assertThat(location1).isNotNull();
        assertThat(location1.getRouteId()).isEqualTo("ROUTE001");
        assertThat(location1.getCongestion()).isEqualTo("medium");
        assertThat(location1.getSpeed()).isEqualTo(25.0);
        
        verify(busLocationRepository).findLatestLocationsByRoute("ROUTE001");
    }

    @Test
    void getBusLocationsByRoute_shouldReturnEmptyList_whenRouteNotExists() {
        // Given
        when(busLocationRepository.findLatestLocationsByRoute("NONEXISTENT"))
            .thenReturn(Collections.emptyList());

        // When
        List<BusLocationResponse> result = busLocationService.getBusLocationsByRoute("NONEXISTENT");

        // Then
        assertThat(result).isEmpty();
        verify(busLocationRepository).findLatestLocationsByRoute("NONEXISTENT");
    }

    @Test
    void getRecentBusLocationsByRoute_shouldReturnRecentLocations() {
        // Given
        when(busLocationRepository.findByRouteIdAndLastUpdatedAfter(eq("ROUTE001"), any(LocalDateTime.class)))
            .thenReturn(Arrays.asList(testLocation2));

        // When
        List<BusLocationResponse> result = busLocationService.getRecentBusLocationsByRoute("ROUTE001", 10);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBusId()).isEqualTo("BUS002");
        
        verify(busLocationRepository).findByRouteIdAndLastUpdatedAfter(eq("ROUTE001"), any(LocalDateTime.class));
    }

    @Test
    void getLatestBusLocation_shouldReturnLocation_whenBusExists() {
        // Given
        when(busLocationRepository.findLatestLocationByBusId("BUS001"))
            .thenReturn(testLocation1);

        // When
        BusLocationResponse result = busLocationService.getLatestBusLocation("BUS001");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBusId()).isEqualTo("BUS001");
        assertThat(result.getRouteId()).isEqualTo("ROUTE001");
        assertThat(result.getCongestion()).isEqualTo("medium");
        
        verify(busLocationRepository).findLatestLocationByBusId("BUS001");
    }

    @Test
    void getLatestBusLocation_shouldReturnNull_whenBusNotExists() {
        // Given
        when(busLocationRepository.findLatestLocationByBusId("NONEXISTENT"))
            .thenReturn(null);

        // When
        BusLocationResponse result = busLocationService.getLatestBusLocation("NONEXISTENT");

        // Then
        assertThat(result).isNull();
        verify(busLocationRepository).findLatestLocationByBusId("NONEXISTENT");
    }

    @Test
    void saveBusLocation_shouldSetTimestampAndSave() {
        // Given
        BusLocation newLocation = new BusLocation("BUS003", "ROUTE002", 37.5500, 127.0800);

        // When
        busLocationService.saveBusLocation(newLocation);

        // Then
        assertThat(newLocation.getLastUpdated()).isNotNull();
        verify(busLocationRepository).save(newLocation);
    }
}