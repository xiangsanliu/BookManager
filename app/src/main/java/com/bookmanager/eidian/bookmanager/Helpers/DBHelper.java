package com.bookmanager.eidian.bookmanager.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by xiang on 2017/1/19.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String CREATE_COURSE = "create table course (" +
            "courseId integer primary key, " +
            "courseName text, " +
            "courseType text, " +
            "courseTeacher text, " +
            "startWeek integer, " +
            "endWeek integer, " +
            "weekType integer, " +
            "locX integer, " +
            "locY integer)";

    DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSE);
        Log.d("DateBaseHelper", "数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
