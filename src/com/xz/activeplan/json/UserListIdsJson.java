package com.xz.activeplan.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.utils.JsonUtils;

public class UserListIdsJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<UserInfoBean> listIds = new ArrayList<UserInfoBean>();
		JSONObject obj;
		try {
			obj = new JSONObject(jsonString);
			JSONArray jsonArray = obj.getJSONArray("users") ;
			for(int i = 0 ;i < jsonArray.length();i++){
				UserInfoBean bean = new UserInfoBean() ;
				bean.setUserid(JsonUtils.getInt("id", jsonArray.getJSONObject(i)));
				bean.setUsername("test - " + i) ;
				bean.setHeadurl("http://www.52ij.com/uploads/allimg/160317/0Q91622b-5.jpg") ;
				
				listIds.add(bean) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return listIds;
	}

}
