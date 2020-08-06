package com.example.communicationwarningsystem.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallUtil {
    private final static String TAG = "ComWa-CallUtil";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Context context;
    private String filename;
    private static CallUtil callUtil;

    private CallUtil(Context context){
        this.context = context;
        this.filename = "";
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.d(TAG, "CallUtil: ok");
        }
        else {
            Log.d(TAG, "CallUtil: no");
        }

        mediaRecorder = new MediaRecorder();
        mediaPlayer = new MediaPlayer();
    }


    // 单例
    public static CallUtil getInstance(Context context){
        if(callUtil == null){
            callUtil = new CallUtil(context);
        }
        return callUtil;
    }


    // 开始录音
    public void startRecord(String number){
        try {
            Log.d(TAG, "startRecord: "+ context.getFilesDir());

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);            // 音源
            mediaRecorder.setAudioSamplingRate(44100);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);           // 格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);              // 编码
            filename = context.getFilesDir() + "/" + number + getTimeForFile() + ".m4a";
            mediaRecorder.setOutputFile(filename);

            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.d(TAG, "startRecord: start record");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 结束录音
    public String stopRecord(){
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Log.d(TAG, "stopRecord: end");
        }
        return filename;
    }


    // 播放
    public void play(String filename){
        try {
            Log.d(TAG, "play: start");
            mediaPlayer.setDataSource(filename);
            mediaPlayer.setVolume(1,1);
            mediaPlayer.setLooping(false);

                // 结束监听
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                Toast.makeText(context, "播放完毕", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCompletion: end");
                }
            });

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // 获取时间
    private String getTimeForFile(){
        String time =  new SimpleDateFormat("-yyyMMddHHmm").format(new Date(System.currentTimeMillis()));
        return time;
    }
}
