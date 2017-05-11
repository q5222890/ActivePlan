package com.xz.activeplan.json;

import java.util.ArrayList;

import org.json.JSONArray;

import com.xz.activeplan.entity.AccountDeatilBean;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.utils.JsonUtils;

public class AccountDetailJosn implements IJson {

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<AccountDeatilBean> arr = new ArrayList<AccountDeatilBean>();
		
		try {
			JSONArray jsonArr = new JSONArray(jsonString) ;
			for(int i = 0 ;i < jsonArr.length();i++){
				AccountDeatilBean bean = new AccountDeatilBean() ;
				bean.setCreatetime(JsonUtils.getString("createtime", jsonArr.getJSONObject(i)));
				bean.setId(JsonUtils.getInt("id", jsonArr.getJSONObject(i)));
				bean.setNum(JsonUtils.getDouble("num", jsonArr.getJSONObject(i)));
				bean.setPhonenum(JsonUtils.getString("phonenum", jsonArr.getJSONObject(i)));
				bean.setType(JsonUtils.getInt("type", jsonArr.getJSONObject(i)));
				bean.setUserid(JsonUtils.getInt("userid", jsonArr.getJSONObject(i)));
				bean.setIntro(JsonUtils.getString("intro", jsonArr.getJSONObject(i)));
				arr.add(bean) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}


}
