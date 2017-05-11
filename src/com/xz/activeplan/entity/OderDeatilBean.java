package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/11.
 */
public class OderDeatilBean implements Serializable {
    private	int	inv_id	;	//	订单ID	主键自增
    private	String	number	;	//	订单编号
    private	int	status	;	//	订单状态	确认=1 付款=2 见面=3 评价=4 完成=5 取消=6
    private	long	make_time	;	//	创建时间
    private	String	trade_no	;	//	交易号	支付宝/微信 交易号
    private	double	amount	;	//	交易金额
    private	int	pay_type	;	//	支付类型	支付宝=0 微信=1
    private	String	gam_title	;	//	话题标题
    private	String	gam_content	;	//	话题内容
    private	String	inv_palce	;	//	邀约地点
    private	long	starttime	;	//	开始时间
    private	long	endtime	;	//	结束时间
    private	int	duration	;	//	时长
    private	String	linkman	;	//	联系人
    private	String	phone	;	//	联系电话
    private	String	inv_title	;	//	邀约主题
    private	String	inv_content	;	//	活动详情
    private	long	sure_time	;	//	确认时间	名人确认时间，后台计算取消时间，前台计算剩余付款时间
    private	long	pay_time	;	//	付款时间	邀请方付款的时间
    private	String	ref_reason	;	//	取消原因	订单取消的原因
    private	int	tea_id	;	//	名人	邀请的名人
    private	int	usr_id	;	//	用户	邀请方
    private CommentBean comment;
    private RefuseOrderBean refuseOrder;
    private String headurl;

    public OderDeatilBean() {
    }

    public OderDeatilBean(String headurl, String phone, String inv_title, String inv_content, long sure_time, long pay_time, String ref_reason, int tea_id, int usr_id, CommentBean comment, RefuseOrderBean refuseOrder, String linkman, int duration, long endtime, long starttime, String inv_palce, String gam_content, String gam_title, int pay_type, double amount, String trade_no, long make_time, int status, String number, int inv_id) {
        this.headurl = headurl;
        this.phone = phone;
        this.inv_title = inv_title;
        this.inv_content = inv_content;
        this.sure_time = sure_time;
        this.pay_time = pay_time;
        this.ref_reason = ref_reason;
        this.tea_id = tea_id;
        this.usr_id = usr_id;
        this.comment = comment;
        this.refuseOrder = refuseOrder;
        this.linkman = linkman;
        this.duration = duration;
        this.endtime = endtime;
        this.starttime = starttime;
        this.inv_palce = inv_palce;
        this.gam_content = gam_content;
        this.gam_title = gam_title;
        this.pay_type = pay_type;
        this.amount = amount;
        this.trade_no = trade_no;
        this.make_time = make_time;
        this.status = status;
        this.number = number;
        this.inv_id = inv_id;
    }

    public int getInv_id() {
        return inv_id;
    }

    public void setInv_id(int inv_id) {
        this.inv_id = inv_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getMake_time() {
        return make_time;
    }

    public void setMake_time(long make_time) {
        this.make_time = make_time;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getGam_title() {
        return gam_title;
    }

    public void setGam_title(String gam_title) {
        this.gam_title = gam_title;
    }

    public String getGam_content() {
        return gam_content;
    }

    public void setGam_content(String gam_content) {
        this.gam_content = gam_content;
    }

    public String getInv_palce() {
        return inv_palce;
    }

    public void setInv_palce(String inv_palce) {
        this.inv_palce = inv_palce;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInv_title() {
        return inv_title;
    }

    public void setInv_title(String inv_title) {
        this.inv_title = inv_title;
    }

    public String getInv_content() {
        return inv_content;
    }

    public void setInv_content(String inv_content) {
        this.inv_content = inv_content;
    }

    public long getSure_time() {
        return sure_time;
    }

    public void setSure_time(long sure_time) {
        this.sure_time = sure_time;
    }

    public long getPay_time() {
        return pay_time;
    }

    public void setPay_time(long pay_time) {
        this.pay_time = pay_time;
    }

    public String getRef_reason() {
        return ref_reason;
    }

    public void setRef_reason(String ref_reason) {
        this.ref_reason = ref_reason;
    }

    public int getTea_id() {
        return tea_id;
    }

    public void setTea_id(int tea_id) {
        this.tea_id = tea_id;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public CommentBean getComment() {
        return comment;
    }

    public void setComment(CommentBean comment) {
        this.comment = comment;
    }

    public RefuseOrderBean getRefuseOrder() {
        return refuseOrder;
    }

    public void setRefuseOrder(RefuseOrderBean refuseOrder) {
        this.refuseOrder = refuseOrder;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    @Override
    public String toString() {
        return "OderDeatilBean{" +
                "inv_id=" + inv_id +
                ", number='" + number + '\'' +
                ", status=" + status +
                ", make_time=" + make_time +
                ", trade_no='" + trade_no + '\'' +
                ", amount=" + amount +
                ", pay_type=" + pay_type +
                ", gam_title='" + gam_title + '\'' +
                ", gam_content='" + gam_content + '\'' +
                ", inv_palce='" + inv_palce + '\'' +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", duration=" + duration +
                ", linkman='" + linkman + '\'' +
                ", phone='" + phone + '\'' +
                ", inv_title='" + inv_title + '\'' +
                ", inv_content='" + inv_content + '\'' +
                ", sure_time=" + sure_time +
                ", pay_time=" + pay_time +
                ", ref_reason='" + ref_reason + '\'' +
                ", tea_id=" + tea_id +
                ", usr_id=" + usr_id +
                ", comment=" + comment +
                ", refuseOrder=" + refuseOrder +
                ", headurl='" + headurl + '\'' +
                '}';
    }
}
