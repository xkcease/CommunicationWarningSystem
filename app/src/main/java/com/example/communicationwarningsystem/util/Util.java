package com.example.communicationwarningsystem.util;

import com.example.communicationwarningsystem.entity.Phone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util {
    public static final Integer TRUST = 1;
    public static final Integer NORMAL = 2;
    public static final Integer STRANGE = 3;
    public static final Integer RISK = 4;
    public static final Integer MENACE = 5;

    public static final Integer MISSED = 0;
    public static final Integer RECEIVED = 1;
    public static final Integer DIALED = 2;


    // 求手机集合差集
    public static List<Phone> subtraction(List<Phone> list1, List<Phone> list2){
        List<Phone> subList = new ArrayList<>();

        for(Phone phone : list2){
            if(!list1.contains(phone)){
                subList.add(phone);
            }
        }

        return subList;
    }



    // 获取安全系数字符串
    public static String getLevelString(Integer level){
        String string = "";

        switch (level){
            case 1:
                string = "Trust";
                break;
            case 2:
                string = "Normal";
                break;
            case 3:
                string = "Strange";
                break;
            case 4:
                string = "Risk";
                break;
            case 5:
                string = "Menace";
                break;
            default:
                break;
        }

        return string;
    }


    public static String getStateString(Integer state){
        String string = "";

        switch (state){
            case 0:
                string = "未接来电";
                break;
            case 1:
                string = "已接来电";
                break;
            case 2:
                string = "已拨电话";
                break;
            default:
                break;
        }

        return string;
    }


    // 获取当前日期
    public static String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
