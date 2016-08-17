package com.myframe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myframe.config.Constant;

/**
 * Created by aaron on 16-8-14.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_READ_TABLE = "create table read_record(_id integer primary key autoincrement,newsid varchar(20))";
    private static DBHelper dbHelper;


    public DBHelper(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_READ_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public DBHelper getDBHelper(Context context){
        if(dbHelper == null){
            synchronized (DBHelper.class){
                dbHelper = new DBHelper(context);
            }
        }
        return dbHelper;
    }
}
