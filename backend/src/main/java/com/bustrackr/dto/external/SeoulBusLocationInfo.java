package com.bustrackr.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeoulBusLocationInfo {
    
    @JsonProperty("busRouteId")
    private String busRouteId; // 노선 ID
    
    @JsonProperty("routeNm")
    private String routeNm; // 노선명
    
    @JsonProperty("plateNo")
    private String plateNo; // 차량번호
    
    @JsonProperty("stationId")
    private String stationId; // 현재 정류장 ID
    
    @JsonProperty("stationNm")
    private String stationNm; // 현재 정류장명
    
    @JsonProperty("tmX")
    private String tmX; // X좌표
    
    @JsonProperty("tmY")
    private String tmY; // Y좌표
    
    @JsonProperty("posX")
    private String posX; // GPS X좌표
    
    @JsonProperty("posY")
    private String posY; // GPS Y좌표
    
    @JsonProperty("dataTm")
    private String dataTm; // 데이터 생성 시각
    
    @JsonProperty("lastStnId")
    private String lastStnId; // 종점 정류장 ID
    
    @JsonProperty("congetion")  // 서울시 API의 오타
    private String congetion; // 혼잡도
    
    @JsonProperty("isFullFlag")
    private String isFullFlag; // 만차여부
    
    // Getters and Setters
    public String getBusRouteId() { return busRouteId; }
    public void setBusRouteId(String busRouteId) { this.busRouteId = busRouteId; }
    
    public String getRouteNm() { return routeNm; }
    public void setRouteNm(String routeNm) { this.routeNm = routeNm; }
    
    public String getPlateNo() { return plateNo; }
    public void setPlateNo(String plateNo) { this.plateNo = plateNo; }
    
    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }
    
    public String getStationNm() { return stationNm; }
    public void setStationNm(String stationNm) { this.stationNm = stationNm; }
    
    public String getTmX() { return tmX; }
    public void setTmX(String tmX) { this.tmX = tmX; }
    
    public String getTmY() { return tmY; }
    public void setTmY(String tmY) { this.tmY = tmY; }
    
    public String getPosX() { return posX; }
    public void setPosX(String posX) { this.posX = posX; }
    
    public String getPosY() { return posY; }
    public void setPosY(String posY) { this.posY = posY; }
    
    public String getDataTm() { return dataTm; }
    public void setDataTm(String dataTm) { this.dataTm = dataTm; }
    
    public String getLastStnId() { return lastStnId; }
    public void setLastStnId(String lastStnId) { this.lastStnId = lastStnId; }
    
    public String getCongetion() { return congetion; }
    public void setCongetion(String congetion) { this.congetion = congetion; }
    
    public String getIsFullFlag() { return isFullFlag; }
    public void setIsFullFlag(String isFullFlag) { this.isFullFlag = isFullFlag; }
}