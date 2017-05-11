package com.xz.activeplan.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.utils.Utiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public class VideoCollectDao {

    private VideoCollectDBHelper helper;

    public VideoCollectDao(Context context) {
        helper = new VideoCollectDBHelper(context);   //与数据库建立连接
    }

    /**插入数据**/
    /**
     * INSERT INTO %@(liveurl, channelid, isattend,startdate,liveid,userid,categoryid,url,backurl,seetype,gambit,title,headurl,username,coverurl,fans,gcid,status,seenum) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);
     */
    public void insertDetsilNews(LiveTvBean bean) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("insert into collect" +
                "(liveurl, channelid, isattend,startdate,liveid,userid,categoryid," +
                "url,backurl,seetype,gambit,title,headurl,username," +
                "coverurl,fans,gcid,status,seenum)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[]{
                bean.getLiveurl(), bean.getChannelid(), bean.isIsattend() + "", bean.getStartdate() + "", bean.getLiveid() + "", bean.getUserid() + "", bean.getCategoryid() + ""
                , bean.getUrl(), bean.getBackurl(), bean.getSeetype(), bean.getGambit(), bean.getTitle(), bean.getHeadurl(),
                bean.getUsername(), bean.getCoverurl(),
                bean.getFans(), bean.getGcid(), bean.getStatus(), bean.getSeenum() + ""
        });
        db.close();
    }

    /***
     * 查询数据
     * <p/>
     * 传入回放的视频Id
     */

    public boolean findVideoById(String liveId) {
        boolean b= false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from collect where liveid=?", new String[]{liveId});    //只有对数据进行查询时，才用rawQuery()，增、删、改和建表，都用execSQl()
        if(cursor.moveToNext())
        {
            Utiles.log("find-------"+cursor.getString(cursor.getColumnIndex("liveid"))+"--"+db.getPath());
            b = true;
        }
        cursor.close();
        db.close();
        return b;
    }

    /**
     * 查找全部
     * 异常：
     * Make sure the Cursor is initialized correctly before accessing data from it.
     */
    public List<LiveTvBean> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from collect", null);
        List<LiveTvBean> list = new ArrayList<>();

        while (cursor!= null && cursor.moveToNext()) {
            LiveTvBean bean = new LiveTvBean();
            String string = cursor.getString(cursor.getColumnIndex("isattend"));
            if (string.equals("false")) {
                bean.setIsattend(false);
            } else {
                bean.setIsattend(true);
            }
            bean.setBackurl(cursor.getString(cursor.getColumnIndex("backurl")));
            bean.setCategoryid(cursor.getInt(cursor.getColumnIndex("categoryid")));
            bean.setChannelid(cursor.getString(cursor.getColumnIndex("channelid")));

            bean.setCoverurl(cursor.getString(cursor.getColumnIndex("coverurl")));
            bean.setFans(cursor.getString(cursor.getColumnIndex("fans")));
            bean.setGambit(cursor.getString(cursor.getColumnIndex("gambit")));
            bean.setGcid(cursor.getString(cursor.getColumnIndex("gcid")));
            bean.setHeadurl(cursor.getString(cursor.getColumnIndex("headurl")));

            bean.setLiveid(cursor.getInt(cursor.getColumnIndex("liveid")));
            bean.setLiveurl(cursor.getString(cursor.getColumnIndex("liveurl")));
            bean.setSeenum(cursor.getInt(cursor.getColumnIndex("seenum")));
            bean.setSeetype(cursor.getString(cursor.getColumnIndex("seetype")));

            bean.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
            bean.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            bean.setStartdate(cursor.getLong(cursor.getColumnIndex("startdate")));
            bean.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            bean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            bean.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 删除表结构
     */
    public void dropTable()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql="drop table if exists TBL_DETAILNEWS";
        db.execSQL(sql);
        db.close();
    }

    /**
     *  删除多条数据或一条数据
     */

    public void deleteData(List<Integer> list)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        for (int i = 0; i < list.size(); i++) {
                String sql = "delete from collect where liveid in (?)";
                db.execSQL(sql,new Object[]{list.get(i)});
        }

        db.close();

    }
}

