package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.MyAttendBean;
import com.xz.activeplan.utils.JsonUtils;

public class MyAttendJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		return null;
	}

	public MyAttendBean parseJson(JSONObject obj) throws Exception{
		MyAttendBean bean = new MyAttendBean() ;
		bean.setActiveid(JsonUtils.getInt("activeid", obj));
		bean.setId(JsonUtils.getInt("id", obj));
		bean.setTicket(JsonUtils.getString("ticket", obj));
		bean.setUserid(JsonUtils.getInt("userid", obj));
		return bean;
	}
}
