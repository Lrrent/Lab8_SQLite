package com.example.da.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Da on 2016/11/17.
 */

public class MyDBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME= "Member";
    private static final String TABLE_NAME = "BirthMessage";
    private static final int DB_VERSION = 1;
    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);  //还没搞懂参数表示什么
    }
    @Override
    public void onCreate(SQLiteDatabase db) { //刚开始的sql语句中exists没有空格,导致建表时出错
        String CREATE_TABLE = "create table if not exists " + TABLE_NAME
                +"(_id integer primary key autoincrement,name text,birth text,gift text)";
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public long insert(Member member){   // 数据库插入操作
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",member.getName());
        contentValues.put("birth",member.getBirth());
        contentValues.put("gift",member.getGift());
        long id = db.insert(TABLE_NAME,null,contentValues); //调用insert方法进行读入
        db.close();
        Log.i("插入成功", "insert: ");
        return id;
    }
    public int delete(Member member){  //删除相应的记录
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArg = {member.getId()+""};
        int rows = db.delete(TABLE_NAME,whereClause,whereArg);
        return rows;
    }
    public long update(Member member){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArg = {member.getId()+""};
        ContentValues contentValues = new ContentValues();
        contentValues.put("birth",member.getBirth());
        contentValues.put("gift",member.getGift());
        long id = db.update(TABLE_NAME,contentValues,whereClause,whereArg);
        db.close();
        Log.i("更新成功", "update: ");
        return id;
    }
    public Cursor query(){
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME,null,null,null,null,null,null);
    }
    public boolean queryRepeat(Member member){
        //Log.i(member.getName(), "queryFromName: ");
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME,new String[]{"name"},"name =? ",new String[]{member.getName()},null,null,null).moveToFirst();
    }
}
