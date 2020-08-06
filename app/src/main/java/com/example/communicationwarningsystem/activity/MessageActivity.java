package com.example.communicationwarningsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.communicationwarningsystem.R;
import com.example.communicationwarningsystem.entity.Message;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.util.Util;

public class MessageActivity extends AppCompatActivity {
    public final static int CODE = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


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
        TextView bannerText = findViewById(R.id.banner);
        TextView contentText = findViewById(R.id.text_number);
        TextView levelText = findViewById(R.id.text_level);
        TextView dateText = findViewById(R.id.text_date);
        TextView deleteText = findViewById(R.id.text_delete);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Message message = (Message) bundle.getSerializable("message");
                Phone phone = (Phone)bundle.getSerializable("phone");

                bannerText.setText(phone.getName() + "  " + phone.getNumber());
                levelText.setText(Util.getLevelString(phone.getLevel()));
                contentText.setText(message.getContent());
                dateText.setText(message.getDate());


            }
        }
    }
}
