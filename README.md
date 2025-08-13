# BusTrackr

실시간 버스 위치 및 도착 예정 시간을 확인할 수 있는 대시보드 애플리케이션입니다.

## 📋 프로젝트 개요

사용자가 노선/정류장을 선택하면 해당 버스의 현재 위치, 도착 예정 시간, 혼잡도를 실시간으로 확인할 수 있는 웹 애플리케이션입니다.

### 🎯 주요 기능
- 실시간 버스 위치 추적
- 도착 예정 시간 표시
- 버스 혼잡도 정보 (지원 노선에 한해)
- 대화형 지도 인터페이스
- PWA 지원으로 모바일 앱과 같은 경험

### 🔧 기술 스택

**백엔드**
- Java 17 + Spring Boot 3.x
- WebSocket/SSE를 통한 실시간 데이터 전송
- Spring Data JPA + H2/MySQL
- Redis (캐싱 및 세션 관리)
- 서울시 버스정보 시스템(BIS) OpenAPI 연동

**프론트엔드**
- React 18 + TypeScript
- Vite (빌드 도구)
- Zustand (상태 관리)
- Leaflet (지도)
- PWA 지원

**인프라**
- Docker & Docker Compose
- Nginx (리버스 프록시)

## 🚀 실행 방법

### 개발 환경

1. 저장소 클론
```bash
git clone https://github.com/your-username/BusTrackr.git
cd BusTrackr
```

2. 백엔드 실행
```bash
cd backend
./gradlew bootRun
```

3. 프론트엔드 실행
```bash
cd frontend
npm install
npm run dev
```

### Docker를 사용한 실행

```bash
docker-compose up -d
```

서비스 접근:
- 프론트엔드: http://localhost:3000
- 백엔드 API: http://localhost:8080/api
- H2 Console: http://localhost:8080/api/h2-console

## 📁 프로젝트 구조

```
BusTrackr/
├── backend/          # Spring Boot 백엔드
│   ├── src/main/java/com/bustrackr/
│   ├── src/main/resources/
│   └── build.gradle
├── frontend/         # React 프론트엔드
│   ├── src/
│   ├── public/
│   └── package.json
├── docker-compose.yml
└── README.md
```

## 🔑 환경 설정

서울시 버스 API 서비스 키를 `backend/src/main/resources/application.yml`에 설정해야 합니다:

```yaml
bus:
  api:
    service-key: YOUR_SERVICE_KEY_HERE
```

## 📄 라이선스

MIT License
