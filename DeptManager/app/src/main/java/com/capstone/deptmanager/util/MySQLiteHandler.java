package com.capstone.deptmanager.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.capstone.deptmanager.model.NotiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daehee on 2017. 11. 11..
 */

public class MySQLiteHandler {

    private MySQLiteOpenHelper helper;
    private Context context;

    public MySQLiteHandler(Context context) {
        // 데이터베이스 초기화 작업
        this.context = context;
        helper = new MySQLiteOpenHelper(context, "deptmanager.db", null, 1); // null : 표준커서팩토리 사용
        //  제어권자,   파일명,      커서팩토리, 버전
    } // Constructor


    /**
     * 앱 내부에서 SQLite가 사용 되는 '알림목록 확인 기능'에 맞는 쿼리들을 함수로 정의하여 사용한다.
     */

    // 알림목록을 가져오는 메서드
    public List<NotiBean> selectAll() {

        SQLiteDatabase db = helper.getReadableDatabase();

//		db.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal);
        Cursor c = db.query("noti", null, null, null, null, null, null);

        List<NotiBean> list = new ArrayList<>();
        while (c.moveToNext()) {
            int no = c.getInt(c.getColumnIndex("_id")); // 0 을 직접 넣는 것 보다 더 좋은(유연한) 방식의 코드 (테이블이 변경될 수도 있음 !)
            String title = c.getString(c.getColumnIndex("title"));
            String msg = c.getString(c.getColumnIndex("msg"));;
            String date = c.getString(c.getColumnIndex("date"));;
            int read = c.getInt(c.getColumnIndex("read"));
            list.add(new NotiBean(no, title, msg, date));
            //String str = title + "," + msg + "," + date;
            //Log.d("MyLog", str);
        } // end of while
        return list;

    } // end of selectAll

    // 알림 수신 시 DB에 저장하는 메서드
    public int insert(String title, String msg, String date, int read) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("msg", msg);
        values.put("date", date);
        values.put("read", read);
        long result = db.insert("noti", null, values);
//		db.execSQL(sql); // db 명령을 수행후 결과를 리턴받지 못함 -> ContentValues 객체 생성 후 insert 메서드를 이용하여 데이터를 추가하는 것이 바람직함

        String str = "insert : " + result + "번째 row insert 성공";
        Log.d("sqlite", str);

        //if (result == -1) Toast.makeText(context, "튜플 입력을 실패하였습니다", Toast.LENGTH_SHORT).show();
        //else Toast.makeText(context, "튜플 입력 성공", Toast.LENGTH_SHORT).show();

        return (int) result;
    } // end of insert

    // 알림 확인 시

} // end of class
