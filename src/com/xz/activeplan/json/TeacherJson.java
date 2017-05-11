package com.xz.activeplan.json;

import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherJson<T> implements IJson {
	public TeacherJson() {
	}

	@Override
	public Object analysisJson2Object(String jsonString) {
		ArrayList<TeacherBean> arr = new ArrayList<TeacherBean>();
		try {
			JSONArray jsonArr = new JSONArray(jsonString) ;
			for(int i = 0 ;i < jsonArr.length();i++){
			/*	TeacherBean bean = new TeacherBean() ;
				 bean.setId(JsonUtils.getInt("id", jsonArr.getJSONObject(i)));//基本ID
				 bean.setRealname(JsonUtils.getString("realname", jsonArr.getJSONObject(i)));	;//姓名
				bean.setPosition(JsonUtils.getString("position", jsonArr.getJSONObject(i)));	;//头衔职位
				bean.setCompany(JsonUtils.getString("company", jsonArr.getJSONObject(i)));	;//任职机构
				bean.setWorktime(JsonUtils.getString("worktime", jsonArr.getJSONObject(i)));	;//工作年限
				bean.setPhone(JsonUtils.getString("phone", jsonArr.getJSONObject(i))); 	;//手机号码
				bean.setEmail(JsonUtils.getString("email", jsonArr.getJSONObject(i))); 	;//邮箱地址
				bean.setAddress(JsonUtils.getString("address", jsonArr.getJSONObject(i))); 	;//常驻地址
				bean.setCertifier(JsonUtils.getString("certifier", jsonArr.getJSONObject(i))); 	;//身份证明人
				bean.setCard(JsonUtils.getString("card", jsonArr.getJSONObject(i))); 	;//名片
				bean.setSkill(JsonUtils.getString("skill", jsonArr.getJSONObject(i)));		;//经历
				bean.setIntro(JsonUtils.getString("intro", jsonArr.getJSONObject(i))); 	;//
				bean.setIntro1(JsonUtils.getString("intro1", jsonArr.getJSONObject(i))); 	;//个人介绍1
				bean.setIntro2(JsonUtils.getString("intro2", jsonArr.getJSONObject(i))); 	;//
				bean.setIntro3(JsonUtils.getString("intro3", jsonArr.getJSONObject(i))); 	;//
				bean.setGambit1(JsonUtils.getString("gambit1", jsonArr.getJSONObject(i))); 	;//我的话题1
				bean.setGambit2(JsonUtils.getString("gambit2", jsonArr.getJSONObject(i))); 	;//
				bean.setGambit3(JsonUtils.getString("gambit3", jsonArr.getJSONObject(i))); 	;//
				bean.setGambit4(JsonUtils.getString("gambit4", jsonArr.getJSONObject(i))); 	;//
				bean.setGambit5(JsonUtils.getString("gambit5", jsonArr.getJSONObject(i))); 	;//
				bean.setGambit6(JsonUtils.getString("gambit6", jsonArr.getJSONObject(i))); 	;//
				bean.setCategory(JsonUtils.getString("category", jsonArr.getJSONObject(i)));  	;//类别ID
				bean.setPushid(JsonUtils.getInt("pushid", jsonArr.getJSONObject(i)));  	;//推荐
				bean.setAuthstatus(JsonUtils.getInt("authstatus", jsonArr.getJSONObject(i)));  	;//认证状态
				bean.setUserid(JsonUtils.getInt("userid", jsonArr.getJSONObject(i)));  	;//用户ID
				bean.setHeadurl(JsonUtils.getString("headurl", jsonArr.getJSONObject(i)));  	;//用户ID
*/
				//arr.add(bean) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	public TeacherBean parseJson(String jsonString){
		TeacherBean bean =null;
		try {
			JSONObject obj = new JSONObject(jsonString) ;
				bean = new TeacherBean() ;

			/*bean.setId(JsonUtils.getInt("id", obj));//基本ID
			bean.setRealname(JsonUtils.getString("realname", obj));	;//姓名
			bean.setPosition(JsonUtils.getString("position", obj));	;//头衔职位
			bean.setCompany(JsonUtils.getString("company", obj));	;//任职机构
			bean.setWorktime(JsonUtils.getString("worktime", obj));	;//工作年限
			bean.setPhone(JsonUtils.getString("phone", obj)); 	;//手机号码
			bean.setEmail(JsonUtils.getString("email", obj)); 	;//邮箱地址
			bean.setAddress(JsonUtils.getString("address", obj)); 	;//常驻地址
			bean.setCertifier(JsonUtils.getString("certifier", obj)); 	;//身份证明人
			bean.setCard(JsonUtils.getString("card", obj)); 	;//名片
			bean.setSkill(JsonUtils.getString("skill", obj));	;//经历
			bean.setIntro(JsonUtils.getString("intro", obj)); 	;//
			bean.setIntro1(JsonUtils.getString("intro1", obj)); 	;//个人介绍1
			bean.setIntro2(JsonUtils.getString("intro2", obj)); 	;//
			bean.setIntro3(JsonUtils.getString("intro3", obj)); 	;//
			bean.setGambit1(JsonUtils.getString("gambit1", obj)); 	;//我的话题1
			bean.setGambit2(JsonUtils.getString("gambit2", obj)); 	;//
			bean.setGambit3(JsonUtils.getString("gambit3", obj)); 	;//
			bean.setGambit4(JsonUtils.getString("gambit4", obj)); 	;//
			bean.setGambit5(JsonUtils.getString("gambit5", obj)); 	;//
			bean.setGambit6(JsonUtils.getString("gambit6", obj)); 	;//
			bean.setCategory(JsonUtils.getString("category", obj));  	;//类别ID
			bean.setPushid(JsonUtils.getInt("pushid", obj));  	;//推荐
			bean.setAuthstatus(JsonUtils.getInt("authstatus", obj));  	;//认证状态
			bean.setUserid(JsonUtils.getInt("userid", obj));  	;//用户ID
			bean.setHeadurl(JsonUtils.getString("headurl", obj));  	;//用户ID*/
			return bean;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}


	public List<TeacherBean> CelebriteSimpleBeanJson(String jsonString) {

		List<TeacherBean> arr = new ArrayList<TeacherBean>();
		try {
			JSONArray jsonArr = new JSONArray(jsonString);
			for (int i = 0; i < jsonArr.length(); i++) {
				TeacherBean bean = new TeacherBean();
				bean.setTea_id(JsonUtils.getInt("id", jsonArr.getJSONObject(i)));//基本ID

				bean.setRealname(JsonUtils.getString("realname", jsonArr.getJSONObject(i)));
				;//姓名
				bean.setCover(JsonUtils.getString("cover", jsonArr.getJSONObject(i)));
				arr.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	}
