package com.xz.activeplan.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtils {

	static long secondsOfHour = 60 * 60;
	static long secondsOfDay = secondsOfHour * 24;
	static long secondsOfTwoDay = secondsOfDay * 2;
	static long secondsOfThreeDay = secondsOfDay * 3;
	static SimpleDateFormat mFullDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	static SimpleDateFormat mFullDateFormat1 = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
	static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat f;


	public static String FormatsimpleDate(Date date) {
		return simpleDateFormat.format(date);
	}

	public static String getSimpleTime(String time) {

		SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		Date date = null;
		try {
			date = mFullDateFormat.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static int getMinutesBefore(String time, String now) {

		try {
			Date t = mFullDateFormat.parse(time);

			Date n = mFullDateFormat.parse(now);

			long ms = n.getTime() - t.getTime();

			return (int) (ms / (1000 * 60 * 60));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

	public static String getTime(String time){
		String t = "" ;
		try{
			Date d = new Date(Long.parseLong(time)*1000);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			t = sdf.format(d) ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		return t ;
	}

	public static String getTime1(String time){
		String t = "" ;
		try{
			Date d = new Date(Long.parseLong(time)*1000);
			SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
			t = sdf.format(d) ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		return t ;
	}

	/**
	 * time 是否大于 now
	 * @param time
	 * @param now
	 * @return
	 */
	public static boolean minutesBefore(String time, String now) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		try {
			Date t = f.parse(time);
			Date n = f.parse(now);
			long ms = t.getTime() - n.getTime();
			return ms >= 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public static String formartTaskDate(String time) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat formater = new SimpleDateFormat("MM月dd日");

		Date date = null;

		try {

			date = f.parse(time);

			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String formartTaskTime(String time) {

		SimpleDateFormat f = new SimpleDateFormat("HH:mm");

		SimpleDateFormat formater = new SimpleDateFormat("HH:mm");

		Date date = null;

		try {

			date = f.parse(time);

			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getCurrentDateString() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		return f.format(new Date(System.currentTimeMillis()));
	}

	public static String getAfterDateString(int millis) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");

		return f.format(new Date(System.currentTimeMillis() + millis));
	}

	public static String getAfterTimeString(int millis) {

		SimpleDateFormat f = new SimpleDateFormat("HH:mm");

		return f.format(new Date(System.currentTimeMillis() + millis));
	}

	public static String getCurrentTimeString() {

		SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");

		return f.format(new Date(System.currentTimeMillis()));
	}

	public static String getCurrentTimeString(int afterMinutes) {

		SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");

		return f.format(new Date(System.currentTimeMillis() + (afterMinutes * 60 * 1000)));
	}

	public static String getSimpleTimeString(String time) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		return f.format(new Date(Long.valueOf(time)));
	}

	public static String getSimpleTimeString2(String time) {

		SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm");

		return f.format(new Date(Long.valueOf(time)));
	}

	public static String formatChatTime(String time) {

		try {
			Date d = mFullDateFormat.parse(time);

			SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm:ss");

			return f.format(d);

		} catch (ParseException e) {

			e.printStackTrace();
		}

		return "";
	}

	public static String formatTime(Date date) {
		return mFullDateFormat.format(date);

	}

	public static long formatDate(String time) {
		long parse=0;
		 f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		try {
			Date date = f.parse(time);
			parse=date.getTime();
		} catch (ParseException e) {
		}
		return parse;
	}

	public static String formatTime(long ss) {

		return mFullDateFormat.format(new Date(ss));
	}

	/**
	 * 时间格式化
	 * @param ss
	 * @return
     */
	public static String formatTime1(long ss)
	{
		return mFullDateFormat1.format(new Date(ss));
	}

	public static String formatDate(long ss) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(new Date(ss));
	}
	
	public static String formatDate2(long ss) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
		return f.format(new Date(ss));
	}

	public static String getCurrentTime() {
		return mFullDateFormat.format(new Date(System.currentTimeMillis()));

	}
	
	public static Date formartToDate(String time) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		try {
			Date date = f.parse(time);

			return date;

		} catch (Exception e) {
		}
		return null;
	}

	public static String dateSelectorConvert(String text) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date date = f.parse(text);

			return mSimpleDateFormat.format(date);

		} catch (Exception e) {
		}
		return null;

	}

	// 得到简单的时间描述
	public static String getSimpleTimeDesc(String now, String target) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		SimpleDateFormat f2 = new SimpleDateFormat("yyyy年MM月dd日");

		try {
			Date nowDate = f.parse(now);

			Date targetDate = f.parse(target);

			String result = new String();

			long date = (nowDate.getTime() - targetDate.getTime()) / (24 * 60 * 60 * 1000);

			if (date < 7) {
				if (date == 0) {
					long h = (nowDate.getTime() - targetDate.getTime()) / (60 * 60 * 1000);
					if (h < 1) {
						long m = (nowDate.getTime() - targetDate.getTime()) / (60 * 1000);
						if (m < 1) {
							result = "刚刚";
						} else {
							result = m + "分钟前";
						}
					} else {
						result = h + "小时前";
					}
				} else {
					result = date + "天前";
				}
			} else {

				return f2.format(targetDate);

			}
			return result;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	@SuppressWarnings("deprecation")
	public static int getYear(String dateString) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date d = formart.parse(dateString);

			return d.getYear();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static int getYear(long dateString) {
		try{
			Date d = new Date(dateString);

			return d.getYear();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static int getMonth(String dateString) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date d = formart.parse(dateString);

			return d.getMonth();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static int getMonth(long dateString) {


		try {
			Date d = new Date(dateString);

			return d.getMonth();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	
	public static int getDayOfMonth(String dateString) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date d = formart.parse(dateString);

			return d.getDate();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	
	public static int getDayOfMonth(long dateString) {


		try {
			Date d = new Date(dateString);

			return d.getDate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	//long类型时间，或者时间字符串时分秒18:15:16都能获取到"小时"
	public static int getHours(String timeString) {
		SimpleDateFormat formart = new SimpleDateFormat("HH:mm");

		try {
			Date date = formart.parse(timeString);

			return date.getHours();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static int getMinutes(String timeString) {

		SimpleDateFormat formart = new SimpleDateFormat("HH:mm");

		try {
			Date date = formart.parse(timeString);

			return date.getMinutes();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}

	public static Date parseTime(String time) {

		try {
			Date d = mFullDateFormat.parse(time);

			return d;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Date parseTaskTime(String time) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		try {
			Date d = formart.parse(time);

			return d;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	//活动的时间格式
	public static String getActiveTime2(long time){
		String date="";
		if (time>0) {
			SimpleDateFormat formart = new SimpleDateFormat("MM/dd");
			try {
				Date d = new Date(time);
				date=formart.format(d);
				return date;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return date;
	}

	//活动的时间格式
	public static String getActiveTime(long time){
		String date="";
		if (time>0) {
			SimpleDateFormat formart = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			try {
				Date d = new Date(time);
				date=formart.format(d);
				return date;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return date;
	}

	public static String getPublishDateString(String date) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date d = f.parse(date);

			return mSimpleDateFormat.format(d);

		} catch (Exception e) {

		}
		return null;

	}

	public static Date parseBirthday(String birthday) {

		try {
			Date d = mSimpleDateFormat.parse(birthday);

			return d;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getLastUpdateTimeDesc(String time) {

		if (TextUtils.isEmpty(time)) {
			return "";
		}

		String desc = time;

		try {
			Date d = mFullDateFormat.parse(time);
			desc = formatLastUpdateTime(d);

		} catch (ParseException e) {

			e.printStackTrace();
		}

		return desc;

	}

	public static String getLastUpdateTimeDesc(long time) {
		String desc = "";

		try {
			Date d = new Date(time);
			desc = formatLastUpdateTime(d);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return desc;
	}
	//聊天的时间格式
	public static String getChatLastUpdateTimeDesc(long time) {
		String desc = "";
		
		try {
			Date d = new Date(time);
			desc = formatCahtLastUpdateTime(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return desc;
	}
	
	@SuppressWarnings("deprecation")
	private static String formatCahtLastUpdateTime(Date d) {
		String desc = "";
		Date n = new Date();

//		long delay = n.getTime() - d.getTime();
//		// 相差的秒数
//		long delaySeconds = delay / 1000;
		long todayZero=getTodayZero();
		if (d.getTime() > todayZero) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			desc = dateFormat.format(d);
		}else if(d.getTime() < todayZero&&d.getTime() > (todayZero-secondsOfDay*1000)){
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			desc ="昨天 "+dateFormat.format(d);
		}else if(d.getYear()==n.getYear()){
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
			desc =dateFormat.format(d);
		}else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
			desc =dateFormat.format(d);
		}
		return desc;
	}
	
	public static String getTime(long time){
		String desc = "";
		
		try {
			Date d = new Date(time*1000);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
			desc =dateFormat.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return desc;
	}
	
	//当天0点的时间戳
	public static long getTodayZero() {
	    Date date = new Date();     
	    long l = 24*60*60*1000; //每天的毫秒数 
	    //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。 
	    //减8个小时的毫秒值是为了解决时区的问题。
	    return (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
	}

	public static String getTodayFormat() {
		return mFullDateFormat.format(new Date());
	}

	public static String getDayFormat(long l) {
		String dates = "";
		SimpleDateFormat ydf = new SimpleDateFormat("yyyyMM");
		String yd = ydf.format(new Date(l));
		String nyd = ydf.format(new Date());
		if (nyd.equals(yd)) {
			SimpleDateFormat ddf = new SimpleDateFormat("dd");
			int dd = Integer.parseInt(ddf.format(new Date(l)));
			Log.i("d", "day:" + dd);
			int nd = Integer.parseInt(ddf.format(new Date()));
			if (dd == nd) {
				dates = "今天";
			} else if ((dd + 1) == nd) {
				dates = "昨天";
			} else {
				dates = "custom";
			}
		} else {
			dates = "custom";
		}
		return dates;
	}

	private static String formatLastUpdateTime(Date d) {

		String desc = "";

		Date n = new Date();

		long delay = n.getTime() - d.getTime();

		// 相差的秒数
		long delaySeconds = delay / 1000;

		if (delaySeconds < 10) {
			desc = "刚刚";
		} else if (delaySeconds <= 60) {
			desc = delaySeconds + "秒钟前";
		} else if (delaySeconds < secondsOfHour) {
			desc = (delaySeconds / 60) + "分钟前";
		} else if (delaySeconds < secondsOfDay) {
			desc = (delaySeconds / 3600) + "小时前";
		} else if (delaySeconds < secondsOfTwoDay) {
			desc = "一天前";
		} else if (delaySeconds < secondsOfThreeDay) {
			desc = "两天前";
		} else if (n.getYear() == d.getYear()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
			desc = dateFormat.format(d);
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			desc = format.format(d);
		}

		return desc;
	}

	public static String compareDate(Calendar calender, long serviceTime, long createTime) {
		calender.setTimeInMillis(serviceTime);

		int year1 = calender.get(Calendar.YEAR);
		int month1 = calender.get(Calendar.MONTH);
		int day1 = calender.get(Calendar.DATE);

		calender.setTimeInMillis(createTime);

		int year2 = calender.get(Calendar.YEAR);
		int month2 = calender.get(Calendar.MONTH);
		int day2 = calender.get(Calendar.DATE);

		if (year1 == year2 && month1 == month2) {
			if (day1 == day2) {
				return "今天";
			} else if (day2 + 1 == day1) {
				return "昨天";
			}
		}
		return null;
	}

	public static String getDate(String unixDate) {
		SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat fm2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long unixLong = 0;
		String date = "";
		try {
			unixLong = Long.parseLong(unixDate);
		} catch (Exception ex) {
			System.out.println("String转换Long错误，请确认数据可以转换！");
		}
		try {
			date = fm1.format(unixLong);
			date = fm2.format(new Date(date));
		} catch (Exception ex) {
			System.out.println("String转换Date错误，请确认数据可以转换！");
		}

		return date;
	}

	public static boolean isBiger(String arg1, String now) {
		int s = arg1.length();
		long old = 0;
		// if(s<13){
		// old=Long.parseLong(arg1)*1000;
		// }
		old = Long.parseLong(arg1);
		long nows = Long.parseLong(now);
		return old > nows ? true : false;
	}
	
	//日期转为时间戳(年、月、日、时、分)
		public static long getLongTime(String da){
			long d=0;
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");
//				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
//				String dateString = "2011/07/29 14:50:11";
				Date date = df.parse(da);
				 d=date.getTime();
				 String str=String.valueOf(d);
				 d=Long.parseLong(str.substring(0, 10));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return d;
			
		}

	/**
	 * 时间 10 位
	 * 将string转换为long
	 *
     */
		public static long getLongTime1(String da){
			long d=0;
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
				Date date = df.parse(da);
				 d=date.getTime();
				 String str=String.valueOf(d);
				 d=Long.parseLong(str.substring(0, 10));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return d;
			
		}

	/**
	 * 时间 13 位
	 * 将string转换为long
	 */
	public static long getLongTime13(String da){
		long d=0;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
			Date date = df.parse(da);
			d=date.getTime();
			String str=String.valueOf(d);
			d=Long.parseLong(str.substring(0, 13));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return d;
	}
		//日期转化
		public static long getLongTimes(String da){
			long d=0;
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
				Date date = df.parse(da);
				 d=date.getTime();
				 String str=String.valueOf(d);
				 d=Long.parseLong(str.substring(0, str.length()-3));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return d;
			
		}
		//日期转化
		public static long getLongTimems(String da){
			long d=0;
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM");
				Date date = df.parse(da);
				d=date.getTime();
				String str=String.valueOf(d);
				d=Long.parseLong(str.substring(0, str.length()-3));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return d;
			
		}
		
		//日期转为时间戳(年、月、日、时分秒)
		public static long getFullLongTime(String da){
			long d=0;
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
//				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");
//				String dateString = "2011/07/29 14:50:11";
				Date date = df.parse(da);
				d=date.getTime();
				String str=String.valueOf(d);
				d=Long.parseLong(str.substring(0, 10));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return d;
			
		}
		//日期转为时间戳(年、月、日、时分秒)
				public static long getFullLongTimes(String da){
					long d=0;
					try {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//						SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");
//						String dateString = "2011/07/29 14:50:11";
						Date date = df.parse(da);
						d=date.getTime();
						String str=String.valueOf(d);
						d=Long.parseLong(str.substring(0, 10));
					} catch (Exception e) {
						// TODO: handle exception
					}
					return d;
					
				}
		
		/**隐藏日期中的"日"*/
		@SuppressLint("NewApi")
		public static void hideview(DatePicker datePicker){
//            DatePickerdatePicker = new DatePicker(MainActivity.this);
            datePicker.setCalendarViewShown(false);

            //通过反射机制，访问private的mDaySpinner成员，并隐藏它
            try {
                Field daySpinner =datePicker.getClass().getDeclaredField("mDaySpinner");
                daySpinner.setAccessible(true);
                ((View)daySpinner.get(datePicker)).setVisibility(View.GONE);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
		}
		
		/**隐藏日期中的"年"*/
		@SuppressLint("NewApi")
		public static void hideYearView(DatePicker datePicker){
//            DatePickerdatePicker = new DatePicker(MainActivity.this);
            datePicker.setCalendarViewShown(false);

            //通过反射机制，访问private的mDaySpinner成员，并隐藏它
            try {
                Field yearSpinner =datePicker.getClass().getDeclaredField("mYearSpinner");
                yearSpinner.setAccessible(true);
                ((View)yearSpinner.get(datePicker)).setVisibility(View.GONE);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
		}

	/**
	 * 得到时间差---天---小时---分---秒
	 */
	public static String getDataFifference(Date endTime){
		SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//当前系统时间
		String format = formatTime.format(new Date(System.currentTimeMillis()));
		long between = 0;
		try {
			java.util.Date begin = formatTime.parse(formatTime.format(endTime));
			java.util.Date end = formatTime.parse(format);
			between = (begin.getTime() - end.getTime());// 得到两者的毫秒数
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		long day = between / (24 * 60 * 60 * 1000);
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
				- min * 60 * 1000 - s * 1000);
		Utiles.log(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
				+ "毫秒");
		return day +"/"+hour+"/"+min+"/"+s;
	}
}
