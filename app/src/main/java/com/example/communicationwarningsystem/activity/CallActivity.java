package com.example.communicationwarningsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.communicationwarningsystem.R;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.entity.Record;
import com.example.communicationwarningsystem.util.CallUtil;
import com.example.communicationwarningsystem.util.Util;

public class CallActivity extends AppCompatActivity {
    public static final int CODE = 5;
    private Record record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        init();

        // 返回
        ImageView backImage = findViewById(R.id.back);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    private void init(){
        TextView nameText = findViewById(R.id.text_name);
        TextView numberText = findViewById(R.id.text_number);
        TextView levelText = findViewById(R.id.text_level);
        TextView stateText = findViewById(R.id.text_state);
        TextView durationText = findViewById(R.id.text_duration);
        TextView dateText = findViewById(R.id.text_date);
        Button playBtn = findViewById(R.id.btn_play);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Phone phone = (Phone)bundle.getSerializable("phone");
                record = (Record)bundle.getSerializable("record");

                if(phone != null){
                    nameText.setText(phone.getName());
                    numberText.setText((phone.getNumber()));
                    levelText.setText(Util.getLevelString(phone.getLevel()));
                }
                if(record != null){
                    stateText.setText(Util.getStateString(record.getState()));
                    durationText.setText(record.getDuration().toString() + "秒");
                    dateText.setText(record.getDate());
                }
            }
        }


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallUtil.getInstance(CallActivity.this).play(record.getFilename());
            }
        });
    }
}
