package com.xz.activeplan.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
	
	public static Double getDouble(String name,String param) throws Exception {
		try {
			JSONObject obj = new JSONObject(param);
			if(obj.isNull(name)){
				return null;
			}else{
				return obj.getDouble(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONObject getJSONObject(String param) throws Exception {
		try {
			JSONObject obj = new JSONObject(param);
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONObject getJSONObject(String name,String param) throws Exception {
		try {
			JSONObject obj = new JSONObject(param);
			if(obj.isNull(name)){
				return null;
			}else{
				return obj.getJSONObject(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONArray getJSONArray(String name,String param) throws Exception {
		try {
			JSONObject obj = new JSONObject(param);
			if(obj.isNull(name)){
				return null;
			}else{
				return obj.getJSONArray(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static int getInt(String name,String param) throws Exception {
		try {
			JSONObject obj = new JSONObject(param);
			if(obj.isNull(name)){
				return -1;
			}else{
				return obj.getInt(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static boolean checkHas(String name,String param) throws Exception {
		try {
			JSONObject obj = new JSONObject(param);
			return obj.has(name);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getString(String name,String param) throws Exception {
		try {
			JSONObject obj = new JSONObject(param);
			if(obj.isNull(name)){
				return null;
			}else{
				return obj.getString(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean getBoolean(String name,String param) throws Exception {
		try {
			JSONObject obj = new JSONObject(param);
			if(obj.isNull(name)){
				return false;
			}else{
				return obj.getBoolean(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getString(String name,JSONObject obj) throws Exception {
		try {
			if(obj.isNull(name)){
				return null;
			}else{
				return obj.getString(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getInt(String name,JSONObject obj) throws Exception {
		try {
			if(obj.isNull(name)){
				return 0;
			}else{
				return obj.getInt(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static double getDouble(String name,JSONObject obj) throws Exception {
		try {
			if(obj.isNull(name)){
				return 0;
			}else{
				return obj.getDouble(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static boolean getBoolean(String name,JSONObject obj) throws Exception {
		try {
			if(obj.isNull(name)){
				return false;
			}else{
				return obj.getBoolean(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static long getLong(String name,JSONObject obj) throws Exception {
		try {
			if(obj.isNull(name)){
				return 0;
			}else{
				return obj.getLong(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static long getLong(String name,String param) throws Exception {
		try {
			JSONObject obj = new JSONObject(param);
			if(obj.isNull(name)){
				return 0;
			}else{
				return obj.getLong(name);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
