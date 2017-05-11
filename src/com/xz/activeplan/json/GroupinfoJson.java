package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.GroupinfoBean;
import com.xz.activeplan.utils.JsonUtils;

public class GroupinfoJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		return null;
	}
	
	public GroupinfoBean parseJson(JSONObject obj) throws Exception{
		GroupinfoBean bean = new GroupinfoBean() ;
		bean.setGroupid(JsonUtils.getInt("groupid", obj));
		bean.setGroupname(JsonUtils.getString("groupname", obj));
		bean.setIsdisturb(JsonUtils.getBoolean("isdisturb", obj));
		bean.setNotice(JsonUtils.getString("notice", obj));
		return bean;
	}

}
