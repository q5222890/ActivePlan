package com.xz.activeplan.network.teacher;

import com.xz.activeplan.entity.InvitationCelebriteBean;
import com.xz.activeplan.entity.OderDeatilBean;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;

/**
 * 名人网络接口
 * @author johnny
 *CelebrityCallbackInterface
 */
public interface CelebrityCallbackInterface {

	/**
	 * 4.1.11.	名师
	 * @param celebrityId 类型/名师ID
	 * @param nowPage 当前页数, 第几页
	 * @param pagesize 一页几条(默认20条)
	 * @param stringCallback 回调接口
	 */
	public void getCelebrity(String  celebrityId, int nowPage, int pagesize, StringCallback stringCallback) ;
	
	/**
	 * 4.1.12.	获取名师详情
	 * @param celebrityId 名师id
	 * @param stringCallback 回调接口
	 */
	public void getCelebrityDetails(int celebrityId, StringCallback stringCallback) ;
	
	/**
	 * 4.1.22.	获取名师推荐
	 * @param content
	 * @param stringCallback 回调接口
	 */
	public void getTeacherRecommend(String content, StringCallback stringCallback) ;
	
	/**
	 * 4.1.26.	搜索名师
	 * 	接口地址：http://localhost:8080/tryst/teacher?action=search
	 * @param search 搜索字段
	 * @param currpage 当前页数, 第几页,从0开始
	 * @param pagesize 一页几条(默认20条)
	 * @param stringCallback 回调接口
	 */
	public void searchCelebrity(String search, int currpage, int pagesize, StringCallback stringCallback);
	
	/**
	 * 4.1.25.	邀请名师---查看名师是否被邀请
	 * @param celebriteId 名师id
	 * @param stringCallback 回调接口
	 */
	public void InvitationStatus(int celebriteId, StringCallback stringCallback);

	/**
	 * 邀请名师---创建订单
	 * @param invitationCelebriteBean
     */
	public void InviteFamousPeople(InvitationCelebriteBean invitationCelebriteBean, StringCallback stringCallback);

	/**
	 * 获取别人邀请我
	 * @param
	 */
	public void InviteMyPeople(int pagesize, int currpage, StringCallback stringCallback);
	/**
	 * 拒绝邀请
	 * @param
	 */
	public void InviteOrderRefuse(int orderId, int celerbriteId, String reason, StringCallback stringCallback);
	/**
	 * 同意
	 * @param
	 */
	public void InviteOrderAgree(int orderId, int userId, long sureTime, StringCallback stringCallback);
	/**
	 * 用户评价
	 * @param
	 */
	public void carryCelebriteEvaluation(int grade, String evaluation, OderDeatilBean oderBean, StringCallback stringCallback);
	/**
	 * 确认出席（邀请方）
	 * @param
	 */
	public void aonfirmedAttendance(int inv_id, StringCallback stringCallback);
	/**
	 * 跳过
	 * @param
	 */
	public void skip(int inv_id, int ted_id, StringCallback stringCallback);
	/**
	 * 我发起的邀请
	 * @param
	 */
	public void sendInvation(int pagesize, int currpage, StringCallback stringCallback);
	/**
	 * 申请退款
	 * @param
	 */
	public void refund(int inv_id, String reason, String ref_amount, String remark, StringCallback stringCallback);
	/**
	 * 拒绝退款
	 * @param
	 */
	public void refusedRefund(int inv_id, String reason, StringCallback stringCallback);
	/**
	 * 拒绝退款
	 * @param
	 */
	public void agreeRefund(int inv_id, StringCallback stringCallback);
	/**
	 * 拒绝退款
	 * @param
	 */
	public void getVedio(int ted_id, StringCallback stringCallback);
	/**
	 * 得到名人的评价
	 * @param
	 */
	public void getCeleviteEvaluateList(int ted_id,int currpage,int pagesize, StringCallback stringCallback);

	/**
	 * 场地清单
	 * @param
	 */
	public void getAreaList(StringCallback stringCallback);

	/**
	 * m名人清单
	 * @param
	 */
	public void  getCelebriteList(StringCallback stringCallback);

	/**
	 * 提醒名
	 */
	public void remindCelebrities(int inv_id, StringCallback stringCallback);
	/**
	 * 关注和取消关注
	 */
	public void guanZhuCelebrities(int userId,int tedId, StringCallback stringCallback);
	/**
	 * 判断你有没有关注
	 */
	public void guanZhuouYCelebrities(int userId,int tedId, StringCallback stringCallback);
}
