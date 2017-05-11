package com.xz.activeplan.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xz.activeplan.entity.OrganizersBean;
import com.xz.activeplan.utils.JsonUtils;

public class OrganizersJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<OrganizersBean> arr = new ArrayList<OrganizersBean>();

		try {
			JSONArray jsonArr = new JSONArray(jsonString);
			for (int i = 0; i < jsonArr.length(); i++) {
				OrganizersBean bean = new OrganizersBean();
				bean.setFollownum(JsonUtils.getInt("follownum",
						jsonArr.getJSONObject(i)));
				bean.setHostcontact(JsonUtils.getString("hostcontact",
						jsonArr.getJSONObject(i)));
				bean.setHosthearurl(JsonUtils.getString("hosthearurl",
						jsonArr.getJSONObject(i)));
				bean.setHostid(JsonUtils.getInt("hostid",
						jsonArr.getJSONObject(i)));
				bean.setHostintro(JsonUtils.getString("hostintro",
						jsonArr.getJSONObject(i)));
				bean.setHostmail(JsonUtils.getString("hostmail",
						jsonArr.getJSONObject(i)));
				bean.setHostname(JsonUtils.getString("hostname",
						jsonArr.getJSONObject(i)));
				bean.setHostphone(JsonUtils.getString("hostphone",
						jsonArr.getJSONObject(i)));
				bean.setHosturl(JsonUtils.getString("hosturl",
						jsonArr.getJSONObject(i)));
				bean.setId(JsonUtils.getInt("id", jsonArr.getJSONObject(i)));
				bean.setUserid(JsonUtils.getInt("userid",
						jsonArr.getJSONObject(i)));
				bean.setIsattend(JsonUtils.getBoolean("isattend", jsonArr.getJSONObject(i)));

				arr.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	public OrganizersBean parseJson(String jsonString) {
		OrganizersBean bean = null;
		try {
			JSONObject obj = new JSONObject(jsonString);

			bean = new OrganizersBean();
			bean.setFollownum(JsonUtils.getInt("follownum", obj));
			bean.setHostcontact(JsonUtils.getString("hostcontact", obj));
			bean.setHosthearurl(JsonUtils.getString("hosthearurl", obj));
			bean.setHostid(JsonUtils.getInt("hostid", obj));
			bean.setHostintro(JsonUtils.getString("hostintro", obj));
			bean.setHostmail(JsonUtils.getString("hostmail", obj));
			bean.setHostname(JsonUtils.getString("hostname", obj));
			bean.setHostphone(JsonUtils.getString("hostphone", obj));
			bean.setHosturl(JsonUtils.getString("hosturl", obj));
			bean.setId(JsonUtils.getInt("id", obj));
			bean.setUserid(JsonUtils.getInt("userid", obj));
			bean.setIsattend(JsonUtils.getBoolean("isattend", obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

}
