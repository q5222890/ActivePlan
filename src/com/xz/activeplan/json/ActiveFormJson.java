package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.ActiveFormBean;
import com.xz.activeplan.utils.JsonUtils;

public class ActiveFormJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		return null;
	}
	
	public ActiveFormBean parseJosn(JSONObject json) throws Exception{
		ActiveFormBean bean = new ActiveFormBean();
		bean.setFormid(JsonUtils.getInt("formid", json));
		bean.setName(JsonUtils.getString("name", json));
		return bean;
	}

}
