package com.xz.activeplan.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.xz.activeplan.entity.AccountBean;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.utils.JsonUtils;

public class AccountJson implements IJson {

	@Override
	public Object analysisJson2Object(String obj) {
		AccountBean bean = null ;
		
		try {
			bean = new AccountBean() ;
			bean.setDrawnum(JsonUtils.getDouble("drawnum", obj));
			bean.setFronzen(JsonUtils.getDouble("fronzen", obj));
			bean.setId(JsonUtils.getInt("id", obj));
			bean.setPhonenum(JsonUtils.getString("phonenum", obj));
			bean.setTotal(JsonUtils.getDouble("total", obj));
			bean.setUpdatetime(JsonUtils.getString("updatetime", obj));
			bean.setUserid(JsonUtils.getInt("userid", obj));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}


	public ArrayList<UserInfoBean> parseJsonArr(String json){
		ArrayList<UserInfoBean> arr = new ArrayList<UserInfoBean>();
		JSONArray jsonArr;
		try {
			jsonArr = new JSONArray(json);
			for(int i = 0 ;i < jsonArr.length();i++){
				arr.add((UserInfoBean)analysisJson2Object(jsonArr.getString(i))) ;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arr;
	}

}
