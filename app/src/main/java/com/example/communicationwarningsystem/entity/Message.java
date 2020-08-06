package com.example.communicationwarningsystem.entity;

import java.io.Serializable;

public class Message implements Serializable {
    private Integer id;
    private String content;     // 内容
    private String date;        // 日期


    public Message(Integer id){
        this.id = id;
        this.content = "";
        this.date = "";
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
