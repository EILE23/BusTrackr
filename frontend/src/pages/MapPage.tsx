import { useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Icon } from 'leaflet';
import 'leaflet/dist/leaflet.css';

// 커스텀 아이콘 생성
const busIcon = new Icon({
  iconUrl: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzIiIGhlaWdodD0iMzIiIHZpZXdCb3g9IjAgMCAzMiAzMiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMTYiIGN5PSIxNiIgcj0iMTYiIGZpbGw9IiM2NjdlZWEiLz4KPHN2ZyB4PSI4IiB5PSI4IiB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0id2hpdGUiPgo8cGF0aCBkPSJNNCAxNmMwIDEuMTEuODkgMiAyIDJoMWMwIDEuMTEuODkgMiAyIDJzMi0uODkgMi0yaDZjMCAxLjExLjg5IDIgMiAycy0yLS44OSAyLTJoMWMxLjExIDAgMi0uODkgMi0yVjZjMC0xLjExLS44OS0yLTItMkg2Yy0xLjExIDAtMiAuODktMiAydjEwek02IDhoMTJ2NUg2Vjh6Ii8+Cjwvc3ZnPgo8L3N2Zz4K',
  iconSize: [32, 32],
  iconAnchor: [16, 16],
  popupAnchor: [0, -16]
});

const busStopIcon = new Icon({
  iconUrl: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMTIiIGN5PSIxMiIgcj0iMTIiIGZpbGw9IiNmZDc5YTgiLz4KPHN2ZyB4PSI2IiB5PSI2IiB3aWR0aD0iMTIiIGhlaWdodD0iMTIiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0id2hpdGUiPgo8cGF0aCBkPSJNMTIgMmMtNC40IDAtOCAzLjYtOCA4IDAgNS40IDggMTIgOCAxMnM4LTYuNiA4LTEyYzAtNC40LTMuNi04LTgtOHptMCAxMWMtMS43IDAtMy0xLjMtMy0zczEuMy0zIDMtMyAzIDEuMyAzIDMtMS4zIDMtMyAzeiIvPgo8L3N2Zz4KPC9zdmc+Cg==',
  iconSize: [24, 24],
  iconAnchor: [12, 12],
  popupAnchor: [0, -12]
});

interface BusInfo {
  id: string;
  route: string;
  position: [number, number];
  congestion: 'low' | 'medium' | 'high';
  nextStop: string;
  eta: number;
}

interface BusStopInfo {
  id: string;
  name: string;
  position: [number, number];
  routes: string[];
}

export function MapPage() {
  const [selectedFilter, setSelectedFilter] = useState<string>('all');
  const [buses] = useState<BusInfo[]>([
    {
      id: '1',
      route: '472번',
      position: [37.5665, 126.9780],
      congestion: 'medium',
      nextStop: '시청역',
      eta: 3
    },
    {
      id: '2', 
      route: '143번',
      position: [37.5635, 126.9758],
      congestion: 'low',
      nextStop: '광화문',
      eta: 8
    }
  ]);

  const [busStops] = useState<BusStopInfo[]>([
    {
      id: '1',
      name: '강남구청',
      position: [37.5172, 127.0473],
      routes: ['472번', '143번', '301번']
    },
    {
      id: '2',
      name: '시청역',
      position: [37.5658, 126.9779],
      routes: ['472번', '505번']
    }
  ]);

  const bottomNavItems = [
    { id: 'megaphone', icon: '📣', label: '확성기', color: '#6C63FF' },
    { id: 'glasses', icon: '👓', label: '안경', color: '#4ECDC4' },
    { id: 'clock', icon: '⏰', label: '시계', color: '#45B7D1' },
    { id: 'shoe', icon: '👟', label: '신발', color: '#2C2C2C' },
    { id: 'headphone', icon: '🎧', label: '헤드폰', color: '#2C2C2C' },
    { id: 'headphone2', icon: '🎧', label: '헤드폰2', color: '#2C2C2C' }
  ];

  const getCongestionColor = (congestion: string) => {
    switch (congestion) {
      case 'low': return '#00b894';
      case 'medium': return '#fdcb6e';
      case 'high': return '#e17055';
      default: return '#636e72';
    }
  };

  const getCongestionText = (congestion: string) => {
    switch (congestion) {
      case 'low': return '여유';
      case 'medium': return '보통';
      case 'high': return '혼잡';
      default: return '정보없음';
    }
  };

  return (
    <div className="relative h-screen p-0">
      {/* 상단 검색바 */}
      <div className="absolute top-5 left-5 right-5 z-10 bg-white rounded-2xl px-4 py-3 shadow-lg flex items-center">
        <span className="text-base mr-3">🔍</span>
        <input
          type="text"
          placeholder="노선, 지하철 역명 검색"
          className="border-none outline-none flex-1 text-base bg-transparent"
        />
        <button className="bg-none border-none text-base cursor-pointer p-1">
          🎛️
        </button>
      </div>

      {/* 지도 */}
      <div className="h-screen">
        <MapContainer
          center={[37.5665, 126.9780]} // 서울 시청 좌표
          zoom={13}
          style={{ height: '100%', width: '100%' }}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          
          {/* 버스 마커 */}
          {buses.map((bus) => (
            <Marker
              key={bus.id}
              position={bus.position}
              icon={busIcon}
            >
              <Popup>
                <div className="text-center min-w-[150px]">
                  <h3 className="m-0 mb-2 text-base text-gray-800">
                    {bus.route}
                  </h3>
                  <div 
                    className="text-white px-2 py-1 rounded-lg text-xs mb-2 inline-block"
                    style={{ backgroundColor: getCongestionColor(bus.congestion) }}
                  >
                    {getCongestionText(bus.congestion)}
                  </div>
                  <p className="my-1 text-sm text-gray-600">
                    다음 정류장: {bus.nextStop}
                  </p>
                  <p className="m-0 text-sm font-bold text-indigo-500">
                    {bus.eta}분 후 도착
                  </p>
                </div>
              </Popup>
            </Marker>
          ))}

          {/* 정류장 마커 */}
          {busStops.map((stop) => (
            <Marker
              key={stop.id}
              position={stop.position}
              icon={busStopIcon}
            >
              <Popup>
                <div className="text-center min-w-[150px]">
                  <h3 className="m-0 mb-2 text-base text-gray-800">
                    {stop.name}
                  </h3>
                  <div className="text-sm text-gray-600">
                    <p className="my-1">운행 노선:</p>
                    <div className="flex flex-wrap gap-1 justify-center">
                      {stop.routes.map((route, index) => (
                        <span
                          key={index}
                          className="bg-indigo-500 text-white px-2 py-1 rounded-md text-xs"
                        >
                          {route}
                        </span>
                      ))}
                    </div>
                  </div>
                </div>
              </Popup>
            </Marker>
          ))}
        </MapContainer>
      </div>

      {/* 지역 정보 카드 */}
      <div className="absolute bottom-24 left-5 right-5 z-10 bg-white rounded-3xl p-4 shadow-xl">
        <div className="flex justify-between items-center mb-3">
          <h3 className="m-0 text-base font-semibold text-gray-800">
            강남구 대치동
          </h3>
          <span className="text-xs text-gray-400 px-2 py-1 bg-gray-100 rounded-lg">
            모든 보기 ▼
          </span>
        </div>
        
        <div className="flex gap-2 overflow-x-auto pb-2">
          {bottomNavItems.map((item) => (
            <div
              key={item.id}
              onClick={() => setSelectedFilter(item.id)}
              className={`flex flex-col items-center justify-center cursor-pointer p-3 min-w-[60px] rounded-full aspect-square border-2 transition-all ${
                selectedFilter === item.id 
                  ? 'shadow-lg' 
                  : 'border-transparent'
              }`}
              style={{
                backgroundColor: selectedFilter === item.id ? item.color : '#f8f8f8',
                borderColor: selectedFilter === item.id ? `${item.color}30` : 'transparent'
              }}
            >
              <span 
                className="text-2xl"
                style={{
                  filter: selectedFilter === item.id ? 'brightness(0) invert(1)' : 'none'
                }}
              >
                {item.icon}
              </span>
            </div>
          ))}
        </div>
      </div>

      {/* 하단 네비게이션 바 */}
      <div className="absolute bottom-0 left-0 right-0 bg-white py-3 px-5 flex justify-around items-center border-t border-gray-100">
        <div className="text-xl cursor-pointer">🎯</div>
        <div className="text-xl cursor-pointer">⚡</div>
        <div className="text-xl cursor-pointer">🔔</div>
        <div className="text-xl cursor-pointer">⋯</div>
      </div>
    </div>
  );
}