package com.bustrackr.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SeoulBusApiResponse<T> {
    
    @JsonProperty("comMsgHeader")
    private ComMsgHeader comMsgHeader;
    
    @JsonProperty("msgHeader")
    private MsgHeader msgHeader;
    
    @JsonProperty("msgBody")
    private MsgBody<T> msgBody;
    
    public static class ComMsgHeader {
        private String requestMsgID;
        private String responseMsgID;
        private String responseTime;
        
        // Getters and Setters
        public String getRequestMsgID() { return requestMsgID; }
        public void setRequestMsgID(String requestMsgID) { this.requestMsgID = requestMsgID; }
        
        public String getResponseMsgID() { return responseMsgID; }
        public void setResponseMsgID(String responseMsgID) { this.responseMsgID = responseMsgID; }
        
        public String getResponseTime() { return responseTime; }
        public void setResponseTime(String responseTime) { this.responseTime = responseTime; }
    }
    
    public static class MsgHeader {
        private String headerMsg;
        private String headerCd;
        private int itemCount;
        
        // Getters and Setters
        public String getHeaderMsg() { return headerMsg; }
        public void setHeaderMsg(String headerMsg) { this.headerMsg = headerMsg; }
        
        public String getHeaderCd() { return headerCd; }
        public void setHeaderCd(String headerCd) { this.headerCd = headerCd; }
        
        public int getItemCount() { return itemCount; }
        public void setItemCount(int itemCount) { this.itemCount = itemCount; }
    }
    
    public static class MsgBody<T> {
        private List<T> itemList;
        
        public List<T> getItemList() { return itemList; }
        public void setItemList(List<T> itemList) { this.itemList = itemList; }
    }
    
    // Main getters and setters
    public ComMsgHeader getComMsgHeader() { return comMsgHeader; }
    public void setComMsgHeader(ComMsgHeader comMsgHeader) { this.comMsgHeader = comMsgHeader; }
    
    public MsgHeader getMsgHeader() { return msgHeader; }
    public void setMsgHeader(MsgHeader msgHeader) { this.msgHeader = msgHeader; }
    
    public MsgBody<T> getMsgBody() { return msgBody; }
    public void setMsgBody(MsgBody<T> msgBody) { this.msgBody = msgBody; }
}