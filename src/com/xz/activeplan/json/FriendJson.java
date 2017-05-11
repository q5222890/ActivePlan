package com.xz.activeplan.json;

import java.util.ArrayList;

import org.json.JSONArray;

import com.xz.activeplan.entity.UserInfoBean;

public class FriendJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<UserInfoBean> arr = new ArrayList<UserInfoBean>() ;
		try {
			JSONArray jsonArr = new JSONArray(jsonString) ;
			for(int i = 0 ;i < jsonArr.length();i++){
				UserInfoJson userInfoJson = new UserInfoJson();
				UserInfoBean bean = (UserInfoBean)userInfoJson.analysisJson2Object(jsonArr.getString(i)) ;
				arr.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

}
