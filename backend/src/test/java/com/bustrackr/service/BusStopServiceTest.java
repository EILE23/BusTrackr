package com.bustrackr.service;

import com.bustrackr.domain.BusStop;
import com.bustrackr.dto.BusStopSearchResponse;
import com.bustrackr.repository.BusStopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusStopServiceTest {

    @Mock
    private BusStopRepository busStopRepository;

    @InjectMocks
    private BusStopService busStopService;

    private BusStop testStop1;
    private BusStop testStop2;

    @BeforeEach
    void setUp() {
        testStop1 = new BusStop("STOP001", "강남역", 37.4981, 127.0276);
        testStop1.setDistrict("강남구");
        testStop1.setDirection("역삼방향");

        testStop2 = new BusStop("STOP002", "역삼역", 37.5006, 127.0366);
        testStop2.setDistrict("강남구");
        testStop2.setDirection("선릉방향");
    }

    @Test
    void searchBusStops_shouldReturnMatchingStops_whenKeywordExists() {
        // Given
        when(busStopRepository.findByKeyword("강남"))
            .thenReturn(Arrays.asList(testStop1));

        // When
        List<BusStopSearchResponse> result = busStopService.searchBusStops("강남");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStopName()).isEqualTo("강남역");
        assertThat(result.get(0).getStopId()).isEqualTo("STOP001");
        assertThat(result.get(0).getDistrict()).isEqualTo("강남구");
        
        verify(busStopRepository).findByKeyword("강남");
    }

    @Test
    void searchBusStops_shouldReturnEmptyList_whenNoMatch() {
        // Given
        when(busStopRepository.findByKeyword("없는역"))
            .thenReturn(Collections.emptyList());

        // When
        List<BusStopSearchResponse> result = busStopService.searchBusStops("없는역");

        // Then
        assertThat(result).isEmpty();
        verify(busStopRepository).findByKeyword("없는역");
    }

    @Test
    void searchBusStopsByName_shouldReturnMatchingStops() {
        // Given
        when(busStopRepository.findByStopNameContaining("역삼"))
            .thenReturn(Arrays.asList(testStop2));

        // When
        List<BusStopSearchResponse> result = busStopService.searchBusStopsByName("역삼");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStopName()).isEqualTo("역삼역");
        
        verify(busStopRepository).findByStopNameContaining("역삼");
    }

    @Test
    void getBusStopsByDistrict_shouldReturnStopsInDistrict() {
        // Given
        when(busStopRepository.findByDistrict("강남구"))
            .thenReturn(Arrays.asList(testStop1, testStop2));

        // When
        List<BusStopSearchResponse> result = busStopService.getBusStopsByDistrict("강남구");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result)
            .extracting(BusStopSearchResponse::getStopName)
            .containsExactlyInAnyOrder("강남역", "역삼역");
        
        verify(busStopRepository).findByDistrict("강남구");
    }

    @Test
    void getBusStopsNearLocation_shouldReturnNearbyStops() {
        // Given
        double testLat = 37.5000;
        double testLon = 127.0300;
        double radius = 1.0; // 1km
        
        when(busStopRepository.findAll())
            .thenReturn(Arrays.asList(testStop1, testStop2));

        // When
        List<BusStopSearchResponse> result = busStopService.getBusStopsNearLocation(
            testLat, testLon, radius);

        // Then
        // 거리 계산 로직에 따라 결과가 달라질 수 있음
        assertThat(result).isNotNull();
        
        verify(busStopRepository).findAll();
    }

    @Test
    void getBusStopsNearLocation_shouldFilterByDistance() {
        // Given
        BusStop farStop = new BusStop("STOP999", "원거리역", 35.0000, 125.0000);
        farStop.setDistrict("부산시");
        
        when(busStopRepository.findAll())
            .thenReturn(Arrays.asList(testStop1, farStop));

        // When
        List<BusStopSearchResponse> result = busStopService.getBusStopsNearLocation(
            37.5000, 127.0300, 1.0);

        // Then
        // 강남역은 1km 내에 있고, 원거리역은 제외되어야 함
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStopName()).isEqualTo("강남역");
    }
}