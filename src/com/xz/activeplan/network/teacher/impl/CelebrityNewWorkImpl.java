package com.xz.activeplan.network.teacher.impl;

import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.InvitationCelebriteBean;
import com.xz.activeplan.entity.OderDeatilBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.OkHttpClientManager.Param;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.impl.LoginServiceImpl;
import com.xz.activeplan.network.teacher.CelebrityCallbackInterface;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;

/**
 * 名师网络请求具体实现类
 * @author johnny
 *
 */
public  class CelebrityNewWorkImpl implements CelebrityCallbackInterface {
	private static CelebrityCallbackInterface celebrityCallbackInterface;
	private CelebrityNewWorkImpl() {
	}

	public static CelebrityCallbackInterface getInstance() {
		if (celebrityCallbackInterface == null) {
			synchronized (LoginServiceImpl.class) {
				if (celebrityCallbackInterface == null) {
					celebrityCallbackInterface = new CelebrityNewWorkImpl();
				}
			}
		}
		return celebrityCallbackInterface;
	}

	@Override
	public void getCelebrity(String celebrityId, int nowPage, int pagesize, StringCallback stringCallback) {
		Param paramcatrgoryid = new Param("category", celebrityId +"") ;
		Param paramcurpage = new Param("currpage", nowPage +"");
		Param parampagesize = new Param("pagesize",pagesize+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_CELEBRITY,stringCallback, paramcatrgoryid,paramcurpage,parampagesize);
	}

	@Override
	public void getCelebrityDetails(int id, StringCallback stringCallback) {
		Param teaId = new Param("tea_id", id +"") ;
		boolean login = SharedUtil.isLogin(XZApplication.getInstance());
		OkHttpClientManager.getInstance();
		if (login==true) {
			Param usrId = new Param("usr_id",SharedUtil.getUserInfo(XZApplication.getInstance()).getUserid()+"") ;
			OkHttpClientManager.postAsyn(UrlsManager.URL_CELBRITY_DETAILS, stringCallback, teaId, usrId);
		}else {
			OkHttpClientManager.postAsyn(UrlsManager.URL_CELBRITY_DETAILS, stringCallback, teaId);
		}

	}

	@Override
	public void getTeacherRecommend(String content, StringCallback stringCallback) {
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_CELBRITY_RECOMMEND,stringCallback);
	}
	
	@Override
	public void searchCelebrity(String search, int currpage, int pagesize,
								StringCallback stringCallback) {
		Param paramusearch= new Param("search", search + "");
		Param paramcurpage = new Param("currpage", currpage + "");
		Param parampagesize = new Param("pagesize", pagesize + "");

		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.UTL_SEARCH_CELEBRITY, stringCallback,
				paramusearch,paramcurpage,parampagesize);
	}


	public void InvitationStatus( int teacherid,StringCallback stringCallback) {
		Param paramuserid = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		Param paramteacherid = new Param("tea_id",teacherid+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.Url_INVITY_CELEBRITY_ESTABLISH,stringCallback, paramuserid,paramteacherid);
	}
	@Override
	public void InviteFamousPeople(InvitationCelebriteBean invitationCelebriteBean, StringCallback stringCallback) {
		Param amount = new Param("amount",invitationCelebriteBean.getAmount()+"");
		Param inv_palce = new Param("inv_palce",invitationCelebriteBean.getInv_palce());
		Param starttime = new Param("starttime",invitationCelebriteBean.getStarttime());
		Param linkman = new Param("linkman",invitationCelebriteBean.getLinkman());
		Param phone = new Param("phone",invitationCelebriteBean.getPhone());
		Param gam_title = new Param("gam_title",invitationCelebriteBean.getGam_title());
		Param gam_content = new Param("gam_content",invitationCelebriteBean.getGam_content());
		Param inv_title = new Param("inv_title",invitationCelebriteBean.getInv_title());
		Param inv_content = new Param("inv_content",invitationCelebriteBean.getInv_content());
		Param tea_id = new Param("tea_id",invitationCelebriteBean.getTea_id());
		Param usr_id = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		Param duration = new Param("duration", invitationCelebriteBean.getDuration()+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_INVITY_CELEBRITY,stringCallback,gam_content,gam_title,
				amount,inv_palce,starttime,duration,linkman,phone,inv_title,inv_content,tea_id,usr_id);
	}

	/**
	 * 获取邀请我的学员列表
	 * @param pagesize
	 * @param currpage
	 * @param stringCallback
     */
	@Override
	public void InviteMyPeople(int pagesize, int currpage, StringCallback stringCallback) {
		Param usrId = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		Param pagesize1 = new Param("pagesize",pagesize+"");
		Param currpage1 = new Param("currpage",currpage+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_MY_INVITY_CELEBRITY,stringCallback,
				usrId,pagesize1,currpage1);
	}

	/**
	 * 拒绝邀请
	 * @param orderId
	 * @param celerbriteId
	 * @param reason
	 * @param stringCallback
     */
	@Override
	public void InviteOrderRefuse(int orderId, int celerbriteId, String reason, StringCallback stringCallback) {
		Param inv_id = new Param("inv_id",orderId+"");
		Param pagesize1 = new Param("usr_id",celerbriteId+"");
		Param currpage1 = new Param("ref_reason",reason+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_MY_REFUSEINVITY_CELEBRITY,stringCallback,
				inv_id,pagesize1,currpage1);
	}

	/**
	 * 接受邀请
	 * @param orderId
	 * @param userId
	 * @param stringCallback
     */
	@Override
	public void InviteOrderAgree(int orderId, int userId, long sureTime,  StringCallback stringCallback) {
		Param inv_id = new Param("inv_id",orderId+"");
		Param pagesize1 = new Param("usr_id",userId+"");
		Param sure_Time1 = new Param("sure_time",sureTime+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_MY_AGREE_CELEBRITY,stringCallback,
				inv_id,pagesize1,pagesize1,sure_Time1);
	}

	/**
	 * * 用户评价，星级，类容
	 * @param grade
	 * @param evaluation
	 * @param oderBean
	 * @param stringCallback
     */
	@Override
	public void carryCelebriteEvaluation(int grade, String evaluation, OderDeatilBean oderBean, StringCallback stringCallback) {
		Utiles.log("inv_id:"+oderBean.getInv_id()+" teid:"+oderBean.getTea_id()+"  myId; "+ Utiles.getNetworkAndLogin_OK().getUserid()+
		"  话题："+evaluation+"  grade  "+grade+"  gam_title  "+oderBean.getGam_title());

		Param invId = new Param("inv_id",oderBean.getInv_id()+"");//订单Id
		Param grade1 = new Param("grade",grade+"");//星级
		Param gamTitle = new Param("gam_title","测试标题");//参与话题
		Param gamContent = new Param("content",evaluation+"");//评价内容
		Param usrId = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		Param teaId = new Param("tea_id",oderBean.getTea_id()+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_PUBLISHED_EVALUATION,stringCallback,
				invId,grade1,gamTitle,gamContent,usrId,teaId);
	}
	/**
	 * 确认出席（邀请方）
	 * @param
	 */
	@Override
	public void aonfirmedAttendance(int inv_id, StringCallback stringCallback) {
		Param invId = new Param("inv_id",inv_id+"");//订单Id
		Param usrId = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_INVITION_CONFIRM,stringCallback,
				invId,usrId);
	}
	/**
	 * 跳过
	 * @param
	 */
	@Override
	public void skip(int inv_id, int ted_id, StringCallback stringCallback) {
		Param invId = new Param("inv_id",inv_id+"");//订单Id
		Param teaId = new Param("tea_id",ted_id+"");//订单Id
		Param usrId = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_SKIP,stringCallback,invId,
				teaId,usrId);
	}

	/**
	 * 我发起的邀请
	 * @param pagesize
	 * @param currpage
	 * @param stringCallback
     */
	@Override
	public void sendInvation(int pagesize, int currpage, StringCallback stringCallback) {
		Param pagesize1 = new Param("pagesize",pagesize+"");
		Param currpage1 = new Param("currpage",currpage+"");
		Param usrId = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_MY_SEND_INVITION,stringCallback,
				currpage1,pagesize1,usrId);
	}

	/**
	 * 退款
	 * @param inv_id
	 * @param reason
	 * @param ref_amount
	 * @param remark
     * @param stringCallback
     */
	@Override
	public void refund(int inv_id, String reason, String ref_amount, String remark, StringCallback stringCallback) {
		Param invId = new Param("inv_id",inv_id+"");
		Param reason1 = new Param("reason",reason+"");
		Param refAmount = new Param("ref_amount",ref_amount+"");
		Param remark1 = new Param("remark",remark+"");
		Param usrId = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_REFUND,stringCallback,
				invId,reason1,refAmount,usrId,remark1);
	}

	/**
	 * 拒接退款
	 * @param inv_id
	 * @param reason
	 * @param stringCallback
     */
	@Override
	public void refusedRefund(int inv_id, String reason, StringCallback stringCallback) {
		Param invId = new Param("inv_id",inv_id+"");
		Param reason1 = new Param("ref_reason",reason+"");
		Param usrId = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_REFUND_REFUND,stringCallback,
				invId,reason1,usrId);
	}

	/**
	 * 同意退款
	 * @param inv_id
	 * @param stringCallback
     */
	@Override
	public void agreeRefund(int inv_id,  StringCallback stringCallback) {
		Param invId = new Param("inv_id",inv_id+"");
		Param usrId = new Param("usr_id", Utiles.getNetworkAndLogin_OK().getUserid()+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_AgreeREFUND_REFUND,stringCallback,
				invId,usrId);
	}

	/***
	 * 获取名人视频
	 * @param ted_id
	 * @param stringCallback
     */
	@Override
	public void getVedio(int ted_id, StringCallback stringCallback) {
		Param teaId = new Param("tea_id",ted_id+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_CELBRITY_VEDIO,stringCallback,
				teaId);
	}

	/**
	 * 评价清单
	 * @param ted_id
	 * @param currpage
	 * @param pagesize
	 * @param stringCallback
     */
	@Override
	public void getCeleviteEvaluateList(int ted_id,int currpage,int pagesize, StringCallback stringCallback) {
		Param teaId = new Param("tea_id",ted_id+"");
		Param paramcurpage = new Param("currpage", currpage +"");
		Param parampagesize = new Param("pagesize",pagesize+"");
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_CELBRITY_EVALUATE,stringCallback,
				teaId,paramcurpage,parampagesize);
	}

	/**
	 * 场地清单
	 * @param stringCallback
     */
	@Override
	public void getAreaList(StringCallback stringCallback) {
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_AREA,stringCallback);
	}

	/**
	 * 名人清单
	 * @param stringCallback
     */
	@Override
	public void getCelebriteList(StringCallback stringCallback) {
		OkHttpClientManager.getInstance();
		OkHttpClientManager.postAsyn(UrlsManager.URL_CELEBRITE_LIST,stringCallback);
	}

	/**
	 * 提醒名人
	 * @param stringCallback
     */
	@Override
	public void remindCelebrities(int inv_id,StringCallback stringCallback) {
		OkHttpClientManager.getInstance();
		Param inv_id1 = new Param("inv_id",inv_id+"");
		OkHttpClientManager.postAsyn(UrlsManager.URL_REMIND_CELEBRITES,stringCallback,inv_id1);
	}
	/**
	 * 关注和取消关注
	 */
	public void guanZhuCelebrities(int userId,int tedId, StringCallback stringCallback){
		OkHttpClientManager.getInstance();
		Param usrId = new Param("usr_id",userId+"");
		Param teaId = new Param("tea_id",tedId+"");
		OkHttpClientManager.postAsyn(UrlsManager.URL_GUANZHU_CELEBRITES,stringCallback,teaId,usrId);
	}
	/**
	 * 判断你有没有关注
	 */
	public void guanZhuouYCelebrities(int userId,int tedId, StringCallback stringCallback){
		OkHttpClientManager.getInstance();
		Param usrId = new Param("usr_id",userId+"");
		Param teaId = new Param("tea_id",tedId+"");
		OkHttpClientManager.postAsyn(UrlsManager.URL_GUANZHU_YOU_CELEBRITES,stringCallback,teaId,usrId);
	}

}
