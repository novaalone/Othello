package com.ma.nova.othello_try_3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2015/11/7.
 */
public class RecordDB extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "Othello.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "game_record";
    public final static String GAME_ID = "game_id";
    public final static String WINNER = "game_inner";
    public final static String TIME = "game_time";
    public RecordDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_NAME + " (" + GAME_ID
                + " INTEGER primary key autoincrement, " + WINNER + " text, "+ TIME +" text);";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
    public  Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }
    //增加操作
    public long insert(String winner,String time)
    {
        SQLiteDatabase db = this.getWritableDatabase();
/* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(WINNER, winner);
        cv.put(TIME, time);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }
    //删除操作
    public void clear()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql="DROP TABLE IF EXISTS "+TABLE_NAME;

        db.execSQL(sql);
        String sql2 = "CREATE TABLE " + TABLE_NAME + " (" + GAME_ID
                + " INTEGER primary key autoincrement, " + WINNER + " text, "+ TIME +" text);";
        db.execSQL(sql2);
    }
}
