package com.xz.activeplan.json;

import java.util.ArrayList;

import org.json.JSONArray;

import com.xz.activeplan.entity.CollectBean;
import com.xz.activeplan.utils.JsonUtils;

public class CollectJosn implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<CollectBean> arr = new ArrayList<CollectBean>() ;
		try {
			JSONArray jsonArr = new JSONArray(jsonString);
			for(int i = 0 ;i < jsonArr.length();i++){
				CollectBean bean = new CollectBean();
				bean.setActiveid(JsonUtils.getInt("activeid", jsonArr.getJSONObject(i)));
				bean.setActiveurl(JsonUtils.getString("activeurl", jsonArr.getJSONObject(i)));
				bean.setAddress(JsonUtils.getString("address", jsonArr.getJSONObject(i)));
				bean.setCharge(JsonUtils.getBoolean("charge", jsonArr.getJSONObject(i)));
				bean.setCollectid(JsonUtils.getInt("collectid", jsonArr.getJSONObject(i)));
				bean.setEnddate(JsonUtils.getLong("enddate", jsonArr.getJSONObject(i)));
				bean.setIsend(JsonUtils.getBoolean("isend", jsonArr.getJSONObject(i)));
				bean.setMoney(JsonUtils.getDouble("money", jsonArr.getJSONObject(i)));
				bean.setName(JsonUtils.getString("name", jsonArr.getJSONObject(i)));
				bean.setSeenum(JsonUtils.getInt("seenum", jsonArr.getJSONObject(i)));
				bean.setStartdate(JsonUtils.getLong("startdate", jsonArr.getJSONObject(i)));
				bean.setUserid(JsonUtils.getInt("userid", jsonArr.getJSONObject(i)));
				arr.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

}
