package com.example.communicationwarningsystem.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.communicationwarningsystem.db.DbHelper;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.entity.Record;
import com.example.communicationwarningsystem.entity.Statistics;
import com.example.communicationwarningsystem.service.NoticeService;
import com.example.communicationwarningsystem.util.CallUtil;
import com.example.communicationwarningsystem.util.Util;

public class CallReceiver extends BroadcastReceiver {
    public final static String TAG = "ComWa-CallRe";

    private static boolean incomingFlag = false;
    private static boolean answerFlag = false;
    private static long beginTime = 0;
    private static Integer id = -1;
    private static CallUtil callUtil;


    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        DbHelper dbHelper = DbHelper.getInstance(context);
        callUtil = CallUtil.getInstance(context);

        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())
                && "RINGING".equals(intent.getStringExtra(TelephonyManager.EXTRA_STATE))) {         //响铃，来电

            if(number != null) {
                Log.d(TAG, "来电号码: " + number);
                incomingFlag = true;

                Intent serviceIntent = new Intent(context, NoticeService.class);

                Phone phone = dbHelper.queryPhoneByNumber(number);          // 检索号码
                Statistics statistics;


                if(phone == null){              // 陌生电话
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


                id = phone.getId();
                statistics = dbHelper.queryStatistics(id);

                Bundle bundle = new Bundle();
                bundle.putSerializable("phone", phone);
                bundle.putSerializable("statistics", statistics);
                serviceIntent.putExtras(bundle);

                context.startService(serviceIntent);                                    // 启动前台服务
            }
        }
        else {
            if(number != null) {
                if(incomingFlag){           //来电
                    if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){    //挂断电话
                        if(!answerFlag) {                       //拒接并挂断
                            Log.d(TAG, "未接电话: " + number);
                            incomingFlag = false ;


                            Record record = new Record(id);
                            record.setState(Util.MISSED);
                            record.setDate(Util.getTime());
                            boolean f = dbHelper.insertRecord(record);          // 添加通话记录

                            Statistics statistics = dbHelper.queryStatistics(id);
                            statistics.setMissedCount(statistics.getMissedCount() + 1);     // 更新未接数
                            dbHelper.updateStatistics(statistics);

                            Log.d(TAG, "onReceive: "+ f);
                            Log.d(TAG, "onReceive: insert missed");
                        }
                        else{               //接听后挂断
                            long endTime = System.currentTimeMillis();
                            long duration = (endTime - beginTime)/1000;

                            Log.d(TAG, "接听完电话: " + number);
                            Log.d(TAG, "接听时长: " + duration + "秒");

                            incomingFlag = false ;
                            answerFlag = false;

                            String filename = callUtil.stopRecord();

                            Record record = new Record(id);
                            record.setState(Util.RECEIVED);
                            record.setDuration(duration);
                            record.setDate(Util.getTime());
                            record.setFilename(filename);
                            dbHelper.insertRecord(record);          // 添加通话记录

                            Statistics statistics = dbHelper.queryStatistics(id);
                            statistics.setReceivedCount(statistics.getReceivedCount() + 1);         // 更新已接数
                            dbHelper.updateStatistics(statistics);

                            Log.d(TAG, "onReceive: insert received");
                        }
                    }
                    else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){        // 接听来电
                        beginTime = System.currentTimeMillis();
                        Log.d(TAG, "正在接听电话: " + number);
                        answerFlag = true;

                        callUtil.startRecord(number);           // 开始录音
                    }
                }
                else{            //拔出
                    if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {       //挂断电话
                        long endTime = System.currentTimeMillis();
                        long duration = (endTime - beginTime)/1000;

                        Log.d(TAG, "挂了拔出电话: " + number);
                        Log.d(TAG, "拔打时长: " + duration + "秒");

                        Record record = new Record(id);
                        record.setState(Util.DIALED);
                        record.setDuration(duration);
                        record.setDate(Util.getTime());
                        dbHelper.insertRecord(record);          // 添加通话记录

                        Statistics statistics = dbHelper.queryStatistics(id);
                        statistics.setDialledCount(statistics.getDialledCount() + 1);       // 更新拨出数
                        dbHelper.updateStatistics(statistics);

                        Log.d(TAG, "onReceive: insert dialled");
                    }
                    else{   //正拔出
                        Log.d(TAG, "已拔电话: " + number);
                        beginTime = System.currentTimeMillis();
                    }
                }
            }
        }
    }


}
