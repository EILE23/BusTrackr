package com.bustrackr.repository;

import com.bustrackr.domain.BusRoute;
import com.bustrackr.domain.BusStop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BusStopRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BusStopRepository busStopRepository;

    @Autowired
    private BusRouteRepository busRouteRepository;

    private BusRoute testRoute;
    private BusStop testStop1;
    private BusStop testStop2;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
        testRoute = new BusRoute("TEST001", "테스트노선", BusRoute.RouteType.BRANCH, "테스트방향");
        entityManager.persistAndFlush(testRoute);

        testStop1 = new BusStop("STOP001", "강남역", 37.4981, 127.0276);
        testStop1.setDistrict("강남구");
        testStop1.setDirection("역삼방향");
        testStop1.setRoute(testRoute);

        testStop2 = new BusStop("STOP002", "역삼역", 37.5006, 127.0366);
        testStop2.setDistrict("강남구");
        testStop2.setDirection("선릉방향");
        testStop2.setRoute(testRoute);

        entityManager.persistAndFlush(testStop1);
        entityManager.persistAndFlush(testStop2);
    }

    @Test
    void findById_shouldReturnBusStop_whenExists() {
        // When
        Optional<BusStop> found = busStopRepository.findById("STOP001");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getStopName()).isEqualTo("강남역");
        assertThat(found.get().getDistrict()).isEqualTo("강남구");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        // When
        Optional<BusStop> found = busStopRepository.findById("NONEXISTENT");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void findByStopNameContaining_shouldReturnMatchingStops() {
        // When
        List<BusStop> found = busStopRepository.findByStopNameContaining("강남");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getStopName()).isEqualTo("강남역");
    }

    @Test
    void findByStopNameContaining_shouldReturnEmptyList_whenNoMatch() {
        // When
        List<BusStop> found = busStopRepository.findByStopNameContaining("없는역");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void findByDistrict_shouldReturnStopsInDistrict() {
        // When
        List<BusStop> found = busStopRepository.findByDistrict("강남구");

        // Then
        assertThat(found).hasSize(2);
        assertThat(found)
            .extracting(BusStop::getStopName)
            .containsExactlyInAnyOrder("강남역", "역삼역");
    }

    @Test
    void findByKeyword_shouldReturnMatchingStops() {
        // When
        List<BusStop> foundByName = busStopRepository.findByKeyword("강남");
        List<BusStop> foundById = busStopRepository.findByKeyword("STOP001");

        // Then
        assertThat(foundByName).hasSize(1);
        assertThat(foundByName.get(0).getStopName()).isEqualTo("강남역");

        assertThat(foundById).hasSize(1);
        assertThat(foundById.get(0).getStopId()).isEqualTo("STOP001");
    }

    @Test
    void save_shouldPersistBusStop() {
        // Given
        BusStop newStop = new BusStop("STOP003", "신논현역", 37.5045, 127.0254);
        newStop.setDistrict("강남구");
        newStop.setRoute(testRoute);

        // When
        BusStop saved = busStopRepository.save(newStop);

        // Then
        assertThat(saved.getStopId()).isEqualTo("STOP003");
        
        // 데이터베이스에서 확인
        Optional<BusStop> found = busStopRepository.findById("STOP003");
        assertThat(found).isPresent();
        assertThat(found.get().getStopName()).isEqualTo("신논현역");
    }

    @Test
    void deleteById_shouldRemoveBusStop() {
        // Given
        assertThat(busStopRepository.findById("STOP001")).isPresent();

        // When
        busStopRepository.deleteById("STOP001");

        // Then
        assertThat(busStopRepository.findById("STOP001")).isEmpty();
    }
}