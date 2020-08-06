package com.example.communicationwarningsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.communicationwarningsystem.R;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.entity.Statistics;
import com.example.communicationwarningsystem.util.Util;

public class DetailActivity extends AppCompatActivity {
    public static final int CODE = 1;
    private final String TAG = "ComWa-Detail";
    private Phone phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        init(null);     // 初始化


        // 进入编辑页面
        TextView editText = findViewById(R.id.text_edit);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("phone", phone);
                intent.putExtras(bundle);

                startActivityForResult(intent, EditActivity.CODE);
            }
        });


        // 进入更多信息页面
        TextView moreText = findViewById(R.id.text_more);
        moreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, RecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("phone", phone);
                intent.putExtras(bundle);

                startActivityForResult(intent, RecordActivity.CODE);
            }
        });


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




    // 响应数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EditActivity.CODE && resultCode == RESULT_OK){        // 编辑页响应
            init(data);
        }
        else if(requestCode == RecordActivity.CODE && resultCode == RESULT_OK){     // 记录页响应
            init(data);
        }
    }



    // 初始化手机号信息
    private void initPhone(Phone phone){
        TextView nameText = findViewById(R.id.text_name);
        TextView numberText = findViewById(R.id.text_number);
        TextView levelText = findViewById(R.id.text_level);
        TextView noteText = findViewById(R.id.text_note);

        nameText.setText(phone.getName());
        numberText.setText((phone.getNumber()));
        levelText.setText(Util.getLevelString(phone.getLevel()));
        noteText.setText(phone.getNote());
        Log.d(TAG, "initPhone: " + phone.getNumber() + "--" + Util.getLevelString(phone.getLevel()));
    }



    // 初始化统计
    private void initStatistics(Statistics statistics){
        TextView msgcText = findViewById(R.id.text_msgc);
        TextView miscText = findViewById(R.id.text_misc);
        TextView rececText = findViewById(R.id.text_recec);
        TextView diacText = findViewById(R.id.text_diac);


        msgcText.setText("短信数：" + statistics.getMsgCount().toString());
        miscText.setText("未接通数：" + statistics.getMissedCount().toString());
        rececText.setText("已接通数" + statistics.getReceivedCount().toString());
        diacText.setText("拨打数：" + statistics.getDialledCount().toString());
    }


    // 初始化数据
    private void init(Intent intent){

        if(intent == null){         // MainActivity
            intent = getIntent();
        }

        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                phone = (Phone)bundle.getSerializable("phone");         // 手机
                if(phone != null){
                    initPhone(phone);
                }

                Statistics statistics = (Statistics)bundle.getSerializable("statistics");   // 统计
                if(statistics != null){
                    initStatistics(statistics);
                }

            }
        }
    }

}



