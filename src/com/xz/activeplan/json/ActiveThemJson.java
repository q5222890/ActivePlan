package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.ActiveThemeBean;
import com.xz.activeplan.utils.JsonUtils;

public class ActiveThemJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		return null;
	}
	
	public ActiveThemeBean parseJson(JSONObject obj) throws Exception{
		ActiveThemeBean bean = new ActiveThemeBean() ;
		bean.setName(JsonUtils.getString("name", obj));
		bean.setThemeid(JsonUtils.getInt("themeid", obj)) ;
		return bean;
	}

}
