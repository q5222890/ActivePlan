package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.GroupMemberBean;
import com.xz.activeplan.utils.JsonUtils;

public class GroupMemberJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		return null;
	}
	
	public GroupMemberBean parseJosn(JSONObject obj) throws Exception{
		GroupMemberBean bean = new GroupMemberBean() ;
		bean.setGroupid(JsonUtils.getInt("groupid", obj));
		bean.setHeadurl(JsonUtils.getString("headurl", obj));
		bean.setId(JsonUtils.getInt("id", obj));
		bean.setUserid(JsonUtils.getInt("userid", obj));
		bean.setUsername(JsonUtils.getString("username", obj));
		return bean;
	}

}
