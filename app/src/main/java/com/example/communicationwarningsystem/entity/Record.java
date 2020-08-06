package com.example.communicationwarningsystem.entity;

import java.io.Serializable;

public class Record implements Serializable {
    private Integer id;
    private Integer state;          // 状态
    private Long duration;       // 通话时长
    private String date;            // 日期
    private String filename;        // 录音文件


    public Record(Integer id){
        this.id = id;
        this.duration = 0L;
        this.filename = "";
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", state=" + state +
                ", duration=" + duration +
                ", date='" + date + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
