package com.xz.activeplan.json;

import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActiveinfosJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<ActiveinfosBean> arr = new ArrayList<ActiveinfosBean>();
		
		try {
			JSONArray jsonArr = new JSONArray(jsonString) ;
			for(int i = 0 ;i < jsonArr.length();i++){
				ActiveinfosBean bean = new ActiveinfosBean() ;
				bean.setActiveid(JsonUtils.getInt("activeid", jsonArr.getJSONObject(i)));
				bean.setActiveurl(JsonUtils.getString("activeurl", jsonArr.getJSONObject(i)));
				bean.setAddress(JsonUtils.getString("address", jsonArr.getJSONObject(i)));
				bean.setCategoryid(JsonUtils.getInt("categoryid", jsonArr.getJSONObject(i)));
				bean.setCharge(JsonUtils.getBoolean("charge", jsonArr.getJSONObject(i)));
				bean.setContent(JsonUtils.getString("content", jsonArr.getJSONObject(i)));
				bean.setEnddate(JsonUtils.getLong("enddate", jsonArr.getJSONObject(i)));
				bean.setFormid(JsonUtils.getInt("formid", jsonArr.getJSONObject(i)));
				bean.setGroupid(JsonUtils.getString("groupid", jsonArr.getJSONObject(i)));
				bean.setHostid(JsonUtils.getInt("hostid", jsonArr.getJSONObject(i)));
				bean.setIsend(JsonUtils.getBoolean("isend", jsonArr.getJSONObject(i)));
				bean.setLabel(JsonUtils.getString("label", jsonArr.getJSONObject(i)));
				bean.setMoney(JsonUtils.getDouble("money", jsonArr.getJSONObject(i)));
				bean.setName(JsonUtils.getString("name", jsonArr.getJSONObject(i)));
				bean.setPersonnum(JsonUtils.getInt("personnum", jsonArr.getJSONObject(i)));
				bean.setPushid(JsonUtils.getInt("pushid", jsonArr.getJSONObject(i)));
				bean.setSeenum(JsonUtils.getInt("seenum", jsonArr.getJSONObject(i)));
				bean.setStartdate(JsonUtils.getInt("startdate", jsonArr.getJSONObject(i)));
				bean.setThemeid(JsonUtils.getInt("themeid", jsonArr.getJSONObject(i)));
				bean.setUserid(JsonUtils.getInt("userid", jsonArr.getJSONObject(i))) ;
				bean.setCollectnum(JsonUtils.getInt("collectnum", jsonArr.getJSONObject(i))) ;
				bean.setIscollect(JsonUtils.getBoolean("iscollect", jsonArr.getJSONObject(i))) ;
				bean.setHosthearurl(JsonUtils.getString("hosthearurl", jsonArr.getJSONObject(i)));
				bean.setHostname(JsonUtils.getString("hostname", jsonArr.getJSONObject(i)));
				bean.setIsapply(JsonUtils.getBoolean("isapply", jsonArr.getJSONObject(i)));
				arr.add(bean) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	public ActiveinfosBean parseJson(String jsonString){
		ActiveinfosBean bean = null;
		try {
			JSONObject obj = new JSONObject(jsonString) ;
				bean = new ActiveinfosBean() ;
				bean.setActiveid(JsonUtils.getInt("activeid", obj));
				bean.setActiveurl(JsonUtils.getString("activeurl", obj));
				bean.setAddress(JsonUtils.getString("address", obj));
				bean.setCategoryid(JsonUtils.getInt("categoryid", obj));
				bean.setCharge(JsonUtils.getBoolean("charge", obj));
				bean.setContent(JsonUtils.getString("content", obj));
				bean.setEnddate(JsonUtils.getLong("enddate", obj));
				bean.setFormid(JsonUtils.getInt("formid", obj));
				bean.setGroupid(JsonUtils.getString("groupid", obj));
				bean.setHostid(JsonUtils.getInt("hostid", obj));
				bean.setIsend(JsonUtils.getBoolean("isend", obj));
				bean.setLabel(JsonUtils.getString("label", obj));
				bean.setMoney(JsonUtils.getDouble("money", obj));
				bean.setName(JsonUtils.getString("name", obj));
				bean.setPersonnum(JsonUtils.getInt("personnum", obj));
				bean.setPushid(JsonUtils.getInt("pushid", obj));
				bean.setSeenum(JsonUtils.getInt("seenum", obj));
				bean.setStartdate(JsonUtils.getInt("startdate", obj));
				bean.setThemeid(JsonUtils.getInt("themeid", obj));
				bean.setUserid(JsonUtils.getInt("userid", obj)) ;
				bean.setCollectnum(JsonUtils.getInt("collectnum", obj)) ;
				bean.setIscollect(JsonUtils.getBoolean("iscollect", obj)) ;
				bean.setHosthearurl(JsonUtils.getString("hosthearurl", obj));
				bean.setHostname(JsonUtils.getString("hostname", obj));
				bean.setIsapply(JsonUtils.getBoolean("isapply", obj));
				bean.setIssellout(JsonUtils.getBoolean("issellout", obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

}
