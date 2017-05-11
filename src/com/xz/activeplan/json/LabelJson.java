package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.LabelBean;
import com.xz.activeplan.utils.JsonUtils;

public class LabelJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		return null;
	}
	
	public LabelBean parseJson(JSONObject obj) throws Exception{
		LabelBean bean = new LabelBean() ;
		bean.setLabelid(JsonUtils.getInt("labelid", obj));
		bean.setName(JsonUtils.getString("name", obj));
		
		return bean;
	}

}
