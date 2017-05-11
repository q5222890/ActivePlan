package com.xz.activeplan.utils;

import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.widget.Toast;

public class CalendarUtils {
	 //Android2.2版本以后的URL
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";
    
    
    public static void insert(Context context,String title,String address,long start,long end,int minutes){
    	 // 获取要出入的gmail账户的id
        String calId = "";
        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null, "account_name=? and account_type=?", new String[]{new String("mygmailaddress@gmail.com"),new String("com.android.exchange")}, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToLast();  //注意：是向最后一个账户添加，开发者可以根据需要改变添加事件 的账户
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));
            userCursor.close();
        }else {
            initCalendars(context);
            userCursor.close();
            userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null, "account_name=? and account_type=?", new String[]{new String("mygmailaddress@gmail.com"),new String("com.android.exchange")}, null);
            if (userCursor.getCount() > 0) {
                userCursor.moveToLast();  //注意：是向最后一个账户添加，开发者可以根据需要改变添加事件 的账户
                calId = userCursor.getString(userCursor.getColumnIndex("_id"));
                userCursor.close();
            }else{
            	userCursor.close();
            	return;
            }
        }
        
        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", "参加"+title);
        // 插入账户
        event.put("calendar_id", calId);
        event.put("eventLocation", "" + address);   

        event.put("dtstart", start);
        event.put("dtend", end);
        event.put("hasAlarm", 1);

        event.put(Events.EVENT_TIMEZONE, "Asia/Shanghai");  //这个是时区，必须有，
        //添加事件
        Uri newEvent = context.getContentResolver().insert(Uri.parse(calanderEventURL), event);        
        //事件提醒的设定
        long id = Long.parseLong(newEvent.getLastPathSegment());
        ContentValues values = new ContentValues();
        values.put("event_id", id);
        // 提前10分钟有提醒
        values.put("minutes", minutes);
        context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
        
       // Toast.makeText(context, "插入事件成功!!!", Toast.LENGTH_LONG).show();
    }
    
    @SuppressLint("NewApi")
	private static void initCalendars(Context context) {

        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(Calendars.NAME, "yy");

        value.put(Calendars.ACCOUNT_NAME, "mygmailaddress@gmail.com");
        value.put(Calendars.ACCOUNT_TYPE, "com.android.exchange");
        value.put(Calendars.CALENDAR_DISPLAY_NAME, "mytt");
        value.put(Calendars.VISIBLE, 1);
        value.put(Calendars.CALENDAR_COLOR, -9206951);
        value.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
        value.put(Calendars.SYNC_EVENTS, 1);
        value.put(Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(Calendars.OWNER_ACCOUNT, "mygmailaddress@gmail.com");
        value.put(Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(Calendars.ACCOUNT_NAME, "mygmailaddress@gmail.com")
                .appendQueryParameter(Calendars.ACCOUNT_TYPE, "com.android.exchange")    
                .build();

        context.getContentResolver().insert(calendarUri, value);
    }
    
    
    public static void update(Context context,int minutes){
    	Cursor eventCursor = context.getContentResolver().query(Uri.parse(calanderEventURL), null, "account_name=? and account_type=?", new String[]{new String("mygmailaddress@gmail.com"),new String("com.android.exchange")}, null);
    	if(eventCursor.getCount() > 0){
    		for(eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()){
    			 String event_id = eventCursor.getString(eventCursor.getColumnIndex("_id")) ;
    			 Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderRemiderURL), null,  "event_id=?", new String[]{new String(event_id)}, null);
    			 if(userCursor.getCount() > 0){
    				 for(userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()){
    					 String _id = userCursor.getString(userCursor.getColumnIndex("_id"));
    	                 ContentValues values = new ContentValues();
    	                 values.put("minutes", minutes);
    	                 
    	                 int i = context.getContentResolver().update(Uri.parse(calanderRemiderURL), values, "_id=?", new String[]{new String(_id)});
    	                 
    	                 //Toast.makeText(context, _id +"      "+ i, Toast.LENGTH_LONG).show();
    				 }
    			 }
    		}
    	}
    	
    }
}
