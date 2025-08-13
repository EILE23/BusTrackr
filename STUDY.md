# 🚌 BusTrackr 프로젝트 코드 완전 분석

> JavaScript 개발자를 위한 Java/Spring Boot 코드 이해 가이드

## 📚 목차
- [프로젝트 구조](#-프로젝트-구조)
- [주요 개념 설명](#-주요-개념-설명)
- [도메인 모델 분석](#-도메인-모델-분석)
- [Repository 레이어 분석](#-repository-레이어-분석)
- [Service 레이어 분석](#-service-레이어-분석)
- [Controller 레이어 분석](#-controller-레이어-분석)
- [설정 및 구성 분석](#-설정-및-구성-분석)
- [테스트 코드 분석](#-테스트-코드-분석)
- [JavaScript vs Java 비교](#-javascript-vs-java-비교)

---

## 🏗 프로젝트 구조

```
backend/
├── src/main/java/com/bustrackr/          # 메인 애플리케이션 코드
│   ├── BusTrackrApplication.java         # 메인 실행 클래스 (JS의 index.js와 유사)
│   ├── config/                           # 설정 클래스들 (JS의 config/ 폴더와 유사)
│   ├── controller/                       # HTTP 요청 처리 (Express.js의 routes와 유사)
│   ├── service/                          # 비즈니스 로직 (JS의 service layer와 동일)
│   ├── repository/                       # 데이터베이스 접근 (JS의 DAO/ORM layer와 유사)
│   ├── domain/                           # 엔티티/모델 (JS의 model과 유사)
│   ├── dto/                              # 데이터 전송 객체 (API 응답 형태)
│   └── scheduler/                        # 주기적 작업 (JS의 cron job과 유사)
├── src/main/resources/                   # 리소스 파일들
│   └── application.yml                   # 설정 파일 (JS의 .env + config.json과 유사)
└── src/test/java/                        # 테스트 코드 (Jest 테스트와 유사)
```

---

## 🎯 주요 개념 설명

### 1. 어노테이션 (Annotations)
Java의 어노테이션은 JavaScript의 데코레이터와 비슷한 개념입니다.

```java
@Component  // Spring이 이 클래스를 관리하겠다는 의미 (JS의 모듈 export와 유사)
@Service    // 비즈니스 로직을 담당하는 클래스라는 표시
@Repository // 데이터베이스 접근을 담당하는 클래스
@Controller // HTTP 요청을 처리하는 클래스 (Express router와 유사)
```

### 2. 의존성 주입 (Dependency Injection)
Spring이 자동으로 객체들을 연결해주는 기능입니다.

```java
@Autowired
private BusStopRepository busStopRepository;  // Spring이 자동으로 주입
```

JavaScript로 비교하면:
```javascript
// JavaScript (수동 주입)
const busStopService = new BusStopService(new BusStopRepository());

// Java Spring (자동 주입)
// @Autowired로 Spring이 자동으로 해줌
```

### 3. JPA/Hibernate (ORM)
JavaScript의 Sequelize, TypeORM과 같은 ORM입니다.

```java
// Java JPA
@Entity
public class BusStop {
    @Id
    private String stopId;
    
    @OneToMany(mappedBy = "busStop")
    private List<BusArrival> arrivals;
}
```

```javascript
// JavaScript (Sequelize와 비교)
const BusStop = sequelize.define('BusStop', {
  stopId: { type: DataTypes.STRING, primaryKey: true }
});

BusStop.hasMany(BusArrival, { foreignKey: 'stopId' });
```

---

## 🏢 도메인 모델 분석

### BusStop.java - 버스 정류장 엔티티
```java
@Entity  // JPA 엔티티임을 표시 (데이터베이스 테이블과 매핑)
@Table(name = "bus_stops")  // 테이블 이름 지정
public class BusStop {
    
    @Id  // 기본키 (Primary Key)
    private String stopId;  // 정류장 ID
    
    @Column(nullable = false)  // NOT NULL 제약조건
    private String stopName;   // 정류장 이름
    
    @Column(nullable = false)
    private Double latitude;   // 위도 (JavaScript의 number와 같음)
    
    @Column(nullable = false)
    private Double longitude;  // 경도
    
    // 관계 매핑: 하나의 정류장은 여러 도착정보를 가짐 (1:N)
    @OneToMany(mappedBy = "busStop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BusArrival> arrivals;  // JavaScript의 Array와 같음
    
    // 생성자 (JavaScript의 constructor와 같음)
    public BusStop() {}
    
    public BusStop(String stopId, String stopName, Double latitude, Double longitude) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getter/Setter 메서드들 (JavaScript의 get/set과 같음)
    public String getStopId() { return stopId; }
    public void setStopId(String stopId) { this.stopId = stopId; }
    // ... 다른 getter/setter들
}
```

**JavaScript로 표현하면:**
```javascript
class BusStop {
  constructor(stopId, stopName, latitude, longitude) {
    this.stopId = stopId;
    this.stopName = stopName;
    this.latitude = latitude;
    this.longitude = longitude;
    this.arrivals = []; // OneToMany 관계
  }
  
  // getter/setter는 JavaScript에서는 속성 접근으로 가능
}
```

### BusLocation.java - 버스 위치 정보
```java
@Entity
@Table(name = "bus_locations")
public class BusLocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가 ID
    private Long id;
    
    @Column(nullable = false)
    private String busId;      // 버스 ID
    
    @Column(nullable = false)
    private String routeId;    // 노선 ID
    
    @Column(nullable = false)
    private Double latitude;   // 현재 위도
    
    @Column(nullable = false)
    private Double longitude;  // 현재 경도
    
    @Column
    private Double speed;      // 속도 (null 허용)
    
    // Enum 사용: JavaScript의 const ENUM_VALUES = { ... }과 비슷
    @Column
    @Enumerated(EnumType.STRING)  // 문자열로 저장
    private CongestionLevel congestion;
    
    // 내부 enum 정의
    public enum CongestionLevel {
        LOW,     // "여유"
        MEDIUM,  // "보통"  
        HIGH     // "혼잡"
    }
    
    @Column(nullable = false)
    private LocalDateTime lastUpdated;  // JavaScript의 Date와 같음
    
    // 기본 생성자에서 현재 시간 설정
    public BusLocation() {
        this.lastUpdated = LocalDateTime.now();
    }
}
```

**JavaScript Enum 비교:**
```javascript
// JavaScript
const CongestionLevel = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH'
};

class BusLocation {
  constructor() {
    this.lastUpdated = new Date(); // Java의 LocalDateTime.now()와 같음
  }
}
```

---

## 📊 Repository 레이어 분석

Repository는 데이터베이스와 직접 소통하는 계층입니다. JavaScript의 DAO나 ORM 모델과 같습니다.

### BusStopRepository.java
```java
@Repository  // Spring Data JPA가 자동으로 구현체를 만들어줌
public interface BusStopRepository extends JpaRepository<BusStop, String> {
    //                                     ^엔티티타입  ^ID타입
    
    // 메서드 이름으로 쿼리 자동 생성 (Spring Data JPA 기능)
    List<BusStop> findByStopNameContaining(String stopName);
    // SQL: SELECT * FROM bus_stops WHERE stop_name LIKE '%stopName%'
    
    List<BusStop> findByDistrict(String district);
    // SQL: SELECT * FROM bus_stops WHERE district = ?
    
    // 직접 JPQL 쿼리 작성 (SQL과 비슷하지만 엔티티 기반)
    @Query("SELECT s FROM BusStop s WHERE s.stopName LIKE %:keyword% OR s.stopId LIKE %:keyword%")
    List<BusStop> findByKeyword(@Param("keyword") String keyword);
}
```

**JavaScript (Sequelize) 비교:**
```javascript
// JavaScript Sequelize
class BusStopRepository {
  async findByStopNameContaining(stopName) {
    return await BusStop.findAll({
      where: {
        stopName: { [Op.like]: `%${stopName}%` }
      }
    });
  }
  
  async findByDistrict(district) {
    return await BusStop.findAll({
      where: { district }
    });
  }
  
  async findByKeyword(keyword) {
    return await BusStop.findAll({
      where: {
        [Op.or]: [
          { stopName: { [Op.like]: `%${keyword}%` } },
          { stopId: { [Op.like]: `%${keyword}%` } }
        ]
      }
    });
  }
}
```

### Spring Data JPA의 마법 ✨
Java에서는 메서드 이름만 정의하면 Spring이 자동으로 SQL을 생성합니다:

```java
// 메서드 이름          →  생성되는 SQL
findByStopName          →  WHERE stop_name = ?
findByStopNameContaining →  WHERE stop_name LIKE %?%
findByDistrictAndStopName → WHERE district = ? AND stop_name = ?
findByLatitudeBetween   →  WHERE latitude BETWEEN ? AND ?
```

---

## 🔧 Service 레이어 분석

Service는 비즈니스 로직을 담당합니다. Repository를 사용해서 데이터를 가져오고 가공합니다.

### BusStopService.java
```java
@Service  // Spring이 관리하는 비즈니스 로직 클래스
public class BusStopService {
    
    @Autowired  // 자동으로 BusStopRepository 주입
    private BusStopRepository busStopRepository;
    
    // 정류장 검색 메서드
    public List<BusStopSearchResponse> searchBusStops(String keyword) {
        // 1. Repository에서 데이터 조회
        List<BusStop> busStops = busStopRepository.findByKeyword(keyword);
        
        // 2. 엔티티를 DTO로 변환 (stream은 JavaScript의 map과 비슷)
        return busStops.stream()
                .map(BusStopSearchResponse::new)  // 생성자 참조
                .collect(Collectors.toList());    // 리스트로 수집
    }
    
    // 위치 기반 검색 (거리 계산 포함)
    public List<BusStopSearchResponse> getBusStopsNearLocation(
            Double latitude, Double longitude, Double radiusKm) {
        
        // 모든 정류장 조회
        List<BusStop> allStops = busStopRepository.findAll();
        
        // 거리 계산하여 필터링 (JavaScript의 filter + map과 같음)
        return allStops.stream()
                .filter(stop -> calculateDistance(
                    latitude, longitude, 
                    stop.getLatitude(), stop.getLongitude()) <= radiusKm)
                .map(BusStopSearchResponse::new)
                .collect(Collectors.toList());
    }
    
    // Haversine 공식을 사용한 거리 계산
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
```

**JavaScript 버전:**
```javascript
class BusStopService {
  constructor(busStopRepository) {
    this.busStopRepository = busStopRepository;
  }
  
  async searchBusStops(keyword) {
    // 1. Repository에서 데이터 조회
    const busStops = await this.busStopRepository.findByKeyword(keyword);
    
    // 2. 엔티티를 DTO로 변환
    return busStops.map(stop => new BusStopSearchResponse(stop));
  }
  
  async getBusStopsNearLocation(latitude, longitude, radiusKm) {
    const allStops = await this.busStopRepository.findAll();
    
    return allStops
      .filter(stop => {
        const distance = this.calculateDistance(
          latitude, longitude,
          stop.latitude, stop.longitude
        );
        return distance <= radiusKm;
      })
      .map(stop => new BusStopSearchResponse(stop));
  }
  
  calculateDistance(lat1, lon1, lat2, lon2) {
    // Haversine 공식 (Java와 동일)
    const R = 6371;
    const latDistance = this.toRadians(lat2 - lat1);
    const lonDistance = this.toRadians(lon2 - lon1);
    
    const a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
              Math.cos(this.toRadians(lat1)) * Math.cos(this.toRadians(lat2)) *
              Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }
  
  toRadians(degrees) {
    return degrees * Math.PI / 180;
  }
}
```

### Java Stream API vs JavaScript Array Methods

Java Stream API는 JavaScript의 배열 메서드와 매우 비슷합니다:

```java
// Java Stream API
list.stream()
    .filter(item -> item.getAge() > 18)    // JavaScript의 .filter()
    .map(item -> item.getName())           // JavaScript의 .map()  
    .collect(Collectors.toList());         // 결과를 리스트로 수집
```

```javascript
// JavaScript
list
  .filter(item => item.age > 18)
  .map(item => item.name)
  // collect는 필요없음 (이미 배열)
```

---

## 🎮 Controller 레이어 분석

Controller는 HTTP 요청을 받아서 처리하는 계층입니다. Express.js의 라우터와 같습니다.

### BusStopController.java
```java
@RestController  // RESTful API 컨트롤러 (JSON 응답)
@RequestMapping("/bus-stops")  // 기본 경로 (Express의 router.use('/bus-stops')와 같음)
@CrossOrigin(origins = "http://localhost:3002")  // CORS 설정
public class BusStopController {
    
    @Autowired
    private BusStopService busStopService;  // 서비스 주입
    
    @GetMapping("/search")  // GET /bus-stops/search (Express의 router.get('/search')와 같음)
    public ResponseEntity<List<BusStopSearchResponse>> searchBusStops(
            @RequestParam(required = false) String keyword,    // ?keyword=값
            @RequestParam(required = false) String name,       // ?name=값
            @RequestParam(required = false) String district) { // ?district=값
        
        List<BusStopSearchResponse> results;
        
        // 조건에 따른 분기 처리
        if (keyword != null && !keyword.trim().isEmpty()) {
            results = busStopService.searchBusStops(keyword.trim());
        } else if (name != null && !name.trim().isEmpty()) {
            results = busStopService.searchBusStopsByName(name.trim());
        } else if (district != null && !district.trim().isEmpty()) {
            results = busStopService.getBusStopsByDistrict(district.trim());
        } else {
            // 파라미터가 없으면 400 Bad Request 반환
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(results);  // 200 OK + JSON 응답
    }
    
    @GetMapping("/nearby")  // GET /bus-stops/nearby
    public ResponseEntity<List<BusStopSearchResponse>> getNearbyBusStops(
            @RequestParam Double latitude,                          // 필수 파라미터
            @RequestParam Double longitude,                         // 필수 파라미터
            @RequestParam(defaultValue = "1.0") Double radiusKm) {  // 기본값 1.0km
        
        // 필수 파라미터 검증
        if (latitude == null || longitude == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<BusStopSearchResponse> results = busStopService.getBusStopsNearLocation(
            latitude, longitude, radiusKm);
            
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/district/{district}")  // GET /bus-stops/district/강남구 (경로 변수)
    public ResponseEntity<List<BusStopSearchResponse>> getBusStopsByDistrict(
            @PathVariable String district) {  // URL 경로의 {district} 값
        
        List<BusStopSearchResponse> results = busStopService.getBusStopsByDistrict(district);
        return ResponseEntity.ok(results);
    }
}
```

**Express.js 버전:**
```javascript
const express = require('express');
const router = express.Router();

// CORS 설정
router.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', 'http://localhost:3002');
  next();
});

// GET /bus-stops/search
router.get('/search', async (req, res) => {
  const { keyword, name, district } = req.query;
  
  let results;
  
  if (keyword?.trim()) {
    results = await busStopService.searchBusStops(keyword.trim());
  } else if (name?.trim()) {
    results = await busStopService.searchBusStopsByName(name.trim());
  } else if (district?.trim()) {
    results = await busStopService.getBusStopsByDistrict(district.trim());
  } else {
    return res.status(400).json({ error: 'Missing required parameter' });
  }
  
  res.json(results);
});

// GET /bus-stops/nearby
router.get('/nearby', async (req, res) => {
  const { latitude, longitude, radiusKm = 1.0 } = req.query;
  
  if (!latitude || !longitude) {
    return res.status(400).json({ error: 'Missing coordinates' });
  }
  
  const results = await busStopService.getBusStopsNearLocation(
    parseFloat(latitude), 
    parseFloat(longitude), 
    parseFloat(radiusKm)
  );
  
  res.json(results);
});

// GET /bus-stops/district/:district
router.get('/district/:district', async (req, res) => {
  const { district } = req.params;
  
  const results = await busStopService.getBusStopsByDistrict(district);
  res.json(results);
});

module.exports = router;
```

### Spring Boot vs Express.js 비교

| Spring Boot | Express.js | 설명 |
|------------|------------|------|
| `@GetMapping("/path")` | `router.get('/path', ...)` | GET 요청 처리 |
| `@RequestParam` | `req.query.param` | 쿼리 파라미터 |
| `@PathVariable` | `req.params.param` | URL 경로 변수 |
| `@RequestBody` | `req.body` | POST 요청 바디 |
| `ResponseEntity.ok()` | `res.json()` | JSON 응답 |
| `ResponseEntity.badRequest()` | `res.status(400)` | 에러 응답 |

---

## ⚙️ 설정 및 구성 분석

### application.yml - 애플리케이션 설정
```yaml
spring:
  application:
    name: bustrackr-backend  # 애플리케이션 이름
  profiles:
    active: local  # 현재 활성 프로필 (개발/운영 환경 구분)
    
  # 데이터베이스 설정
  datasource:
    url: jdbc:h2:mem:testdb      # H2 인메모리 데이터베이스
    driver-class-name: org.h2.Driver
    username: sa
    password:
    
  # H2 데이터베이스 콘솔 활성화 (개발용)
  h2:
    console:
      enabled: true
      path: /h2-console
      
  # JPA/Hibernate 설정
  jpa:
    hibernate:
      ddl-auto: create-drop  # 앱 시작시 테이블 생성, 종료시 삭제
    show-sql: true          # SQL 쿼리 로그 출력
    properties:
      hibernate:
        format_sql: true    # SQL 쿼리 포맷팅
        
  # Redis 설정 (캐싱용)
  redis:
    host: localhost
    port: 6379
    timeout: 2000ms
    
# 서버 설정
server:
  port: 8080              # 서버 포트
  servlet:
    context-path: /api    # 기본 경로 (모든 API가 /api로 시작)

# 로그 레벨 설정
logging:
  level:
    com.bustrackr: DEBUG           # 우리 패키지는 DEBUG 레벨
    org.springframework.web: DEBUG  # Spring 웹 로그도 DEBUG
    
# 커스텀 설정 (서울시 버스 API)
bus:
  api:
    base-url: http://ws.bus.go.kr/api/rest
    service-key: TEST_SERVICE_KEY_PLACEHOLDER  # 실제로는 환경변수로 관리
    timeout: 5000
    
# Actuator 설정 (모니터링용)
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics  # 노출할 엔드포인트
```

**JavaScript (.env + config.js) 비교:**
```javascript
// .env 파일
NODE_ENV=development
PORT=8080
DB_URL=sqlite://memory
REDIS_HOST=localhost
REDIS_PORT=6379
BUS_API_URL=http://ws.bus.go.kr/api/rest
BUS_API_KEY=TEST_SERVICE_KEY_PLACEHOLDER

// config.js
module.exports = {
  server: {
    port: process.env.PORT || 8080,
    contextPath: '/api'
  },
  database: {
    url: process.env.DB_URL,
    options: {
      logging: process.env.NODE_ENV === 'development'
    }
  },
  redis: {
    host: process.env.REDIS_HOST,
    port: process.env.REDIS_PORT,
    timeout: 2000
  },
  busApi: {
    baseUrl: process.env.BUS_API_URL,
    serviceKey: process.env.BUS_API_KEY,
    timeout: 5000
  }
};
```

### BusApiProperties.java - 설정을 Java 객체로 매핑
```java
@Component  // Spring이 관리하는 컴포넌트
@ConfigurationProperties(prefix = "bus.api")  // application.yml의 bus.api 하위 설정 매핑
public class BusApiProperties {
    
    private String baseUrl;    // bus.api.base-url과 매핑
    private String serviceKey; // bus.api.service-key와 매핑
    private int timeout = 5000; // bus.api.timeout과 매핑 (기본값 5000)
    
    // Getter/Setter (Spring이 설정 값을 주입할 때 사용)
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    
    public String getServiceKey() { return serviceKey; }
    public void setServiceKey(String serviceKey) { this.serviceKey = serviceKey; }
    
    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
}
```

이렇게 하면 다른 클래스에서 `@Autowired BusApiProperties`로 설정을 주입받아 사용할 수 있습니다.

---

## 🧪 테스트 코드 분석

### Repository 테스트 - BusStopRepositoryTest.java
```java
@DataJpaTest  // JPA 관련 테스트만 로드 (빠른 테스트)
class BusStopRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;  // 테스트용 JPA 매니저
    
    @Autowired
    private BusStopRepository busStopRepository;  // 테스트할 Repository
    
    @BeforeEach  // 각 테스트 실행 전에 실행 (JavaScript의 beforeEach)
    void setUp() {
        // 테스트 데이터 준비
        BusStop testStop = new BusStop("STOP001", "강남역", 37.4981, 127.0276);
        testStop.setDistrict("강남구");
        
        entityManager.persistAndFlush(testStop);  // 데이터베이스에 저장
    }
    
    @Test  // 테스트 메서드 (JavaScript의 test() 또는 it())
    void findById_shouldReturnBusStop_whenExists() {
        // Given (준비) - setUp()에서 데이터 준비됨
        
        // When (실행)
        Optional<BusStop> found = busStopRepository.findById("STOP001");
        
        // Then (검증) - assertThat은 JavaScript의 expect()와 비슷
        assertThat(found).isPresent();  // Optional이 값을 가지고 있는지
        assertThat(found.get().getStopName()).isEqualTo("강남역");
        assertThat(found.get().getDistrict()).isEqualTo("강남구");
    }
    
    @Test
    void findByStopNameContaining_shouldReturnMatchingStops() {
        // When
        List<BusStop> found = busStopRepository.findByStopNameContaining("강남");
        
        // Then
        assertThat(found).hasSize(1);  // 크기가 1인지
        assertThat(found.get(0).getStopName()).isEqualTo("강남역");
    }
}
```

**Jest 테스트 비교:**
```javascript
describe('BusStopRepository', () => {
  let repository;
  
  beforeEach(async () => {
    // 테스트 데이터 준비
    const testStop = await BusStop.create({
      stopId: 'STOP001',
      stopName: '강남역',
      latitude: 37.4981,
      longitude: 127.0276,
      district: '강남구'
    });
  });
  
  test('findById should return bus stop when exists', async () => {
    // When
    const found = await repository.findById('STOP001');
    
    // Then
    expect(found).toBeTruthy();
    expect(found.stopName).toBe('강남역');
    expect(found.district).toBe('강남구');
  });
  
  test('findByStopNameContaining should return matching stops', async () => {
    // When
    const found = await repository.findByStopNameContaining('강남');
    
    // Then
    expect(found).toHaveLength(1);
    expect(found[0].stopName).toBe('강남역');
  });
});
```

### Service 테스트 - BusStopServiceTest.java (Mock 사용)
```java
@ExtendWith(MockitoExtension.class)  // Mockito 확장 사용
class BusStopServiceTest {

    @Mock  // 가짜 객체 생성 (JavaScript의 jest.mock()과 비슷)
    private BusStopRepository busStopRepository;
    
    @InjectMocks  // Mock을 주입받는 실제 테스트 대상
    private BusStopService busStopService;
    
    @Test
    void searchBusStops_shouldReturnMatchingStops_whenKeywordExists() {
        // Given - Mock 동작 정의
        BusStop testStop = new BusStop("STOP001", "강남역", 37.4981, 127.0276);
        when(busStopRepository.findByKeyword("강남"))  // 이 메서드가 호출되면
            .thenReturn(Arrays.asList(testStop));      // 이 값을 반환하라
        
        // When
        List<BusStopSearchResponse> result = busStopService.searchBusStops("강남");
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStopName()).isEqualTo("강남역");
        
        verify(busStopRepository).findByKeyword("강남");  // 메서드가 호출되었는지 검증
    }
}
```

**Jest Mock 비교:**
```javascript
describe('BusStopService', () => {
  let service;
  let mockRepository;
  
  beforeEach(() => {
    mockRepository = {
      findByKeyword: jest.fn()
    };
    service = new BusStopService(mockRepository);
  });
  
  test('searchBusStops should return matching stops when keyword exists', async () => {
    // Given - Mock 설정
    const testStop = { stopId: 'STOP001', stopName: '강남역' };
    mockRepository.findByKeyword.mockResolvedValue([testStop]);
    
    // When
    const result = await service.searchBusStops('강남');
    
    // Then
    expect(result).toHaveLength(1);
    expect(result[0].stopName).toBe('강남역');
    expect(mockRepository.findByKeyword).toHaveBeenCalledWith('강남');
  });
});
```

### Controller 테스트 - Web Layer 테스트
```java
@WebMvcTest(BusStopController.class)  // Web 계층만 테스트
class BusStopControllerTest {

    @Autowired
    private MockMvc mockMvc;  // HTTP 요청을 시뮬레이션하는 도구
    
    @MockBean  // Spring Context에 Mock Bean 등록
    private BusStopService busStopService;
    
    @Test
    void searchBusStops_withKeyword_shouldReturnOk() throws Exception {
        // Given
        BusStopSearchResponse response = new BusStopSearchResponse();
        response.setStopId("STOP001");
        response.setStopName("강남역");
        
        when(busStopService.searchBusStops("강남"))
            .thenReturn(Arrays.asList(response));
        
        // When & Then
        mockMvc.perform(get("/bus-stops/search")  // GET 요청 실행
                .param("keyword", "강남"))         // 쿼리 파라미터 추가
            .andExpect(status().isOk())            // HTTP 200 OK 예상
            .andExpect(content().contentType("application/json"))  // JSON 응답 예상
            .andExpect(jsonPath("$").isArray())    // 배열인지 확인
            .andExpect(jsonPath("$[0].stopId").value("STOP001"))    // JSON 내용 확인
            .andExpect(jsonPath("$[0].stopName").value("강남역"));
    }
}
```

**Supertest (Node.js) 비교:**
```javascript
const request = require('supertest');
const app = require('../app');

describe('BusStopController', () => {
  test('GET /bus-stops/search with keyword should return OK', async () => {
    // Given - Mock 서비스 설정
    const mockResponse = [{ stopId: 'STOP001', stopName: '강남역' }];
    jest.spyOn(busStopService, 'searchBusStops').mockResolvedValue(mockResponse);
    
    // When & Then
    const response = await request(app)
      .get('/bus-stops/search')
      .query({ keyword: '강남' })
      .expect(200)
      .expect('Content-Type', /json/);
    
    expect(response.body).toHaveLength(1);
    expect(response.body[0].stopId).toBe('STOP001');
    expect(response.body[0].stopName).toBe('강남역');
  });
});
```

---

## ⚡ 스케줄링과 비동기 처리

### BusDataScheduler.java - 주기적 작업
```java
@Component  // Spring 컴포넌트
public class BusDataScheduler {
    
    @Autowired
    private BusDataSyncService busDataSyncService;
    
    // 30초마다 실행 (cron job과 비슷)
    @Scheduled(fixedRate = 30000, initialDelay = 10000)
    public void updateBusLocations() {
        logger.debug("Starting scheduled bus location update");
        
        try {
            List<BusRoute> routes = busRouteRepository.findAll();
            
            // 비동기 병렬 처리 (JavaScript의 Promise.all과 비슷)
            CompletableFuture<?>[] futures = routes.stream()
                .map(route -> CompletableFuture.runAsync(() -> {  // 각 노선을 별도 스레드에서 처리
                    try {
                        // 데이터 동기화
                        busDataSyncService.syncBusLocations(route.getRouteId());
                        
                        // WebSocket으로 실시간 전송
                        var locations = busLocationService.getBusLocationsByRoute(route.getRouteId());
                        if (!locations.isEmpty()) {
                            webSocketNotificationService.broadcastBusLocations(
                                route.getRouteId(), locations);
                        }
                    } catch (Exception e) {
                        logger.error("Error updating locations for route: " + route.getRouteId(), e);
                    }
                }))
                .toArray(CompletableFuture[]::new);
                
            CompletableFuture.allOf(futures).join();  // 모든 작업 완료까지 대기
            
        } catch (Exception e) {
            logger.error("Error in scheduled bus location update", e);
        }
    }
}
```

**JavaScript (Node.js cron + Promise.all) 비교:**
```javascript
const cron = require('node-cron');

// 30초마다 실행
cron.schedule('*/30 * * * * *', async () => {
  console.log('Starting scheduled bus location update');
  
  try {
    const routes = await busRouteRepository.findAll();
    
    // 병렬 처리 (Java의 CompletableFuture와 같음)
    const promises = routes.map(async route => {
      try {
        // 데이터 동기화
        await busDataSyncService.syncBusLocations(route.routeId);
        
        // WebSocket으로 실시간 전송
        const locations = await busLocationService.getBusLocationsByRoute(route.routeId);
        if (locations.length > 0) {
          webSocketNotificationService.broadcastBusLocations(route.routeId, locations);
        }
      } catch (error) {
        console.error(`Error updating locations for route: ${route.routeId}`, error);
      }
    });
    
    await Promise.all(promises);  // 모든 Promise 완료까지 대기
    
  } catch (error) {
    console.error('Error in scheduled bus location update', error);
  }
});
```

---

## 🔗 WebSocket 실시간 통신

### WebSocketConfig.java - WebSocket 설정
```java
@Configuration
@EnableWebSocketMessageBroker  // WebSocket 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");           // 클라이언트가 구독할 prefix
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 보낼 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")                    // WebSocket 연결 엔드포인트
            .setAllowedOrigins("http://localhost:3002") // CORS 허용
            .withSockJS();                             // SockJS 지원
    }
}
```

**Socket.IO (Node.js) 비교:**
```javascript
const io = require('socket.io')(server, {
  cors: {
    origin: "http://localhost:3002",
    methods: ["GET", "POST"]
  }
});

// WebSocket 연결 처리
io.on('connection', (socket) => {
  console.log('Client connected');
  
  // 클라이언트가 특정 노선 구독
  socket.on('subscribe-route', (routeId) => {
    socket.join(`route-${routeId}`);
  });
  
  // 클라이언트가 특정 정류장 구독
  socket.on('subscribe-stop', (stopId) => {
    socket.join(`stop-${stopId}`);
  });
});

// 실시간 데이터 브로드캐스트
function broadcastBusLocations(routeId, locations) {
  io.to(`route-${routeId}`).emit('bus-locations', locations);
}
```

### WebSocketNotificationService.java - 실시간 알림
```java
@Service
public class WebSocketNotificationService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;  // WebSocket 메시지 전송 도구
    
    public void broadcastBusLocations(String routeId, List<BusLocationResponse> locations) {
        try {
            String destination = "/topic/bus-locations/" + routeId;  // 목적지 주소
            messagingTemplate.convertAndSend(destination, locations); // 메시지 전송
            
            logger.debug("Broadcasted {} bus locations for route {} to {}", 
                locations.size(), routeId, destination);
                
        } catch (Exception e) {
            logger.error("Error broadcasting bus locations for route: " + routeId, e);
        }
    }
}
```

---

## 🔄 JavaScript vs Java 주요 차이점 비교표

| 측면 | JavaScript | Java | 설명 |
|------|------------|------|------|
| **타입 시스템** | 동적 타입 | 정적 타입 | Java는 컴파일 시 타입 체크 |
| **변수 선언** | `let`, `const` | 타입명 + 변수명 | `String name` vs `let name` |
| **null 처리** | `null`, `undefined` | `null`, `Optional<T>` | Java는 Optional로 null 안전성 |
| **배열/리스트** | `Array` | `List<T>`, `Array[]` | Java는 제네릭으로 타입 안전성 |
| **함수** | `function`, 화살표 함수 | `public/private 메서드` | Java는 클래스 내부에서만 정의 |
| **비동기 처리** | `Promise`, `async/await` | `CompletableFuture` | Java도 비동기 지원 |
| **모듈 시스템** | `import/export` | `package/import` | Java는 패키지 기반 |
| **의존성 관리** | 수동 주입 | Spring DI | Java Spring은 자동 주입 |
| **데이터베이스** | ORM 라이브러리 | JPA/Hibernate | Java는 표준 ORM |
| **테스트** | Jest, Mocha | JUnit, Mockito | 비슷한 개념 |

---

## 🎓 학습 순서 추천

1. **Java 기본 문법** (이미 JavaScript를 알고 있다면 빠르게)
   - 클래스와 객체
   - 제네릭 (`List<String>`)
   - Optional과 null 처리

2. **Spring Framework 핵심 개념**
   - 의존성 주입 (DI)
   - 어노테이션 시스템
   - Bean과 Component

3. **Spring Boot 실용**
   - Auto Configuration
   - application.yml 설정
   - Web MVC 패턴

4. **JPA/Hibernate** (데이터베이스)
   - 엔티티 매핑
   - Repository 패턴
   - 쿼리 메서드

5. **테스트** (JavaScript와 매우 유사)
   - 단위 테스트
   - Mock 사용법
   - 통합 테스트

---

## 💡 실제 개발 팁

### 1. IDE 활용
- **IntelliJ IDEA** 사용 (VS Code의 Java 버전)
- 자동완성과 리팩토링 기능 적극 활용
- Lombok 플러그인으로 보일러플레이트 코드 줄이기

### 2. 디버깅
```java
// JavaScript console.log 대신
logger.debug("User data: {}", userData);  // SLF4J 로거 사용
System.out.println("Debug: " + message);  // 간단한 디버깅용
```

### 3. 예외 처리
```java
// JavaScript try-catch와 비슷하지만 더 구체적
try {
    busStopService.searchBusStops(keyword);
} catch (IllegalArgumentException e) {  // 특정 예외 타입 캐치
    logger.error("Invalid keyword: {}", keyword, e);
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
}
```

### 4. 컬렉션 활용
```java
// JavaScript Array methods와 비슷한 Stream API
List<String> names = busStops.stream()
    .filter(stop -> stop.getDistrict().equals("강남구"))
    .map(BusStop::getStopName)
    .sorted()
    .collect(Collectors.toList());
```

---

---

## 🎨 Frontend 프로젝트 구조 및 분석

### 프론트엔드 기술 스택
```javascript
// package.json 주요 의존성
{
  "dependencies": {
    "react": "^19.1.1",           // UI 라이브러리
    "react-router-dom": "^7.8.0", // 라우팅
    "axios": "^1.11.0",           // HTTP 클라이언트 (백엔드 API 통신)
    "leaflet": "^1.9.4",          // 지도 라이브러리
    "react-leaflet": "^5.0.0",    // React용 Leaflet 래퍼
    "zustand": "^5.0.7",          // 상태 관리
    "@tailwindcss/postcss": "^4.1.11" // CSS 프레임워크
  },
  "devDependencies": {
    "vite": "^7.1.2",             // 빌드 도구 (Webpack 대신)
    "typescript": "~5.8.3"        // TypeScript 지원
  }
}
```

### 프론트엔드 폴더 구조
```
frontend/
├── src/
│   ├── App.tsx                    # 메인 애플리케이션 (라우터 설정)
│   ├── main.tsx                   # 애플리케이션 진입점
│   ├── types/
│   │   └── index.ts              # TypeScript 타입 정의 (백엔드 DTO와 매핑)
│   ├── pages/
│   │   ├── OnboardingPage.tsx    # 랜딩 페이지
│   │   ├── SearchPage.tsx        # 검색 필터 페이지
│   │   └── MapPage.tsx          # 실시간 지도 페이지
│   ├── components/              # 재사용 가능한 컴포넌트들
│   ├── store/                   # Zustand 상태 관리
│   ├── hooks/                   # 커스텀 React Hooks
│   └── utils/                   # 유틸리티 함수들
├── postcss.config.js           # PostCSS 설정 (Tailwind 처리)
├── tailwind.config.js          # Tailwind CSS 설정
└── package.json                # 의존성 및 스크립트 정의
```

---

## 📱 React 컴포넌트 분석

### App.tsx - 메인 애플리케이션 라우터
```typescript
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { OnboardingPage } from './pages/OnboardingPage';
import { SearchPage } from './pages/SearchPage';
import { MapPage } from './pages/MapPage';

function App() {
  return (
    <Router>  {/* React Router로 페이지 라우팅 관리 */}
      <div className="app">
        <Routes>
          <Route path="/" element={<OnboardingPage />} />      {/* 홈: 온보딩 */}
          <Route path="/search" element={<SearchPage />} />    {/* 검색 필터 */}
          <Route path="/map" element={<MapPage />} />          {/* 실시간 지도 */}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
```

**Java Spring Boot Controller와 비교:**
```java
// Java - URL 매핑이 어노테이션으로
@RestController
@RequestMapping("/api")
public class BusController {
    @GetMapping("/")              // GET /api/
    @GetMapping("/search")        // GET /api/search  
    @GetMapping("/map")          // GET /api/map
}
```

```javascript
// React Router - 컴포넌트 기반 라우팅
<Route path="/" element={<HomePage />} />
<Route path="/search" element={<SearchPage />} />
<Route path="/map" element={<MapPage />} />
```

### TypeScript 타입 정의 - types/index.ts
```typescript
// 백엔드 Java Entity와 1:1 매핑되는 타입들
export interface BusRoute {
  routeId: string;    // Java: String routeId
  routeName: string;  // Java: String routeName
  routeType: 'express' | 'trunk' | 'branch' | 'circular' | 'wide';  // Java: Enum
  direction: string;  // Java: String direction
}

export interface BusStop {
  stopId: string;     // Java: String stopId (Primary Key)
  stopName: string;   // Java: String stopName
  latitude: number;   // Java: Double latitude
  longitude: number;  // Java: Double longitude  
  direction: string;  // Java: String direction
}

export interface BusLocation {
  busId: string;              // Java: String busId
  routeId: string;            // Java: String routeId
  latitude: number;           // Java: Double latitude
  longitude: number;          // Java: Double longitude
  speed: number;              // Java: Double speed
  congestion: 'low' | 'medium' | 'high';  // Java: CongestionLevel enum
  nextStopId: string;         // Java: String nextStopId
  estimatedArrival: number;   // Java: Integer estimatedArrival
}
```

**Java Entity와의 매핑:**
```java
// Java Entity
@Entity
public class BusLocation {
    private String busId;
    private String routeId;
    private Double latitude;
    private Double longitude;
    private Double speed;
    
    @Enumerated(EnumType.STRING)
    private CongestionLevel congestion;  // LOW, MEDIUM, HIGH
    
    private String nextStopId;
    private Integer estimatedArrival;
}
```

### OnboardingPage.tsx - 랜딩 페이지
```typescript
import { useNavigate } from 'react-router-dom';  // 프로그래매틱 네비게이션

export function OnboardingPage() {
  const navigate = useNavigate();  // React Router 훅

  const handleGetStarted = () => {
    navigate('/search');  // /search 페이지로 이동
  };

  return (
    <div className="page">
      <div className="container">
        {/* 앱 소개 UI */}
        <h1>버스가 언제 올까요?</h1>
        <p>실시간으로 확인하는 버스 정보 서비스</p>
        
        {/* 기능 소개 카드들 */}
        <div className="card">
          <div>🚌 실시간 위치 확인</div>
          <p>버스의 현재 위치를 실시간으로 추적</p>
        </div>
        
        <div className="card">
          <div>⏰ 도착 시간 예측</div>
          <p>정확한 도착 시간과 남은 정류장 수</p>
        </div>
        
        <div className="card">
          <div>📊 혼잡도 정보</div>
          <p>버스 내부 혼잡도를 미리 확인</p>
        </div>

        <button onClick={handleGetStarted}>시작하기</button>
      </div>
    </div>
  );
}
```

### SearchPage.tsx - 검색 필터 페이지
```typescript
import { useState } from 'react';  // React 상태 관리 훅
import { useNavigate } from 'react-router-dom';

type FilterType = 'card' | 'subway' | 'express' | 'general' | 'electric';

export function SearchPage() {
  const navigate = useNavigate();
  
  // React 상태 관리 (Java의 클래스 필드와 비교)
  const [selectedFilters, setSelectedFilters] = useState<FilterType[]>([]);
  const [searchQuery, setSearchQuery] = useState('');

  // 필터 토글 함수 (Java의 메서드와 비교)
  const toggleFilter = (filterId: FilterType) => {
    setSelectedFilters(prev => 
      prev.includes(filterId) 
        ? prev.filter(id => id !== filterId)  // 제거
        : [...prev, filterId]                 // 추가
    );
  };

  const handleNext = () => {
    navigate('/map');  // 다음 페이지로 이동
  };

  return (
    <div className="page">
      {/* 헤더 */}
      <div>
        <button onClick={() => navigate(-1)}>←</button>  {/* 뒤로가기 */}
        <h1>검색 필터 설정</h1>
      </div>

      {/* 검색 입력 */}
      <div className="input-group">
        <label>지역</label>
        <input
          type="text"
          placeholder="목적지를 입력하세요"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}  {/* 실시간 입력 업데이트 */}
        />
      </div>

      {/* 거리 범위 슬라이더 */}
      <div>
        <label>거리 범위</label>
        <div className="range-slider">
          {/* 슬라이더 UI */}
        </div>
      </div>

      {/* 필터 아이콘 그리드 */}
      <div className="icon-grid">
        {filters.map(filter => (
          <div
            key={filter.id}
            className={`icon-item ${selectedFilters.includes(filter.id) ? 'active' : ''}`}
            onClick={() => toggleFilter(filter.id)}
          >
            {filter.icon} {filter.label}
          </div>
        ))}
      </div>

      <button onClick={handleNext}>검색하기</button>
    </div>
  );
}
```

**Java Controller vs React State 비교:**
```java
// Java - 서버 상태 관리
@RestController
public class SearchController {
    private List<String> selectedFilters = new ArrayList<>();  // 상태
    private String searchQuery = "";  // 상태
    
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        // 비즈니스 로직 처리
        return ResponseEntity.ok(results);
    }
}
```

```typescript
// React - 클라이언트 상태 관리
export function SearchPage() {
  const [selectedFilters, setSelectedFilters] = useState<string[]>([]);  // 상태
  const [searchQuery, setSearchQuery] = useState('');  // 상태
  
  const handleSearch = async () => {
    // API 호출로 서버에 요청
    const response = await axios.post('/api/search', {
      filters: selectedFilters,
      query: searchQuery
    });
  };
}
```

### MapPage.tsx - 실시간 지도 페이지 (가장 복잡한 컴포넌트)
```typescript
import { useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';  // Leaflet 지도
import { Icon } from 'leaflet';

// 버스와 정류장 타입 정의 (백엔드와 매핑)
interface BusInfo {
  id: string;
  route: string;
  position: [number, number];      // [latitude, longitude]
  congestion: 'low' | 'medium' | 'high';
  nextStop: string;
  eta: number;  // 도착 예상 시간 (분)
}

interface BusStopInfo {
  id: string;
  name: string;
  position: [number, number];
  routes: string[];  // 경유 노선들
}

export function MapPage() {
  // 로컬 상태들 (실제로는 백엔드 API나 WebSocket에서 받아올 데이터)
  const [buses] = useState<BusInfo[]>([
    {
      id: '1',
      route: '472번',
      position: [37.5665, 126.9780],  // 서울 시청 근처
      congestion: 'medium',
      nextStop: '시청역',
      eta: 3
    }
  ]);

  const [busStops] = useState<BusStopInfo[]>([
    {
      id: '1',
      name: '강남구청',
      position: [37.5172, 127.0473],
      routes: ['472번', '143번', '301번']
    }
  ]);

  // 혼잡도에 따른 색상 매핑 (Java의 switch문과 비슷)
  const getCongestionColor = (congestion: string) => {
    switch (congestion) {
      case 'low': return '#00b894';     // 여유 - 초록
      case 'medium': return '#fdcb6e';  // 보통 - 노랑
      case 'high': return '#e17055';    // 혼잡 - 빨강
      default: return '#636e72';
    }
  };

  return (
    <div className="relative h-screen">
      {/* 상단 검색바 */}
      <div className="search-bar">
        <input placeholder="노선, 지하철 역명 검색" />
      </div>

      {/* Leaflet 지도 */}
      <div className="h-screen">
        <MapContainer
          center={[37.5665, 126.9780]}  // 서울 중심
          zoom={13}
          style={{ height: '100%', width: '100%' }}
        >
          <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
          
          {/* 실시간 버스 위치 마커들 */}
          {buses.map((bus) => (
            <Marker key={bus.id} position={bus.position} icon={busIcon}>
              <Popup>
                <div>
                  <h3>{bus.route}</h3>
                  <div style={{ backgroundColor: getCongestionColor(bus.congestion) }}>
                    {congestion === 'low' ? '여유' : congestion === 'medium' ? '보통' : '혼잡'}
                  </div>
                  <p>다음 정류장: {bus.nextStop}</p>
                  <p>{bus.eta}분 후 도착</p>
                </div>
              </Popup>
            </Marker>
          ))}

          {/* 버스 정류장 마커들 */}
          {busStops.map((stop) => (
            <Marker key={stop.id} position={stop.position} icon={busStopIcon}>
              <Popup>
                <div>
                  <h3>{stop.name}</h3>
                  <div>운행 노선:</div>
                  {stop.routes.map((route, index) => (
                    <span key={index}>{route}</span>
                  ))}
                </div>
              </Popup>
            </Marker>
          ))}
        </MapContainer>
      </div>
    </div>
  );
}
```

---

## 🔄 전체 애플리케이션 데이터 흐름 (Flow) 분석

### 1. 사용자 여정 (User Journey)
```
1. 랜딩 페이지 (/) 
   → "시작하기" 클릭
   
2. 검색 페이지 (/search)
   → 필터 선택, 지역 입력
   → "검색하기" 클릭
   
3. 지도 페이지 (/map)
   → 실시간 버스/정류장 정보 표시
   → WebSocket으로 실시간 업데이트
```

### 2. 시스템 아키텍처 및 데이터 흐름

```
┌─────────────────┐    HTTP/WebSocket    ┌─────────────────┐
│   Frontend      │ ←──────────────────→ │    Backend      │
│   (React TS)    │                      │  (Spring Boot)  │
├─────────────────┤                      ├─────────────────┤
│ • React Router  │                      │ • REST API      │
│ • Zustand       │                      │ • WebSocket     │
│ • Leaflet Map   │                      │ • JPA/H2        │
│ • Axios HTTP    │                      │ • Redis Cache   │
└─────────────────┘                      └─────────────────┘
                                                   │
                                         ┌─────────▼─────────┐
                                         │  External APIs    │
                                         │ (Seoul Bus API)   │
                                         └───────────────────┘
```

### 3. 상세 데이터 흐름

#### A) 정적 데이터 요청 흐름 (HTTP REST API)
```typescript
// Frontend: 사용자가 검색 버튼 클릭
const handleSearch = async () => {
  try {
    // 1. Axios로 백엔드 API 호출
    const response = await axios.get('/api/bus-stops/search', {
      params: { keyword: '강남' }
    });
    
    // 2. 응답 데이터를 상태에 저장
    setBusStops(response.data);
  } catch (error) {
    console.error('검색 실패:', error);
  }
};
```

```java
// Backend: Controller에서 요청 처리
@RestController
@RequestMapping("/api/bus-stops")
public class BusStopController {
    
    @Autowired
    private BusStopService busStopService;
    
    @GetMapping("/search")  // 1. HTTP GET 요청 수신
    public ResponseEntity<List<BusStopSearchResponse>> searchBusStops(
            @RequestParam String keyword) {
        
        // 2. Service 계층으로 비즈니스 로직 처리 위임
        List<BusStopSearchResponse> results = busStopService.searchBusStops(keyword);
        
        // 3. JSON 형태로 응답 반환
        return ResponseEntity.ok(results);
    }
}
```

```java
// Backend: Service에서 비즈니스 로직
@Service
public class BusStopService {
    
    @Autowired
    private BusStopRepository repository;
    
    public List<BusStopSearchResponse> searchBusStops(String keyword) {
        // 1. Repository에서 데이터 조회 (SQL 실행)
        List<BusStop> busStops = repository.findByKeyword(keyword);
        
        // 2. Entity를 DTO로 변환
        return busStops.stream()
            .map(BusStopSearchResponse::new)
            .collect(Collectors.toList());
    }
}
```

```java
// Backend: Repository에서 데이터베이스 쿼리
@Repository
public interface BusStopRepository extends JpaRepository<BusStop, String> {
    
    @Query("SELECT s FROM BusStop s WHERE s.stopName LIKE %:keyword%")
    List<BusStop> findByKeyword(@Param("keyword") String keyword);
    // JPA가 자동으로 SQL 생성: 
    // SELECT * FROM bus_stops WHERE stop_name LIKE '%강남%'
}
```

#### B) 실시간 데이터 흐름 (WebSocket)
```java
// Backend: 스케줄러가 주기적으로 데이터 수집
@Component
public class BusDataScheduler {
    
    @Scheduled(fixedRate = 30000)  // 30초마다 실행
    public void updateBusLocations() {
        // 1. 서울 버스 API에서 실시간 데이터 가져오기
        List<BusLocationResponse> locations = seoulBusApiService.getBusLocations();
        
        // 2. 데이터베이스에 저장
        busLocationService.saveAllBusLocations(locations);
        
        // 3. WebSocket으로 모든 연결된 클라이언트에게 브로드캐스트
        webSocketNotificationService.broadcastBusLocations(locations);
    }
}
```

```java
// Backend: WebSocket 알림 서비스
@Service
public class WebSocketNotificationService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public void broadcastBusLocations(String routeId, List<BusLocationResponse> locations) {
        // WebSocket "/topic/bus-locations/{routeId}" 채널로 메시지 전송
        messagingTemplate.convertAndSend("/topic/bus-locations/" + routeId, locations);
    }
}
```

```typescript
// Frontend: WebSocket 연결 및 실시간 데이터 수신
import { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

export function MapPage() {
  const [busLocations, setBusLocations] = useState<BusLocation[]>([]);
  
  useEffect(() => {
    // 1. WebSocket 연결 설정
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);
    
    stompClient.connect({}, () => {
      console.log('WebSocket 연결됨');
      
      // 2. 특정 노선의 실시간 위치 구독
      stompClient.subscribe('/topic/bus-locations/ROUTE001', (message) => {
        // 3. 실시간 데이터 수신 및 상태 업데이트
        const locations = JSON.parse(message.body);
        setBusLocations(locations);
        
        // 4. 지도에 실시간으로 버스 위치 업데이트
        console.log('실시간 버스 위치 업데이트:', locations);
      });
    });
    
    // 컴포넌트 언마운트시 연결 해제
    return () => {
      stompClient.disconnect();
    };
  }, []);

  return (
    <div>
      <MapContainer>
        {/* 실시간 업데이트되는 버스 위치들 */}
        {busLocations.map(location => (
          <Marker key={location.busId} position={[location.latitude, location.longitude]}>
            <Popup>
              버스 ID: {location.busId}<br/>
              속도: {location.speed}km/h<br/>
              혼잡도: {location.congestion}
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
}
```

### 4. 외부 API 통합 흐름
```java
// Backend: 서울시 버스 API 클라이언트
@Service
public class SeoulBusApiService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${bus.api.service-key}")
    private String serviceKey;
    
    public List<BusLocationApiResponse> getBusLocationsByRoute(String routeId) {
        // 1. 서울시 API URL 구성
        String url = String.format(
            "http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid?serviceKey=%s&busRouteId=%s&resultType=json",
            serviceKey, routeId
        );
        
        try {
            // 2. 외부 API 호출
            BusApiResponse response = restTemplate.getForObject(url, BusApiResponse.class);
            
            // 3. API 응답을 내부 도메인 객체로 변환
            return response.getMsgBody().getItemList().stream()
                .map(this::convertToInternalFormat)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            logger.error("서울시 버스 API 호출 실패: " + routeId, e);
            return Collections.emptyList();
        }
    }
    
    private BusLocationResponse convertToInternalFormat(BusLocationApiResponse apiData) {
        BusLocationResponse location = new BusLocationResponse();
        location.setBusId(apiData.getVehId());
        location.setRouteId(apiData.getBusRouteId());
        location.setLatitude(Double.parseDouble(apiData.getGpsY()));
        location.setLongitude(Double.parseDouble(apiData.getGpsX()));
        // ... 기타 필드 매핑
        return location;
    }
}
```

### 5. 에러 처리 및 로딩 상태 관리
```typescript
// Frontend: 에러 처리와 로딩 상태
export function SearchPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [busStops, setBusStops] = useState<BusStop[]>([]);

  const handleSearch = async (keyword: string) => {
    setIsLoading(true);   // 로딩 시작
    setError(null);       // 이전 에러 초기화
    
    try {
      const response = await axios.get('/api/bus-stops/search', {
        params: { keyword },
        timeout: 10000      // 10초 타임아웃
      });
      
      setBusStops(response.data);
      
    } catch (error) {
      if (error.code === 'ECONNABORTED') {
        setError('요청 시간이 초과되었습니다. 다시 시도해주세요.');
      } else if (error.response?.status === 404) {
        setError('검색 결과가 없습니다.');
      } else {
        setError('서버 오류가 발생했습니다.');
      }
    } finally {
      setIsLoading(false);  // 로딩 종료
    }
  };

  return (
    <div>
      {isLoading && <div>검색 중...</div>}
      {error && <div className="error">{error}</div>}
      {busStops.length > 0 && (
        <div>
          {busStops.map(stop => (
            <div key={stop.stopId}>{stop.stopName}</div>
          ))}
        </div>
      )}
    </div>
  );
}
```

```java
// Backend: 글로벌 에러 핸들러
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception e) {
        logger.error("Unexpected error occurred", e);
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

### 6. 성능 최적화 패턴

#### Frontend 최적화:
```typescript
// React 최적화: useMemo, useCallback 사용
import { useMemo, useCallback } from 'react';

export function MapPage() {
  const [buses, setBuses] = useState<BusInfo[]>([]);
  
  // 혼잡한 버스만 필터링 (계산 비용이 높은 작업을 메모이제이션)
  const congestedBuses = useMemo(() => 
    buses.filter(bus => bus.congestion === 'high'),
    [buses]
  );
  
  // 버스 클릭 핸들러 (리렌더링시 함수 재생성 방지)
  const handleBusClick = useCallback((busId: string) => {
    console.log('버스 클릭:', busId);
  }, []);

  return (
    <div>
      {congestedBuses.map(bus => (
        <BusMarker 
          key={bus.id} 
          bus={bus} 
          onClick={handleBusClick}
        />
      ))}
    </div>
  );
}
```

#### Backend 최적화:
```java
// 캐싱을 통한 성능 최적화
@Service
public class BusStopService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Cacheable(value = "busStops", key = "#keyword")  // Redis 캐시 사용
    public List<BusStopSearchResponse> searchBusStops(String keyword) {
        // 캐시에 없을 때만 데이터베이스 조회
        List<BusStop> busStops = repository.findByKeyword(keyword);
        return busStops.stream()
            .map(BusStopSearchResponse::new)
            .collect(Collectors.toList());
    }
}
```

---

## 🚀 배포 및 빌드 프로세스

### Frontend 빌드 (Vite)
```bash
# 개발 서버 시작
npm run dev          # http://localhost:3002

# 프로덕션 빌드
npm run build        # TypeScript 컴파일 → Vite 빌드
npm run preview      # 빌드된 앱 미리보기
```

### Backend 빌드 (Gradle)
```bash
# 개발 서버 시작
./gradlew bootRun    # http://localhost:8080

# 프로덕션 빌드
./gradlew build      # JAR 파일 생성
java -jar build/libs/bustrackr-backend-0.0.1.jar
```

---

## 💡 JavaScript → Java 개발자를 위한 핵심 차이점

### 1. 타입 시스템
```javascript
// JavaScript: 런타임에 타입 에러
let busId = "BUS001";
busId = 123;          // OK, 타입 변경 가능
busId.toUpperCase();  // 런타임 에러!
```

```java
// Java: 컴파일 타임에 타입 체크
String busId = "BUS001";
// busId = 123;       // 컴파일 에러!
busId.toUpperCase();  // OK
```

### 2. 비동기 처리
```javascript
// JavaScript: Promise/async-await
const getBusInfo = async (busId) => {
  try {
    const response = await fetch(`/api/buses/${busId}`);
    return response.json();
  } catch (error) {
    console.error(error);
  }
};
```

```java
// Java: CompletableFuture
public CompletableFuture<BusInfo> getBusInfo(String busId) {
    return CompletableFuture.supplyAsync(() -> {
        try {
            return busService.findById(busId);
        } catch (Exception e) {
            logger.error("Error fetching bus info", e);
            throw new RuntimeException(e);
        }
    });
}
```

### 3. 의존성 관리
```javascript
// JavaScript: 수동 import/export
import { BusService } from './services/BusService';
import { ApiClient } from './api/ApiClient';

class BusController {
  constructor() {
    this.busService = new BusService(new ApiClient());
  }
}
```

```java
// Java: Spring DI 자동 주입
@RestController
public class BusController {
    
    @Autowired
    private BusService busService;  // Spring이 자동 주입
    
    // 생성자 주입 방식 (권장)
    public BusController(BusService busService) {
        this.busService = busService;
    }
}
```

---

## 🎯 실제 개발시 주의사항 및 팁

### 1. CORS 설정 (Frontend ↔ Backend 통신)
```java
// Backend: CORS 허용 설정
@CrossOrigin(origins = "http://localhost:3002")
@RestController
public class BusStopController {
    // API 엔드포인트들...
}
```

```typescript
// Frontend: Axios 기본 설정
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',  // 백엔드 서버
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  }
});
```

### 2. 환경별 설정 관리
```yaml
# Backend: application.yml
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:local}  # 환경변수로 제어
    
---
spring:
  profiles: local
  datasource:
    url: jdbc:h2:mem:testdb
    
---
spring:
  profiles: production  
  datasource:
    url: ${DATABASE_URL}
```

```javascript
// Frontend: 환경별 설정
// .env.development
VITE_API_URL=http://localhost:8080/api

// .env.production  
VITE_API_URL=https://api.bustrackr.com/api

// 사용
const apiUrl = import.meta.env.VITE_API_URL;
```

### 3. 실시간 통신 최적화
```java
// Backend: WebSocket 메시지 최적화
@Service
public class WebSocketNotificationService {
    
    // 특정 노선을 구독한 클라이언트에게만 전송
    public void broadcastBusLocations(String routeId, List<BusLocationResponse> locations) {
        messagingTemplate.convertAndSend("/topic/bus-locations/" + routeId, locations);
    }
    
    // 지역별 그룹 메시지 전송
    public void broadcastToDistrict(String district, Object message) {
        messagingTemplate.convertAndSend("/topic/district/" + district, message);
    }
}
```

```typescript
// Frontend: WebSocket 연결 관리
class WebSocketManager {
  private stompClient: any;
  private subscriptions: Map<string, any> = new Map();
  
  connect() {
    const socket = new SockJS('/ws');
    this.stompClient = Stomp.over(socket);
    
    this.stompClient.connect({}, () => {
      console.log('WebSocket 연결됨');
    });
  }
  
  // 노선별 구독
  subscribeToRoute(routeId: string, callback: (data: any) => void) {
    if (!this.subscriptions.has(routeId)) {
      const subscription = this.stompClient.subscribe(
        `/topic/bus-locations/${routeId}`,
        (message) => callback(JSON.parse(message.body))
      );
      this.subscriptions.set(routeId, subscription);
    }
  }
  
  // 구독 해제
  unsubscribeFromRoute(routeId: string) {
    const subscription = this.subscriptions.get(routeId);
    if (subscription) {
      subscription.unsubscribe();
      this.subscriptions.delete(routeId);
    }
  }
}
```

---

이 가이드를 통해 JavaScript 개발자도 Java/Spring Boot 코드를 이해하고, 전체 애플리케이션의 데이터 흐름과 Frontend-Backend 연동을 완벽히 파악할 수 있습니다! 🚀

추가로 궁금한 부분이 있다면 언제든 물어보세요!