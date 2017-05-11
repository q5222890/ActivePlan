package com.xz.activeplan.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.utils.JsonUtils;

public class UserInfoJson implements IJson {

	@Override
	public Object analysisJson2Object(String obj) {
		UserInfoBean bean = null ;
		
		try {
			 bean = JSON.parseObject(obj, UserInfoBean.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public ArrayList<UserInfoBean> parseJsonArr(String json){
		ArrayList<UserInfoBean> arr = new ArrayList<UserInfoBean>();
		JSONArray jsonArr;
		try {
			jsonArr = new JSONArray(json);
			for(int i = 0 ;i < jsonArr.length();i++){
				arr.add((UserInfoBean)analysisJson2Object(jsonArr.getString(i))) ;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arr;
	}

}
