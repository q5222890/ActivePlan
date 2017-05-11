package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.MyFriendBean;
import com.xz.activeplan.utils.JsonUtils;

public class MyFriendJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		return null;
	}
	
	public MyFriendBean parsonJosn(JSONObject json) throws Exception{
		MyFriendBean bean = new MyFriendBean() ;
		bean.setFriendid(JsonUtils.getInt("friendid", json)) ;
		bean.setId(JsonUtils.getInt("id", json));
		bean.setUserid(JsonUtils.getInt("userid", json)) ;
		
		return bean;
	}

}
