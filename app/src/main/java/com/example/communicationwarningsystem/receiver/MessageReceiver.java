package com.example.communicationwarningsystem.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.communicationwarningsystem.db.DbHelper;
import com.example.communicationwarningsystem.entity.Message;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.entity.Statistics;
import com.example.communicationwarningsystem.service.NoticeService;
import com.example.communicationwarningsystem.util.Util;

public class MessageReceiver extends BroadcastReceiver {
    private final static String TAG = "ComWa-MsgRec";

    @Override
    public void onReceive(Context context, Intent intent) {

        StringBuilder content = new StringBuilder();
        String number = null;

        Bundle bundle = intent.getExtras();
        if (bundle != null) {               // 监听到短信
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (Object object : pdus) {
                SmsMessage message= SmsMessage.createFromPdu((byte[])object);
                number = message.getOriginatingAddress();                   //获取短信手机号
                content.append(message.getMessageBody());                   //获取短信内容
                Log.d(TAG, "onReceive: " + number + "-" + content.toString());
            }
        }


        DbHelper dbHelper = DbHelper.getInstance(context);

        Phone phone = dbHelper.queryPhoneByNumber(number);          // 检索号码
        Statistics statistics;

        if(phone == null){              // 陌生号码
            phone = new Phone();
            phone.setNumber(number);
            phone.setLevel(Util.STRANGE);

            dbHelper.insertPhone(phone);           // 添加
            phone = dbHelper.queryPhoneByNumber(number);
            dbHelper.insertStatistics(new Statistics(phone.getId()));
        }
        else if(phone.getLevel().equals(Util.TRUST)){       // 信任号码，不发通知
            Log.d(TAG, "onReceive: trust");
            return;
        }
        else if(phone.getLevel().equals(Util.STRANGE)){         // 第二次接到同一个电话
            Log.d(TAG, "onReceive: modify level to normal");
            phone.setLevel(Util.NORMAL);
            dbHelper.updatePhone(phone);
        }


        Message message = new Message(phone.getId());
        message.setId(phone.getId());
        message.setContent(content.toString());
        message.setDate(Util.getTime());
        dbHelper.insertMessage(message);                    // 添加短信


        statistics = dbHelper.queryStatistics(phone.getId());
        statistics.setMsgCount(statistics.getMsgCount() + 1);
        dbHelper.updateStatistics(statistics);                  // 更新短信数

        Intent serviceIntent = new Intent(context, NoticeService.class);
        Bundle serviceBundle = new Bundle();
        serviceBundle.putSerializable("phone", phone);
        serviceBundle.putSerializable("statistics", statistics);
        serviceBundle.putInt("index", 1);
        serviceIntent.putExtras(serviceBundle);

        context.startService(serviceIntent);                                    // 启动前台服务

    }
}
