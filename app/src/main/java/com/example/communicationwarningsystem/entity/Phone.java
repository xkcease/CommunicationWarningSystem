package com.example.communicationwarningsystem.entity;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Phone implements Serializable{
    private Integer id;
    private String number;      // 手机号
    private Integer level;      // 安全系数
    private String name;
    private String note;

    public Phone(){
        this.name = "";
        this.note = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Phone){
            Phone phone = (Phone)obj;
            return this.number.equals(phone.getNumber());
        }
        return super.equals(obj);
    }
}
