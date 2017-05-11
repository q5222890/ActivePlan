package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.MyCollectBean;
import com.xz.activeplan.utils.JsonUtils;

public class MyCollectJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		return null;
	}
	
	public MyCollectBean parseJson(JSONObject obj) throws Exception{
		MyCollectBean bean = new MyCollectBean() ;
		bean.setActiveid(JsonUtils.getInt("activeid", obj));
		bean.setCollectid(JsonUtils.getInt("collectid", obj));
		bean.setUserid(JsonUtils.getInt("userid", obj));
		return bean;
	}

}
