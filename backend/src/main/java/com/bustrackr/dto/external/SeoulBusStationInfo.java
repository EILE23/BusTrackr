package com.bustrackr.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeoulBusStationInfo {
    
    @JsonProperty("arsId")
    private String arsId; // 정류소 고유번호
    
    @JsonProperty("stId")
    private String stId; // 정류소 ID
    
    @JsonProperty("stNm")
    private String stNm; // 정류소명
    
    @JsonProperty("tmX")
    private String tmX; // X좌표 (경도)
    
    @JsonProperty("tmY")
    private String tmY; // Y좌표 (위도)
    
    @JsonProperty("posX")
    private String posX; // GPS X좌표
    
    @JsonProperty("posY")
    private String posY; // GPS Y좌표
    
    @JsonProperty("stationTp")
    private String stationTp; // 정류소 타입
    
    @JsonProperty("stationNm")
    private String stationNm; // 역명
    
    @JsonProperty("direction")
    private String direction; // 방향
    
    // Getters and Setters
    public String getArsId() { return arsId; }
    public void setArsId(String arsId) { this.arsId = arsId; }
    
    public String getStId() { return stId; }
    public void setStId(String stId) { this.stId = stId; }
    
    public String getStNm() { return stNm; }
    public void setStNm(String stNm) { this.stNm = stNm; }
    
    public String getTmX() { return tmX; }
    public void setTmX(String tmX) { this.tmX = tmX; }
    
    public String getTmY() { return tmY; }
    public void setTmY(String tmY) { this.tmY = tmY; }
    
    public String getPosX() { return posX; }
    public void setPosX(String posX) { this.posX = posX; }
    
    public String getPosY() { return posY; }
    public void setPosY(String posY) { this.posY = posY; }
    
    public String getStationTp() { return stationTp; }
    public void setStationTp(String stationTp) { this.stationTp = stationTp; }
    
    public String getStationNm() { return stationNm; }
    public void setStationNm(String stationNm) { this.stationNm = stationNm; }
    
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}