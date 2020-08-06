package com.example.communicationwarningsystem.entity;

import java.io.Serializable;

public class Statistics implements Serializable {
    private Integer id;
    private Integer msgCount;           // 邮件数
    private Integer missedCount;        // 未接通数
    private Integer receivedCount;      // 已接通数
    private Integer dialledCount;       // 拨打数


    public Statistics(Integer id){
        this.id = id;
        this.msgCount = 0;
        this.missedCount = 0;
        this.receivedCount = 0;
        this.dialledCount = 0;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
    }

    public Integer getMissedCount() {
        return missedCount;
    }

    public void setMissedCount(Integer missedCount) {
        this.missedCount = missedCount;
    }

    public Integer getReceivedCount() {
        return receivedCount;
    }

    public void setReceivedCount(Integer receivedCount) {
        this.receivedCount = receivedCount;
    }

    public Integer getDialledCount() {
        return dialledCount;
    }

    public void setDialledCount(Integer dialledCount) {
        this.dialledCount = dialledCount;
    }


}
