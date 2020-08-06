package com.example.communicationwarningsystem.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.communicationwarningsystem.entity.Message;
import com.example.communicationwarningsystem.entity.Phone;
import com.example.communicationwarningsystem.entity.Record;
import com.example.communicationwarningsystem.entity.Statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static DbHelper dbHelper;
    private final String TAG = "ComWa";


    private DbHelper(@Nullable Context context) {
        super(context, "comuwarnsys.db", null, 1);
        db = this.getWritableDatabase();
    }


    // 单例
    public static DbHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new DbHelper(context);
        }
        return dbHelper;
    }


    // 添加手机
    public boolean insertPhone(Phone phone){
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", phone.getNumber());
        contentValues.put("level", phone.getLevel());
        contentValues.put("name", phone.getName());
        contentValues.put("note", phone.getNote());

        return db.insert("phone", null, contentValues) > 0;
    }

    // 添加短信
    public boolean insertMessage(Message message){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", message.getId());
        contentValues.put("content", message.getContent());
        contentValues.put("date", message.getDate());

        return db.insert("message", null, contentValues) > 0;
    }


    // 添加通话记录
    public boolean insertRecord(Record record){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", record.getId());
        contentValues.put("state", record.getState());
        contentValues.put("duration", record.getDuration());
        contentValues.put("date", record.getDate());
        contentValues.put("filename", record.getFilename());

        return db.insert("record", null, contentValues) > 0;
    }


    // 添加统计
    public boolean insertStatistics(Statistics statistics){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", statistics.getId());
        contentValues.put("msgc", statistics.getMsgCount());
        contentValues.put("misc", statistics.getMissedCount());
        contentValues.put("recec", statistics.getReceivedCount());
        contentValues.put("diac", statistics.getDialledCount());

        return db.insert("statistics", null, contentValues) > 0;
    }


    // 修改手机
    public boolean updatePhone(Phone phone){
        ContentValues contentValues = new ContentValues();
        contentValues.put("level", phone.getLevel());
        contentValues.put("name", phone.getName());
        contentValues.put("note", phone.getNote());

        return db.update("phone", contentValues, "id=?", new String[]{phone.getId().toString()}) > 0;
    }

    // 修改统计
    public boolean updateStatistics(Statistics statistics){
        ContentValues contentValues = new ContentValues();
        contentValues.put("msgc", statistics.getMsgCount());
        contentValues.put("misc", statistics.getMissedCount());
        contentValues.put("recec", statistics.getReceivedCount());
        contentValues.put("diac", statistics.getDialledCount());

        return db.update("statistics", contentValues, "id=?", new String[]{statistics.getId().toString()}) > 0;
    }



    // 删除
    public boolean deleteData(Serializable serializable, String table){
        if(table.equals("phone")){
            Phone obj = (Phone)serializable;
            return db.delete(table, "id=?", new String[]{obj.getId().toString()}) > 0;
        }
        else if (table.equals("message")){
            Message obj = (Message)serializable;
            return db.delete(table, "id=?", new String[]{obj.getId().toString()}) > 0;
        }
        else if (table.equals("record")){
            Record obj = (Record)serializable;
            return db.delete(table, "id=?", new String[]{obj.getId().toString()}) > 0;
        }
        else if (table.equals("statistics")){
            Statistics obj = (Statistics)serializable;
            return db.delete(table, "id=?", new String[]{obj.getId().toString()}) > 0;
        }
        else {
            return false;
        }
    }


    // 查询手机
    public List<Phone> queryPhone(){
        List<Phone> list = new ArrayList<Phone>();

        Cursor cursor = db.rawQuery("select * from phone", null);
        if(cursor != null){
            while(cursor.moveToNext()){
                Phone phone = new Phone();
                phone.setId(cursor.getInt(cursor.getColumnIndex("id")));
                phone.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                phone.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                phone.setName(cursor.getString(cursor.getColumnIndex("name")));
                phone.setNote(cursor.getString(cursor.getColumnIndex("note")));

                list.add(phone);
            }
            cursor.close();
        }

        return list;
    }


    // 查询手机
    public List<Phone> queryPhoneByLevel(Integer level){
        List<Phone> list = new ArrayList<Phone>();

        Cursor cursor = db.rawQuery("select * from phone where level=?", new String[]{level.toString()});
        if(cursor != null){
            while(cursor.moveToNext()){
                Phone phone = new Phone();
                phone.setId(cursor.getInt(cursor.getColumnIndex("id")));
                phone.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                phone.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                phone.setName(cursor.getString(cursor.getColumnIndex("name")));
                phone.setNote(cursor.getString(cursor.getColumnIndex("note")));

                list.add(phone);
            }
            cursor.close();
        }

        return list;
    }

    // 查询按id手机
    public Phone queryPhoneByNumber(String number){
        Phone phone = null;

        Cursor cursor = db.rawQuery("select * from phone where number=?", new String[]{number});
        if(cursor != null){
            while(cursor.moveToNext()){
                phone = new Phone();
                phone.setId(cursor.getInt(cursor.getColumnIndex("id")));
                phone.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                phone.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                phone.setName(cursor.getString(cursor.getColumnIndex("name")));
                phone.setNote(cursor.getString(cursor.getColumnIndex("note")));
            }
            cursor.close();
        }

        return phone;
    }


    // 查询短信
    public List<Message> queryMessage(Integer id){
        List<Message> list = new ArrayList<Message>();

        Cursor cursor = db.rawQuery("select * from message where id=?", new String[]{id.toString()});
        if(cursor != null){
            while(cursor.moveToNext()){
                Message message = new Message(id);
                message.setId(cursor.getInt(cursor.getColumnIndex("id")));
                message.setContent(cursor.getString(cursor.getColumnIndex("content")));
                message.setDate(cursor.getString(cursor.getColumnIndex("date")));

                list.add(message);
            }
            cursor.close();
        }

        return list;
    }



    // 查询通话记录
    public List<Record> queryRecord(Integer id){
        List<Record> list = new ArrayList<Record>();

        Cursor cursor = db.rawQuery("select * from record where id=?", new String[]{id.toString()});
        if(cursor != null){
            while(cursor.moveToNext()){
                Record record = new Record(id);
                record.setId(cursor.getInt(cursor.getColumnIndex("id")));
                record.setState(cursor.getInt(cursor.getColumnIndex("state")));
                record.setDuration(cursor.getLong(cursor.getColumnIndex("duration")));
                record.setDate(cursor.getString(cursor.getColumnIndex("date")));
                record.setFilename(cursor.getString(cursor.getColumnIndex("filename")));

                list.add(record);
            }
            cursor.close();
        }

        return list;
    }


    // 查询统计
    public Statistics queryStatistics(Integer id){
        Statistics statistics = new Statistics(id);

        Cursor cursor = db.rawQuery("select * from statistics where id=?", new String[]{id.toString()});
        if(cursor != null){
            while(cursor.moveToNext()) {
                statistics.setId(cursor.getInt(cursor.getColumnIndex("id")));
                statistics.setMsgCount(cursor.getInt(cursor.getColumnIndex("msgc")));
                statistics.setMissedCount(cursor.getInt(cursor.getColumnIndex("misc")));
                statistics.setReceivedCount(cursor.getInt(cursor.getColumnIndex("recec")));
                statistics.setDialledCount(cursor.getInt(cursor.getColumnIndex("diac")));

                cursor.close();
            }
        }

        return statistics;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_PHONE = "create table phone(" +
                "id integer primary key autoincrement," +
                "number text," +
                "level integer," +
                "name text," +
                "note text)";

        final String CREATE_MESSAGE = "create table message(" +
                "id integer," +
                "content text," +
                "date text)";

        final String CREATE_RECORD = "create table record(" +
                "id integer," +
                "state integer," +
                "duration integer," +
                "date text," +
                "filename text)";

        final String CREATE_STATISTICS = "create table statistics(" +
                "id integer primary key," +
                "msgc integer," +
                "misc integer," +
                "recec integer," +
                "diac integer)";

        db.execSQL(CREATE_PHONE);
        db.execSQL(CREATE_MESSAGE);
        db.execSQL(CREATE_RECORD);
        db.execSQL(CREATE_STATISTICS);

        Log.d(TAG, "onCreate: create tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
