package com.xz.activeplan.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 视频收藏数据库
 */
public class VideoCollectDBHelper extends SQLiteOpenHelper {

    private static final String DBNAME="collect.db";         //数据库名称  collect.db
    private static final int VERSION=2;                     //设置版本号 1

    /***
     *CREATE TABLE IF NOT EXISTS %@(id INTEGER PRIMARY KEY, liveurl text, channelid text, isattend bit, startdate bigint, liveid bigint, userid bigint, categoryid int, url text, backurl text, seetype text, gambit text, title text, headurl text, username text, coverurl text, fans text, gcid text, status int, seenum bigint)
     */
    public VideoCollectDBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         String sql = "CREATE TABLE IF NOT EXISTS collect(id INTEGER PRIMARY KEY, liveurl text, channelid text, isattend text, startdate text, liveid bigint, userid text, categoryid text, url text, backurl text, seetype text, gambit text, title text, headurl text, username text, coverurl text, fans text, gcid text, status text, seenum text)";
         db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql2="drop table if exists collect";
        db.execSQL(sql2);    //创建
        onCreate(db);
    }
}
