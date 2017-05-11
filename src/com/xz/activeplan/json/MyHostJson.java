package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.MyHostBean;
import com.xz.activeplan.utils.JsonUtils;

public class MyHostJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		return null;
	}
	
	public MyHostBean parseJson(JSONObject json) throws Exception{
		MyHostBean bean = new MyHostBean() ;
		bean.setHostid(JsonUtils.getInt("hostid", json));
		bean.setId(JsonUtils.getInt("id", json));
		bean.setUserid(JsonUtils.getInt("userid", json)) ;
		
		return bean;
	}

}
