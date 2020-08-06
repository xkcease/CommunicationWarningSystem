package com.example.communicationwarningsystem.resolver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ContactResolver {
    private ContentResolver contentResolver;
    private Context context;

    public ContactResolver(Context context){
        this.context = context;
        this.contentResolver = context.getContentResolver();
    }


    // 读取联系人
    public List<Phone> readContact(){
        List<Phone> list = new ArrayList<>();

        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if(cursor != null){
            while(cursor.moveToNext()){
                Phone phone = new Phone();
                phone.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                phone.setNumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                phone.setLevel(Util.TRUST);

                list.add(phone);
            }
            cursor.close();
        }

        return list;
    }

}
