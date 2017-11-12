package com.capstone.deptmanager.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by daehee on 2017. 11. 11..
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    } // end of Constructor

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성 코드
//        String sql = "create table student (name text, age integer, address text);";
        String sql = "create table noti (_id integer primary key autoincrement, title text, msg text, date text, read integer);";
        db.execSQL(sql);
    } // end of onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블이 변경되었을 때 호출되는 콜백 메서드,
        if (oldVersion == 1 && newVersion == 2) {
            // 테이블 변경작업을 해야한다
            // 가능하면 처음에 테이블 디자인을 잘 해야한다
        }
        String sql = "drop table if exist noti;";
        db.execSQL(sql);

        onCreate(db); // 테이블 다시 생성
    } // end of onUpgrade
} // end of class
