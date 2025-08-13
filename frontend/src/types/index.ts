export interface BusRoute {
  routeId: string;
  routeName: string;
  routeType: 'express' | 'trunk' | 'branch' | 'circular' | 'wide';
  direction: string;
}

export interface BusStop {
  stopId: string;
  stopName: string;
  latitude: number;
  longitude: number;
  direction: string;
}

export interface BusLocation {
  busId: string;
  routeId: string;
  latitude: number;
  longitude: number;
  speed: number;
  congestion: 'low' | 'medium' | 'high';
  nextStopId: string;
  estimatedArrival: number;
}

export interface BusArrival {
  routeId: string;
  stopId: string;
  estimatedTime: number;
  remainingStops: number;
  congestion: 'low' | 'medium' | 'high';
}