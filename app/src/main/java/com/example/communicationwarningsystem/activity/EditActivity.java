package com.example.communicationwarningsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.communicationwarningsystem.R;
import com.example.communicationwarningsystem.db.DbHelper;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.util.Util;

public class EditActivity extends AppCompatActivity {
    public static final int CODE = 2;
    private final String TAG = "ComWa-Edit";
    Phone phone;
    DbHelper dbHelper;
    EditText nameEdit;
    EditText numberEdit;
    TextView levelText;
    EditText noteEdit;
    Integer level;
    int select;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();         // 初始化




        // 选择系数
        levelText = findViewById(R.id.text_level);
        levelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog;

                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this)
                        .setTitle("选择安全系数").setIcon(R.mipmap.ic_launcher)
                        .setSingleChoiceItems(new String[]{"Trust", "Normal", "Strange", "Risk", "Menace"}, select, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                select = which;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (select){
                                    case 0:
                                        level = Util.TRUST;
                                        levelText.setText(Util.getLevelString(Util.TRUST));
                                        break;
                                    case 1:
                                        level = Util.NORMAL;
                                        levelText.setText(Util.getLevelString(Util.NORMAL));
                                        break;
                                    case 2:
                                        level = Util.STRANGE;
                                        levelText.setText(Util.getLevelString(Util.STRANGE));
                                        break;
                                    case 3:
                                        level = Util.RISK;
                                        levelText.setText(Util.getLevelString(Util.RISK));
                                        break;
                                    case 4:
                                        level = Util.MENACE;
                                        levelText.setText(Util.getLevelString(Util.MENACE));
                                        break;
                                    default:
                                        break;
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });



        // 保存
        Button saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!numberEdit.getText().toString().equals("")){
                    if(phone == null){          // 添加
                        if(dbHelper.queryPhoneByNumber(numberEdit.getText().toString()) == null){   // 不重复
                            phone = new Phone();
                            phone.setName(nameEdit.getText().toString());
                            phone.setNumber(numberEdit.getText().toString());
                            phone.setLevel(level);
                            phone.setNote(noteEdit.getText().toString().trim());

                            dbHelper.insertPhone(phone);
                            Log.d(TAG, "onClick: insert phone");
                            setResult(RESULT_OK);
                            finish();
                        }
                        else {
                            Toast.makeText(EditActivity.this, "手机号已存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {                  // 修改
                        phone.setName(nameEdit.getText().toString());
                        phone.setLevel(level);
                        phone.setNote(noteEdit.getText().toString().trim());

                        dbHelper.updatePhone(phone);
                        Log.d(TAG, "onClick: update phone");
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("phone", phone);
                        intent.putExtras(bundle);

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                else {
                    Toast.makeText(EditActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // 返回
        ImageView backImage = findViewById(R.id.back);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }





    // 初始化数据
    private void init(){
        nameEdit = findViewById(R.id.edit_name);
        numberEdit = findViewById(R.id.edit_number);
        levelText = findViewById(R.id.text_level);
        noteEdit = findViewById(R.id.edit_note);
        dbHelper = DbHelper.getInstance(this);

        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){         // 编辑
                numberEdit.setEnabled(false);
                phone = (Phone)bundle.getSerializable("phone");
                if(phone != null){
                    nameEdit.setText(phone.getName());
                    numberEdit.setText((phone.getNumber()));
                    levelText.setText(Util.getLevelString(phone.getLevel()));
                    level = phone.getLevel();
                    noteEdit.setText(phone.getNote());
                }
            }
            else {      // 添加
                level = Util.NORMAL;
                numberEdit.setEnabled(true);
            }
        }
    }

}
