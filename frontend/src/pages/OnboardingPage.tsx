import { useNavigate } from 'react-router-dom';

export function OnboardingPage() {
  const navigate = useNavigate();

  const handleGetStarted = () => {
    navigate('/search');
  };

  return (
    <div className="page">
      <div className="container">
        <div style={{ paddingTop: '60px', textAlign: 'center' }}>
          <h1 style={{ 
            fontSize: '28px', 
            fontWeight: 'bold', 
            marginBottom: '16px',
            color: '#2d3436'
          }}>
            버스가 언제 올까요?
          </h1>
          <p style={{ 
            fontSize: '16px', 
            color: '#636e72', 
            marginBottom: '60px' 
          }}>
            실시간으로 확인하는 버스 정보 서비스
          </p>

          <div className="card" style={{ marginBottom: '24px' }}>
            <div style={{ 
              display: 'flex', 
              alignItems: 'center', 
              marginBottom: '16px' 
            }}>
              <div style={{
                width: '48px',
                height: '48px',
                background: 'linear-gradient(135deg, #74b9ff 0%, #0984e3 100%)',
                borderRadius: '12px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                marginRight: '16px'
              }}>
                <span style={{ fontSize: '24px' }}>🚌</span>
              </div>
              <div>
                <h3 style={{ 
                  fontSize: '18px', 
                  fontWeight: '600', 
                  color: '#2d3436',
                  marginBottom: '4px'
                }}>
                  실시간 위치 확인
                </h3>
                <p style={{ 
                  fontSize: '14px', 
                  color: '#636e72' 
                }}>
                  버스의 현재 위치를 실시간으로 추적
                </p>
              </div>
            </div>
          </div>

          <div className="card" style={{ marginBottom: '24px' }}>
            <div style={{ 
              display: 'flex', 
              alignItems: 'center', 
              marginBottom: '16px' 
            }}>
              <div style={{
                width: '48px',
                height: '48px',
                background: 'linear-gradient(135deg, #fd79a8 0%, #e84393 100%)',
                borderRadius: '12px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                marginRight: '16px'
              }}>
                <span style={{ fontSize: '24px' }}>⏰</span>
              </div>
              <div>
                <h3 style={{ 
                  fontSize: '18px', 
                  fontWeight: '600', 
                  color: '#2d3436',
                  marginBottom: '4px'
                }}>
                  도착 시간 예측
                </h3>
                <p style={{ 
                  fontSize: '14px', 
                  color: '#636e72' 
                }}>
                  정확한 도착 시간과 남은 정류장 수
                </p>
              </div>
            </div>
          </div>

          <div className="card" style={{ marginBottom: '60px' }}>
            <div style={{ 
              display: 'flex', 
              alignItems: 'center', 
              marginBottom: '16px' 
            }}>
              <div style={{
                width: '48px',
                height: '48px',
                background: 'linear-gradient(135deg, #fdcb6e 0%, #e17055 100%)',
                borderRadius: '12px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                marginRight: '16px'
              }}>
                <span style={{ fontSize: '24px' }}>📊</span>
              </div>
              <div>
                <h3 style={{ 
                  fontSize: '18px', 
                  fontWeight: '600', 
                  color: '#2d3436',
                  marginBottom: '4px'
                }}>
                  혼잡도 정보
                </h3>
                <p style={{ 
                  fontSize: '14px', 
                  color: '#636e72' 
                }}>
                  버스 내부 혼잡도를 미리 확인
                </p>
              </div>
            </div>
          </div>

          <button 
            className="btn btn-primary"
            onClick={handleGetStarted}
            style={{ 
              width: '100%', 
              padding: '16px',
              fontSize: '18px',
              marginBottom: '20px'
            }}
          >
            시작하기
          </button>
        </div>
      </div>
    </div>
  );
}