package com.xz.activeplan.json;

import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.utils.JsonUtils;

public class StatusJson implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		StatusBean bean = null ;
		
		try {
			bean = new StatusBean() ;
			bean.setCode(JsonUtils.getInt("code", jsonString)) ;
			bean.setData(JsonUtils.getString("data", jsonString)) ;
			bean.setMsg(JsonUtils.getString("msg", jsonString)) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

}
