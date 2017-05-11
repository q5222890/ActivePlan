package com.xz.activeplan.network.personal.impl;

import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.OkHttpClientManager.Param;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.personal.IUserInfoService;
import com.xz.activeplan.ui.UrlsManager;

import java.io.File;
import java.io.IOException;

/**
 * 用户信息接口网络请求具体实现类
 *
 * @author johnny
 */
public class UserInfoServiceImpl implements IUserInfoService {

    private static IUserInfoService mIUserInfoService;

    private UserInfoServiceImpl() {
    }

    public static IUserInfoService getInstance() {
        if (mIUserInfoService == null) {
            synchronized (LoginServiceImpl.class) {
                if (mIUserInfoService == null) {
                    mIUserInfoService = new UserInfoServiceImpl();
                }
            }
        }
        return mIUserInfoService;
    }

    @Override
    public void saveUserInfo(int userId, String userName, String city,
                             String realName, String signature, String phonenum, String sex,
                             String email, String alipayaccount, String alipayusername, StringCallback stringCallback) {
        // 传参数修改key value
        Param paramid = new Param("userid", userId + "");
        Param paramusername = new Param("username", userName);
        Param paramCity = new Param("city", city);
        Param paramRealname = new Param("realname", realName);
        Param paramSignature = new Param("signature", signature);
        Param paramPhonenum = new Param("phonenum", phonenum);
        Param paramSex = new Param("sex", sex+ "");
        Param paramEmail = new Param("email", email);
        Param paramalipayaccount = new Param("alipayaccount", alipayaccount);
        Param paramalipayusername = new Param("alipayusername", alipayusername);


        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.SAVE_USER_URL, stringCallback,
                paramid, paramusername, paramCity, paramRealname,
                paramSignature, paramPhonenum, paramSex, paramEmail, paramalipayaccount, paramalipayusername);
    }

    @Override
    public void myTicket(int userId, int type, int currpage, int pagesize,
                         StringCallback stringCallback) {

        Param paramid = new Param("userid", userId + "");
        Param paramtype = new Param("type", type + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.MY_TICKET_URL, stringCallback,
                paramid, paramtype, paramcurpage, parampagesize);
    }

    @Override
    public void myCollect(int userId, int currpage, int pagesize,
                          StringCallback stringCallback) {
        Param paramid = new Param("userid", userId + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.MY_COLLECT, stringCallback,
                paramid, paramcurpage, parampagesize);
    }

    @Override
    public void myHost(int userId, int currpage, int pagesize,
                       StringCallback stringCallback) {
        Param paramid = new Param("userid", userId + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.MY_HOST, stringCallback,
                paramid, paramcurpage, parampagesize);
    }

    @Override
    public void myHostList(int userId, int type, int currpage, int pagesize, StringCallback stringCallback) {
        Param paramid = new Param("userid", userId + "");
        Param paramtype = new Param("type", type + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.MY_HOST, stringCallback,
                paramid, paramtype, paramcurpage, parampagesize);
    }

    @Override
    public void myFriend(int userId, int currpage, int pagesize,
                         StringCallback stringCallback) {
        Param paramid = new Param("userid", userId + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_MY_FRIEND, stringCallback,
                paramid, paramcurpage, parampagesize);
    }

    /**
     * listView数据
     *
     * @param addr           城市, 比如深圳市
     * @param categoryid     类别ID(最新=0爱心吧= 1
     *                       相亲吧=2商务吧=3老乡吧=4创业吧=5运动吧=6学习吧=7旅游吧=8生活吧=9校园吧=10群星吧=11同性吧=12其它吧=
     *                       13)
     * @param currpage
     * @param pagesize
     * @param stringCallback
     */
    @Override
    public void myCategory(String addr, int categoryid, int currpage,
                           int pagesize, StringCallback stringCallback) {

        Param paramaddr = new Param("addr", "全国".equals(addr) ? "" : addr);
        Param paramcategoryid = new Param("categoryid", categoryid + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.MY_CATEGORY, stringCallback,
                paramaddr, paramcategoryid, paramcurpage, parampagesize);
    }

    @Override
    public void getActiveCategoryInfo(int categoryid, StringCallback stringCallback) {
        Param paramcategoryid = new Param("act_id", categoryid + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_CATEGORYINFO, stringCallback,
                paramcategoryid);
    }

    /***
     * @param addr           城市
     * @param categoryid     分类id
     * @param currpage       当前页数，第几页
     * @param pagesize       一页几条 默认20条
     * @param comprehensive  综合热门排序
     * @param startdate      时间排序
     * @param charge         是否收费
     * @param stringCallback 回调接口
     */
    @Override
    public void myCategoryOrder(String addr, int categoryid, int currpage, int pagesize,
                                String comprehensive, int startdate, int charge, StringCallback stringCallback) {

        Param paramaddr = new Param("addr", "全国".equals(addr) ? "" : addr);
        Param paramcategoryid = new Param("categoryid", categoryid + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");
        Param paramcomprehensive = new Param("comprehensive", comprehensive + "");
        Param paramstartdate = new Param("startdate", startdate + "");
        Param paramcharge = new Param("charge", charge + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.MY_CATEGORY_ORDER, stringCallback,
                paramaddr, paramcategoryid, paramcurpage, parampagesize,
                paramcomprehensive, paramstartdate, paramcharge);
    }

    @Override
    public void pushactive(String addr, int pushid, int currpage, int pagesize,
                           StringCallback stringCallback) {
        Param paramaddr = new Param("addr", "全国".equals(addr) ? "" : addr);
        Param parampushid = new Param("pushid", pushid + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.MY_PUSHACTIVE, stringCallback,
                paramaddr, parampushid, paramcurpage, parampagesize);
    }

    @Override
    public void getUserinfo(String username, StringCallback stringCallback) {
        Param paramuser = new Param("username", username);
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.GET_USERINFO_USERNAME_URL,
                stringCallback, paramuser);
    }

    @Override
    public void getUserinfo(int userid, StringCallback stringCallback) {
        Param paramuser = new Param("userid", userid + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.GET_USERINFO_ID_URL,
                stringCallback, paramuser);
    }

    @Override
    public void collectAction(int userid, int activeid,
                              StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        Param paramactiveid = new Param("activeid", activeid + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.COLLECT_ACTION_URL,
                stringCallback, paramuserid, paramactiveid);
    }

    @Override
    public void uploadHeadImg(int userid, String path,
                              StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        OkHttpClientManager.getInstance();
        try {
            OkHttpClientManager.postAsyn(UrlsManager.USERINFO_UPLOAD_HEADIMG,
                    stringCallback, new File(path), "headfile", paramuserid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attendFriend(int userid, int friendid,
                             StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        Param paramfriendid = new Param("friendid", friendid + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.ATTEND_FRIEND, stringCallback,
                paramuserid, paramfriendid);
    }

    @Override
    public void cancelFriend(int userid, int friendid,
                             StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        Param paramfriendid = new Param("friendid", friendid + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.CANCEL_FRIEND, stringCallback,
                paramuserid, paramfriendid);
    }

    @Override
    public void attendHost(int userid, int hostid, StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        Param paramhostid = new Param("hostid", hostid + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.ATTEND_HOST, stringCallback,
                paramuserid, paramhostid);
    }

    @Override
    public void cancelHost(int userid, int hostid, StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        Param paramhostid = new Param("hostid", hostid + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.CANCEL_HOST, stringCallback,
                paramuserid, paramhostid);
    }

    @Override
    public void getHostInfo(int userid, int hostid,
                            StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        Param paramhostid = new Param("hostid", hostid + "");
        OkHttpClientManager.getInstance();
        if (userid != -1) {
            OkHttpClientManager.postAsyn(UrlsManager.GET_HOST_INFO, stringCallback,
                    paramuserid, paramhostid);
        } else {
            OkHttpClientManager.postAsyn(UrlsManager.GET_HOST_INFO, stringCallback,
                    paramhostid);
        }

    }

    @Override
    public void groupmember(String groupid, StringCallback stringCallback) {

        Param paramgroupid = new Param("groupid", groupid + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.GROUP_MEMBER, stringCallback,
                paramgroupid);
    }

    @Override
    public void modifyHost(int userid, String hostname, String hostcontact,
                           String hostphone, String hostmail, String hostintro,
                           String hosthearurl, String hosturl, StringCallback stringCallback) {

        Param paramuserid = new Param("userid", userid + "");
        Param paramhostname = new Param("hostname", hostname + "");
        Param paramhostcontact = new Param("hostcontact", hostcontact + "");
        Param paramhostphone = new Param("hostphone", hostphone + "");
        Param paramhostmail = new Param("hostmail", hostmail + "");
        //Param paramuhosthearurl = new Param("hosthearurl", userid + "");
        Param paramuhostintrol = new Param("hostintro", hostintro + "");
        Param paramhosturl = new Param("hosturl", hosturl);

        OkHttpClientManager.getInstance();
        try {
            OkHttpClientManager.postAsyn(UrlsManager.MODIFY_HOST,
                    stringCallback, new File(hosthearurl), "headfile",
                    paramuserid, paramhostname, paramhostcontact, paramhostphone, paramhostmail, paramuhostintrol, paramhosturl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUserHostId(int userid, StringCallback stringCallback) {

        Param paramuserid = new Param("userid", userid + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.GET_USER_HOSTID, stringCallback,
                paramuserid);
    }

    @Override
    public void hostPostActive(int hostid, int type, int currpage,
                               int pagesize, StringCallback stringCallback) {
        Param paramhostid = new Param("hostid", hostid + "");
        Param paramtype = new Param("type", type + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.HOAST_POST_ACTIVE, stringCallback,
                paramhostid, paramtype, paramcurpage, parampagesize);
    }

    @Override
    public void checkTicket(String ticket, int activeid, StringCallback stringCallback) {
        Param paramTicket = new Param("ticket", ticket + "");
        Param paramActiveId = new Param("activeid",activeid+"");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.CHECK_TICKET, stringCallback,
                paramTicket,paramActiveId);
    }

    @Override
    public void ticketList(int activeid, StringCallback stringCallback) {
        Param paramactiveid = new Param("activeid", activeid + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.TICKET_LIST, stringCallback,
                paramactiveid);
    }

    @Override
    public void cancelCollect(int userid, int activeid, StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        Param paramactiveid = new Param("activeid", activeid + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.CANCEL_COLLECT_ACTION_URL,
                stringCallback, paramuserid, paramactiveid);
    }

    @Override
    public void getMoneyAccount(String userid, String phonenum, StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        Param paramphonenum = new Param("phonenum", phonenum + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.GET_ACCOUNT_URL,
                stringCallback, paramuserid, paramphonenum);
    }

    @Override
    public void adddetail(String userid, String phonenum, double num, int type,
                          String intro, StringCallback stringCallback) {

        Param paramuserid = new Param("userid", userid + "");

        Param paramphonenum = new Param("phonenum", phonenum + "");

        Param paramnum = new Param("num", num + "");

        Param paramtype = new Param("type", type + "");

        Param paramintro = new Param("intro", intro + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.ADD_DATAIL_URL,
                stringCallback, paramuserid, paramphonenum, paramnum, paramtype, paramintro);
    }

    @Override
    public void getDetail(String userid, int currpage, int pagesize,
                          StringCallback stringCallback) {
        Param paramid = new Param("userid", userid + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.GET_DATAIL_URL, stringCallback,
                paramid, paramcurpage, parampagesize);

    }

    @Override
    public void updatever(String versioncode, StringCallback stringCallback) {
        Param paramversioncode = new Param("versioncode", versioncode + "");


        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.UPDATEVER_URL, stringCallback,
                paramversioncode);
    }

    @Override
    public void feedback(String content, String num, StringCallback stringCallback) {
        Param paramcontent = new Param("content", content + "");
        Param paramnum = new Param("num", num + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.FEEDBACK_URL, stringCallback,
                paramcontent, paramnum);
    }

    @Override
    public void getad(int type, StringCallback stringCallback) {
        Param paramtype = new Param("type", type + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.GETAD_URL, stringCallback,
                paramtype);
    }

    @Override
    public void thirdlogin(String type,String openid,String username, String sex,
                           String headurl, StringCallback stringCallback) {
        Param paramtype =new Param("type",type);
        Param paramopenid =new Param("openid",openid);
        Param paramusername = new Param("username", username);
        Param paramsex = new Param("sex", sex);
        Param paramheadurl = new Param("headurl", headurl);
        //&type=qq&openid=qqloginopenid&username=qqlogin&sex=1&headurl=123456789
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.THIRDLOGIN_URL, stringCallback,
                paramtype,paramopenid, paramusername, paramsex, paramheadurl);

    }

    @Override
    public void bindLogin(String type, String phonenum, String openid, String username, String sex,
                          String headurl, String code, StringCallback stringCallback) {
        //&type=qq&phonenum=18402010908&openid=qqloginopenid&username=qqlogin&sex=1&headurl=123456789&code=145658

        Param paramtype =new Param("type",type);
        Param paramphonenum =new Param("phonenum",phonenum);
        Param paramopenid =new Param("openid",openid);
        Param paramusername = new Param("username", username);
        Param paramsex = new Param("sex", sex);
        Param paramheadurl = new Param("headurl", headurl);
        Param paramcode = new Param("code", code);

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.THIRDLOGIN_URL, stringCallback,
                paramtype, paramphonenum, paramopenid, paramusername, paramsex, paramheadurl, paramcode);
    }

    @Override
    public void getSignUp(int userId, int currpage, int pagesize, StringCallback stringCallback) {
        Param paramid = new Param("userid", userId + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_MANAGER_SINGUP, stringCallback,
                paramid, paramcurpage, parampagesize);
    }

    @Override
    public void getMyFans(int userId, int currpage, int pagesize, StringCallback stringCallback) {
        Param paramid = new Param("userid", userId + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_ACTIVEFANS, stringCallback,
                paramid, paramcurpage, parampagesize);
    }

    @Override
    public void getLiveFans(int userId, int currpage, int pagesize, StringCallback stringCallback) {
        Param paramid = new Param("userid", userId + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_LIVEFANS, stringCallback,
                paramid, paramcurpage, parampagesize);
    }
    @Override
    public void getHostOrderInfo(int userid, int hostid, int currpage, int pagesize, StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");
        Param paramhostid = new Param("hostid", hostid + "");
        Param paramcurpage = new Param("currpage", currpage + "");
        Param parampagesize = new Param("pagesize", pagesize + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_LIVE_SPONSOR, stringCallback,
                paramuserid, paramhostid, paramcurpage, parampagesize);
    }


    /**
     * 关注主播
     *
     * @param userid
     * @param orther
     * @param stringCallback
     */
    @Override
    public void followPerson(int userid, int orther, StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");
        Param paramfriendid = new Param("otheruserid", orther + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_FOLLOW_ANCHOR, stringCallback,
                paramuserid, paramfriendid);
    }

    /**
     * 取消关注主播
     *
     * @param userid
     * @param orther
     * @param stringCallback
     */
    @Override
    public void cancelFollowPerson(int userid, int orther, StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");

        Param paramfriendid = new Param("otheruserid", orther + "");
        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_CANCEL_FOLLOW_ANCHOR, stringCallback,
                paramuserid, paramfriendid);
    }




    @Override
    public void submitAnthentication(int userid, String idimg, String idotherimg,
                                     String name, long birthday, String idcardno,
                                     String alipayname, String alipayno, StringCallback stringCallback) {

        Param paramuserid = new Param("userid", userid + "");
        Param paramname = new Param("name", name);
        Param parambirthday = new Param("birthday", birthday + "");
        Param paramidcardno = new Param("idcardno", idcardno);
        Param paramalipayname = new Param("alipayname", alipayname);
        Param paramalipayno = new Param("alipayno", alipayno);

        String[] filekeys=new String[]{"idimg","idotherimg"};
        File[] files =new File[]{new File(idimg),new File(idotherimg)};
        OkHttpClientManager.getInstance();
        try {
            OkHttpClientManager.postAsyn(UrlsManager.URL_AUTHENTICATION, stringCallback,
                    files ,filekeys,paramuserid, paramname, parambirthday, paramidcardno,
                    paramalipayname, paramalipayno);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的名人堂粉丝
     * @param userid
     * @param stringCallback
     */
    @Override
    public void myCelebriteFans(int userid, StringCallback stringCallback) {
        Param paramisadopt = new Param("usr_id", userid + "");
        OkHttpClientManager.postAsyn(UrlsManager.URL_MY_CELBRITY_DETAILS, stringCallback,  paramisadopt);
    }
    /**
     * 我的关注主播某一个主播
     * @param userid
     * @param stringCallback
     */
    @Override
    public void getGuanZhuZhuBoItem(int userid, StringCallback stringCallback) {
        Param paramisadopt = new Param("usr_id", userid + "");
        OkHttpClientManager.postAsyn(UrlsManager.URL_MY_CELBRITY_DETAILS, stringCallback,  paramisadopt);
    }

    @Override
    public void getAuthenticStatus(int userid, StringCallback stringCallback) {
        Param paramuserid =new Param("userid",userid+"");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_AUTHENTIC_STATUS,stringCallback,paramuserid);
    }
}
