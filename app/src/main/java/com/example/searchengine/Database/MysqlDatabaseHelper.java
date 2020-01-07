package com.example.searchengine.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.Nullable;

public class MysqlDatabaseHelper extends SQLiteOpenHelper{

    private static final String SMARTWATCH_DATABASE = "smartwatch";
    private static final String ACTIVFIT_TABLE = "activfit";
    private static final String ACTIVITY_TABLE = "activity";
    private static final String BATTERY_TABLE = "batterysensor";
    private static final String BLUETOOTH_TABLE = "bluetooth";
    private static final String HEARTRATE_TABLE = "heartrate";


    public MysqlDatabaseHelper(@Nullable Context context) {
        super(context, SMARTWATCH_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ACTIVFIT_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,sensor_name TEXT,start_time TEXT,end_time TEXT,activity TEXT,duration TEXT)");
        db.execSQL("create table " + ACTIVITY_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,sensor_name TEXT,timestamp TEXT,time_stamp TEXT,step_counts TEXT,step_delta TEXT)");
        db.execSQL("create table " + BATTERY_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,sensor_name TEXT,timestamp TEXT,percent TEXT,charging TEXT)");
        db.execSQL("create table " + BLUETOOTH_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,sensor_name TEXT,timestamp TEXT,state TEXT)");
        db.execSQL("create table " + HEARTRATE_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,sensor_name TEXT,timestamp TEXT,bpm TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ACTIVFIT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ACTIVITY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+BATTERY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+BLUETOOTH_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+HEARTRATE_TABLE);
        onCreate(db);
    }


    /**
     * search data, based on the table, field, and value
     * @param table
     * @param field
     * @param value
     * @return
     */
    public Cursor getData(String table, String field, String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+table +" " +
                "where " + field + " like '%"+value+"%'",null);
        return res;
    }

    /**
     * insert data into table
     * @param table
     * @param contentValues
     * @return
     */
    public boolean insertData(String table, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(table,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    /**
     * check if the db is empty
     * @return
     */
    public boolean isEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + ACTIVFIT_TABLE, null);
        return !res.moveToNext();
    }


    /**
     * get tables' name of this db
     */
    public void getAllTables(){
        ArrayList<String> tableNames = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        while(cursor.moveToNext()){
            String tableName = cursor.getString(0);
            if(tableName.equals("android_metadata")){
                continue;
            }else{
                tableNames.add(tableName);
            }
        }

        System.out.println(Arrays.toString(tableNames.toArray()));
    }

}
