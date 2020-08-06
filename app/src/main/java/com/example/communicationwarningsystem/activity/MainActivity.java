package com.example.communicationwarningsystem.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;




import com.example.communicationwarningsystem.R;
import com.example.communicationwarningsystem.adapter.MainAdapter;
import com.example.communicationwarningsystem.db.DbHelper;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.entity.Statistics;
import com.example.communicationwarningsystem.resolver.ContactResolver;
import com.example.communicationwarningsystem.util.Util;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "ComWa-Main";
    private List<Phone> list;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private DbHelper dbHelper;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        createNotificationChannel();

        init();


        // 添加
        final ImageView menuImage = findViewById(R.id.menu);
        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(menuImage);
            }
        });

    }


    // 菜单
    private void showPopupMenu(ImageView menu){
        PopupMenu popupMenu = new PopupMenu(this, menu);
        popupMenu.inflate(R.menu.main_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.add:          // 添加联系人
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        startActivityForResult(intent, EditActivity.CODE);
                        return true;
                    case R.id.sync:         // 同步通讯录
                        readContact();
                        return true;
                    default:
                        Log.d(TAG, "onMenuItemClick: nothing");
                        return false;
                }
            }
        });
        popupMenu.show();
    }



    // 初始化环境
    private void init(){
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

            dbHelper = DbHelper.getInstance(this);
        showItemData(Util.TRUST);

        TextView trustText = findViewById(R.id.text_trust);
        trustText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemData(Util.TRUST);
            }
        });

        TextView normalText = findViewById(R.id.text_normal);
        normalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemData(Util.NORMAL);
            }
        });

        TextView strangeText = findViewById(R.id.text_strange);
        strangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemData(Util.STRANGE);
            }
        });

        TextView riskText = findViewById(R.id.text_risk);
        riskText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemData(Util.RISK);
            }
        });

        TextView menaceText = findViewById(R.id.text_menace);
        menaceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemData(Util.MENACE);
            }
        });

        // 进入详细页面
        mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Phone phone = list.get(position);
                Statistics statistics = dbHelper.queryStatistics(phone.getId());

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("phone", phone);
                bundle.putSerializable("statistics", statistics);
                intent.putExtras(bundle);

                startActivityForResult(intent, DetailActivity.CODE);
            }
        });
    }



    // 读取通讯录
    private void readContact(){
        ContactResolver contactResolver = new ContactResolver(this);
        List<Phone> dbList = dbHelper.queryPhone();
        List<Phone> contactList = contactResolver.readContact();

        List<Phone> subList = Util.subtraction(dbList, contactList);        // 求新联系人
        for(Phone phone : subList){
            dbHelper.insertPhone(phone);        // 添加
            dbHelper.insertStatistics(new Statistics(phone.getId()));
        }

        if(subList.size() != 0){        // 刷新数据
            showItemData(Util.TRUST);
        }
    }




    // 申请权限
    private void requestPermission(){
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.RECEIVE_SMS, Manifest.permission.RECORD_AUDIO};
        int count = 0;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                count++;
            }
        }

        if(count > 0){
            ActivityCompat.requestPermissions(this, permissions, 10);
            Log.d(TAG, "requestPermission: request permission");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 10){
            for(int i = 0 ; i < permissions.length; i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: request permission fail");
                    finish();
                }
                else{
                    Log.d(TAG, "onRequestPermissionsResult: request permission clear");
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            showItemData(Util.TRUST);
        }
    }


    //创建通知Channel
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        //API 26+才需要创建通知Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channelName";
            String description = "channelDesc";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            //注册channel
            notificationManager.createNotificationChannel(channel);
        }
    }


    // 展示数据
    private void showItemData(Integer level){
        if(list != null){
            list.clear();
        }

        list = dbHelper.queryPhoneByLevel(level);

        if(mainAdapter == null){
            mainAdapter = new MainAdapter(list);
        }
        else {
            mainAdapter.setList(list);
        }

        recyclerView.setAdapter(mainAdapter);
    }

}

















