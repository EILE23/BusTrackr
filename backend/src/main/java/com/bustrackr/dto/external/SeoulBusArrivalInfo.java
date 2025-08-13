package com.bustrackr.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeoulBusArrivalInfo {
    
    @JsonProperty("stId")
    private String stId; // 정류소 ID
    
    @JsonProperty("stNm")
    private String stNm; // 정류소명
    
    @JsonProperty("busRouteId")
    private String busRouteId; // 노선 ID
    
    @JsonProperty("rtNm")
    private String rtNm; // 노선명
    
    @JsonProperty("adirection")
    private String adirection; // 방향
    
    @JsonProperty("arrmsg1")
    private String arrmsg1; // 첫번째 버스 도착예정시간
    
    @JsonProperty("arrmsg2")  
    private String arrmsg2; // 두번째 버스 도착예정시간
    
    @JsonProperty("arrmsgSec1")
    private String arrmsgSec1; // 첫번째 버스 도착예정시간(초)
    
    @JsonProperty("arrmsgSec2")
    private String arrmsgSec2; // 두번째 버스 도착예정시간(초)
    
    @JsonProperty("plainNo1")
    private String plainNo1; // 첫번째 버스 차량번호
    
    @JsonProperty("plainNo2")
    private String plainNo2; // 두번째 버스 차량번호
    
    @JsonProperty("stationNm1")
    private String stationNm1; // 첫번째 버스 현재 위치 정류장
    
    @JsonProperty("stationNm2")
    private String stationNm2; // 두번째 버스 현재 위치 정류장
    
    @JsonProperty("reride_Num1")
    private String rerideNum1; // 첫번째 버스 남은 정류장 수
    
    @JsonProperty("reride_Num2")
    private String rerideNum2; // 두번째 버스 남은 정류장 수
    
    // Getters and Setters
    public String getStId() { return stId; }
    public void setStId(String stId) { this.stId = stId; }
    
    public String getStNm() { return stNm; }
    public void setStNm(String stNm) { this.stNm = stNm; }
    
    public String getBusRouteId() { return busRouteId; }
    public void setBusRouteId(String busRouteId) { this.busRouteId = busRouteId; }
    
    public String getRtNm() { return rtNm; }
    public void setRtNm(String rtNm) { this.rtNm = rtNm; }
    
    public String getAdirection() { return adirection; }
    public void setAdirection(String adirection) { this.adirection = adirection; }
    
    public String getArrmsg1() { return arrmsg1; }
    public void setArrmsg1(String arrmsg1) { this.arrmsg1 = arrmsg1; }
    
    public String getArrmsg2() { return arrmsg2; }
    public void setArrmsg2(String arrmsg2) { this.arrmsg2 = arrmsg2; }
    
    public String getArrmsgSec1() { return arrmsgSec1; }
    public void setArrmsgSec1(String arrmsgSec1) { this.arrmsgSec1 = arrmsgSec1; }
    
    public String getArrmsgSec2() { return arrmsgSec2; }
    public void setArrmsgSec2(String arrmsgSec2) { this.arrmsgSec2 = arrmsgSec2; }
    
    public String getPlainNo1() { return plainNo1; }
    public void setPlainNo1(String plainNo1) { this.plainNo1 = plainNo1; }
    
    public String getPlainNo2() { return plainNo2; }
    public void setPlainNo2(String plainNo2) { this.plainNo2 = plainNo2; }
    
    public String getStationNm1() { return stationNm1; }
    public void setStationNm1(String stationNm1) { this.stationNm1 = stationNm1; }
    
    public String getStationNm2() { return stationNm2; }
    public void setStationNm2(String stationNm2) { this.stationNm2 = stationNm2; }
    
    public String getRerideNum1() { return rerideNum1; }
    public void setRerideNum1(String rerideNum1) { this.rerideNum1 = rerideNum1; }
    
    public String getRerideNum2() { return rerideNum2; }
    public void setRerideNum2(String rerideNum2) { this.rerideNum2 = rerideNum2; }
}