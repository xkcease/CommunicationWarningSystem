package com.example.communicationwarningsystem.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.communicationwarningsystem.R;
import com.example.communicationwarningsystem.activity.DetailActivity;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.util.Util;

public class NoticeService extends Service {
    public static final String TAG = "ComWa-Notice";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: executed");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: executed");


        Intent activityIntent = new Intent(NoticeService.this, DetailActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

        String text = "";
        Bundle bundle = intent.getExtras();
        if(bundle != null){                                    // 提示信息设置
            Phone phone = (Phone) bundle.getSerializable("phone");
            if (phone != null) {
                if (phone.getLevel().equals(Util.NORMAL)) {
                    text = "普通电话";
                } else if (phone.getLevel().equals(Util.STRANGE)) {
                    text = "陌生电话";
                } else if (phone.getLevel().equals(Util.RISK)) {
                    text = "风险电话";
                } else {
                    text = "危险电话";
                }

                int index = bundle.getInt("index");
                if(index == 1) {                            // 发来的是短信
                    text += "发来的短信，注意查看";
                    Log.d(TAG, "onStartCommand: " + index);
                }
                else {                                  // 来电
                    text += "来电";
                    if(!phone.getLevel().equals(Util.NORMAL)){
                        text += "，谨慎接听!!";
                    }
                }

                Log.d(TAG, "onStartCommand: " + phone.getNumber() + "--" + Util.getLevelString(phone.getLevel()));
            }

            activityIntent.putExtras(bundle);

        }


        PendingIntent pendingIntent = PendingIntent.getActivity(NoticeService.this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = "channel";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(NoticeService.this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("新消息")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NoticeService.this);
        notificationManager.notify( 1, builder.build());
        
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: executed");
    }
}
