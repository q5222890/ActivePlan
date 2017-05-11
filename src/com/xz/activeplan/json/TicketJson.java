package com.xz.activeplan.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xz.activeplan.entity.TicketBean;
import com.xz.activeplan.utils.JsonUtils;

public class TicketJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<TicketBean> arr = new ArrayList<TicketBean>();

		try {
			JSONArray jsonArr = new JSONArray(jsonString);
			for (int i = 0; i < jsonArr.length(); i++) {
				TicketBean bean = new TicketBean();
				bean.setActiveid(JsonUtils.getInt("activeid",
						jsonArr.getJSONObject(i)));
				bean.setActiveurl(JsonUtils.getString("activeurl",
						jsonArr.getJSONObject(i)));
				bean.setAddress(JsonUtils.getString("address",
						jsonArr.getJSONObject(i)));
				bean.setCharge(JsonUtils.getBoolean("charge",
						jsonArr.getJSONObject(i)));
				bean.setEnddate(JsonUtils.getLong("enddate",
						jsonArr.getJSONObject(i)));
				bean.setId(JsonUtils.getInt("id", jsonArr.getJSONObject(i)));
				bean.setName(JsonUtils.getString("name",
						jsonArr.getJSONObject(i)));
				bean.setStartdate(JsonUtils.getLong("startdate",
						jsonArr.getJSONObject(i)));
				bean.setTicket(JsonUtils.getString("ticket",
						jsonArr.getJSONObject(i)));
				bean.setUserid(JsonUtils.getInt("userid",
						jsonArr.getJSONObject(i)));
				arr.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	public TicketBean parseJson(String jsonString) {
		TicketBean bean = null;
		try {
			bean = new TicketBean();
			JSONObject obj = new JSONObject(jsonString) ;
			bean.setActiveid(JsonUtils.getInt("activeid",
					obj));
			bean.setActiveurl(JsonUtils.getString("activeurl",
					obj));
			bean.setAddress(JsonUtils.getString("address",
					obj));
			bean.setCharge(JsonUtils.getBoolean("charge",
					obj));
			bean.setEnddate(JsonUtils.getLong("enddate",
					obj));
			bean.setId(JsonUtils.getInt("id", obj));
			bean.setName(JsonUtils.getString("name", obj));
			bean.setStartdate(JsonUtils.getLong("startdate",
					obj));
			bean.setTicket(JsonUtils.getString("ticket",
					obj));
			bean.setUserid(JsonUtils.getInt("userid", obj));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bean;
	}

}
