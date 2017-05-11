package com.xz.activeplan.json;

import com.xz.activeplan.entity.TicketAddBean;
import com.xz.activeplan.entity.TicketBean;
import com.xz.activeplan.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TicketAddJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<TicketAddBean> arr = new ArrayList<TicketAddBean>();

		try {
			JSONArray jsonArr = new JSONArray(jsonString);
			for (int i = 0; i < jsonArr.length(); i++) {
				TicketAddBean bean = new TicketAddBean();
				bean.setId(JsonUtils.getInt("id",
						jsonArr.getJSONObject(i)));
				bean.setActiveid(JsonUtils.getInt("activeid",
						jsonArr.getJSONObject(i)));
				bean.setIntro(JsonUtils.getString("intro",
						jsonArr.getJSONObject(i)));
				bean.setIscheck(JsonUtils.getBoolean("ischeck",
						jsonArr.getJSONObject(i)));
				bean.setMoney(JsonUtils.getDouble("money",
						jsonArr.getJSONObject(i)));
				bean.setName(JsonUtils.getString("name",
						jsonArr.getJSONObject(i)));
				bean.setPersonnum(JsonUtils.getInt("personnum",
						jsonArr.getJSONObject(i)));
				bean.setType(JsonUtils.getInt("type",
						jsonArr.getJSONObject(i)));
				bean.setIssellout(JsonUtils.getBoolean("issellout",
						jsonArr.getJSONObject(i))) ;
				bean.setSurpluspersonnum(JsonUtils.getInt("surpluspersonnum",
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
