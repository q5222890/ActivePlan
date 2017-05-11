package com.xz.activeplan.json;

import java.util.ArrayList;

import org.json.JSONArray;

import com.xz.activeplan.entity.TicketListBean;
import com.xz.activeplan.utils.JsonUtils;

public class TicketListJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<TicketListBean> arr = new ArrayList<TicketListBean>();

		try {
			JSONArray jsonArr = new JSONArray(jsonString);
			for (int i = 0; i < jsonArr.length(); i++) {
				TicketListBean bean = new TicketListBean();
				
				bean.setHeadurl(JsonUtils.getString("headurl",
						jsonArr.getJSONObject(i)));
				bean.setPhonenum(JsonUtils.getString("phonenum",
						jsonArr.getJSONObject(i)));
				bean.setTicket(JsonUtils.getString("ticket",
						jsonArr.getJSONObject(i)));
				bean.setUsername(JsonUtils.getString("username",
						jsonArr.getJSONObject(i)));
				bean.setTicketcheck(JsonUtils.getBoolean("ticketcheck",
						jsonArr.getJSONObject(i)));
				
				arr.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	
}
