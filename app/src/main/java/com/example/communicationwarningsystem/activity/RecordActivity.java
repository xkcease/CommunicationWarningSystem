package com.example.communicationwarningsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.communicationwarningsystem.R;
import com.example.communicationwarningsystem.adapter.RecordAdapter;
import com.example.communicationwarningsystem.db.DbHelper;
import com.example.communicationwarningsystem.entity.Message;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.entity.Record;

import java.util.List;

public class RecordActivity extends AppCompatActivity {
    public static final int CODE = 3;
    public static final String TAG = "ComWa-RecAct";
    private Phone phone;
    private List<Message> messageList;
    private List<Record> recordList;
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;
    private DbHelper dbHelper;
    private int index;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MessageActivity.CODE && resultCode == RESULT_OK){
            index = MessageActivity.CODE;
            showItemData(index);
        }
        else if(requestCode == CallActivity.CODE && resultCode == RESULT_OK){
            index = CallActivity.CODE;
            showItemData(index);
        }
    }


    // 初始化
    private void init() {
        index = MessageActivity.CODE;
        dbHelper = DbHelper.getInstance(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                phone = (Phone)bundle.getSerializable("phone");
                showItemData(index);
            }
        }

        TextView msgText = findViewById(R.id.text_msg);
        msgText.setOnClickListener(new View.OnClickListener() {     // 短信栏
            @Override
            public void onClick(View v) {
                index = MessageActivity.CODE;
                showItemData(index);
            }
        });

        TextView comText = findViewById(R.id.text_com);
        comText.setOnClickListener(new View.OnClickListener() {        // 通话记录栏
            @Override
            public void onClick(View v) {
                index = CallActivity.CODE;
                showItemData(index);
            }
        });


        // 进入详细页面
        recordAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener(){
            @Override
            public void onClick(int position) {
            if(index == MessageActivity.CODE){
                Message message = messageList.get(position);

                Intent intent = new Intent(RecordActivity.this, MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("message", message);
                bundle.putSerializable("phone", phone);
                intent.putExtras(bundle);

                startActivityForResult(intent, MessageActivity.CODE);
            }
            else {
                Record record = recordList.get(position);
                Log.d(TAG, "onClick: " + record);
                Intent intent = new Intent(RecordActivity.this, CallActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("record", record);
                bundle.putSerializable("phone", phone);
                intent.putExtras(bundle);

                startActivityForResult(intent, CallActivity.CODE);
            }
            }
        });
    }


    // 展示数据
    private void showItemData(int index){
        if(index == MessageActivity.CODE){             // 短信
            if(messageList != null){
                messageList.clear();
            }

            messageList = dbHelper.queryMessage(phone.getId());

            if(recordAdapter == null){
                recordAdapter = new RecordAdapter(index);
                recordAdapter.setMessageList(messageList);
            }
            else {
                recordAdapter.setIndex(index);
                recordAdapter.setMessageList(messageList);
            }
        }
        else {                    // 通话记录
            if(recordList != null){
                recordList.clear();
            }

            recordList = dbHelper.queryRecord(phone.getId());
            Log.d(TAG, "showItemData: " + recordList);
            if(recordAdapter == null){
                recordAdapter = new RecordAdapter(index);
                recordAdapter.setRecordList(recordList);
            }
            else {
                recordAdapter.setIndex(index);
                recordAdapter.setRecordList(recordList);
            }
        }
        Log.d(TAG, "showItemData: index=" + index);
        Log.d(TAG, "showItemData: set");
        Log.d(TAG, "showItemData: " + recordAdapter);
        recyclerView.setAdapter(recordAdapter);
    }
}
