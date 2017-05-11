package com.xz.activeplan.json;

import org.json.JSONObject;

import com.xz.activeplan.entity.TeacherInviteBean;
import com.xz.activeplan.utils.JsonUtils;

public class TeacherInviteJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		
		return null;
	}
	
	public TeacherInviteBean parseJson(JSONObject json) throws Exception{
		TeacherInviteBean bean = new TeacherInviteBean() ;
		bean.setId(JsonUtils.getInt("id", json));
		bean.setTeacherid(JsonUtils.getInt("teacherid", json));
		bean.setUserid(JsonUtils.getInt("userid", json)) ;
		return bean;
	}

}
