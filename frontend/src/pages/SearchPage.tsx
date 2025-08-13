import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

type FilterType = 'card' | 'subway' | 'express' | 'general' | 'electric' | 'autonomous' | 
                  'cafe' | 'shopping' | 'accessory' | 'book' | 'medical' | 'finance';

export function SearchPage() {
  const navigate = useNavigate();
  const [selectedFilters, setSelectedFilters] = useState<FilterType[]>([]);
  const [searchQuery, setSearchQuery] = useState('');

  const filters = [
    { id: 'card' as FilterType, label: '카드', color: '#74b9ff', icon: '💳' },
    { id: 'subway' as FilterType, label: '지하철', color: '#fd79a8', icon: '🚇' },
    { id: 'express' as FilterType, label: '광역버스', color: '#fdcb6e', icon: '🚌' },
    { id: 'general' as FilterType, label: '일반버스', color: '#55a3ff', icon: '🚐' },
    { id: 'electric' as FilterType, label: '전기버스', color: '#00b894', icon: '🔋' },
    { id: 'autonomous' as FilterType, label: '자율주행', color: '#e17055', icon: '🤖' },
    { id: 'cafe' as FilterType, label: '카페', color: '#a29bfe', icon: '☕' },
    { id: 'shopping' as FilterType, label: '쇼핑', color: '#fd79a8', icon: '🛍️' },
    { id: 'accessory' as FilterType, label: '악세서리', color: '#fdcb6e', icon: '👜' },
    { id: 'book' as FilterType, label: '책', color: '#74b9ff', icon: '📚' },
    { id: 'medical' as FilterType, label: '의료/건강', color: '#00b894', icon: '🏥' },
    { id: 'finance' as FilterType, label: '금융상품', color: '#e17055', icon: '💰' }
  ];

  const toggleFilter = (filterId: FilterType) => {
    setSelectedFilters(prev => 
      prev.includes(filterId) 
        ? prev.filter(id => id !== filterId)
        : [...prev, filterId]
    );
  };

  const handleNext = () => {
    navigate('/map');
  };

  return (
    <div className="page">
      <div className="container">
        <div style={{ paddingTop: '20px' }}>
          {/* 헤더 */}
          <div style={{ 
            display: 'flex', 
            alignItems: 'center', 
            marginBottom: '32px' 
          }}>
            <button 
              onClick={() => navigate(-1)}
              style={{
                background: 'none',
                border: 'none',
                fontSize: '20px',
                cursor: 'pointer',
                padding: '8px',
                marginRight: '16px'
              }}
            >
              ←
            </button>
            <h1 style={{ 
              fontSize: '22px', 
              fontWeight: 'bold', 
              color: '#2d3436' 
            }}>
              검색 필터 설정
            </h1>
          </div>

          {/* 검색바 */}
          <div className="input-group">
            <label>지역</label>
            <div style={{ position: 'relative' }}>
              <input
                type="text"
                placeholder="목적지를 입력하세요"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{ paddingRight: '48px' }}
              />
              <span style={{
                position: 'absolute',
                right: '16px',
                top: '50%',
                transform: 'translateY(-50%)',
                fontSize: '16px',
                color: '#636e72'
              }}>
                📍
              </span>
            </div>
          </div>

          {/* 거리 범위 */}
          <div style={{ marginBottom: '32px' }}>
            <label style={{ 
              display: 'block', 
              marginBottom: '12px',
              fontWeight: '600',
              color: '#495057'
            }}>
              거리 범위
            </label>
            <div style={{
              background: 'white',
              borderRadius: '12px',
              padding: '20px',
              border: '2px solid #e9ecef'
            }}>
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                marginBottom: '12px'
              }}>
                <span style={{ fontSize: '14px', color: '#636e72' }}>1km</span>
                <span style={{ fontSize: '16px', fontWeight: '600', color: '#2d3436' }}>5.2km</span>
                <span style={{ fontSize: '14px', color: '#636e72' }}>10.0km</span>
              </div>
              <div style={{
                height: '8px',
                background: '#e9ecef',
                borderRadius: '4px',
                position: 'relative'
              }}>
                <div style={{
                  position: 'absolute',
                  left: '0%',
                  width: '52%',
                  height: '100%',
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  borderRadius: '4px'
                }} />
              </div>
            </div>
          </div>

          {/* 물품 분류 */}
          <div style={{ marginBottom: '32px' }}>
            <label style={{ 
              display: 'block', 
              marginBottom: '16px',
              fontWeight: '600',
              color: '#495057'
            }}>
              물품 분류
            </label>
            <div className="icon-grid">
              {filters.map(filter => (
                <div
                  key={filter.id}
                  className={`icon-item ${selectedFilters.includes(filter.id) ? 'active' : ''}`}
                  onClick={() => toggleFilter(filter.id)}
                >
                  <div style={{
                    fontSize: '24px',
                    marginBottom: '8px'
                  }}>
                    {filter.icon}
                  </div>
                  <span style={{
                    fontSize: '12px',
                    fontWeight: '500',
                    color: selectedFilters.includes(filter.id) ? '#667eea' : '#495057'
                  }}>
                    {filter.label}
                  </span>
                </div>
              ))}
            </div>
          </div>

          {/* 다음 버튼 */}
          <button 
            className="btn btn-primary"
            onClick={handleNext}
            style={{ 
              width: '100%', 
              padding: '16px',
              fontSize: '18px',
              marginBottom: '20px'
            }}
          >
            검색하기
          </button>
        </div>
      </div>
    </div>
  );
}