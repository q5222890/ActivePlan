package com.xz.activeplan.network.active.impl;

import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.OkHttpClientManager.Param;
import com.xz.activeplan.network.OkHttpClientManager.StringCallback;
import com.xz.activeplan.network.active.IActiveService;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.utils.Utiles;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 活动网络接口具体实现类
 *
 * @author johnny
 */
public class ActiveServiceImpl implements IActiveService {


    private static IActiveService mIActiveService;

    private ActiveServiceImpl() {
    }

    public static IActiveService getInstance() {
        if (mIActiveService == null) {
            synchronized (ActiveServiceImpl.class) {
                if (mIActiveService == null) {
                    mIActiveService = new ActiveServiceImpl();
                }
            }
        }
        return mIActiveService;
    }

    @Override
    public void pushActive(int userid, int hostid, String name, String address, long startdate, long enddate,
                           String activeurl, int personnum, int themeid, int formid, int categoryid, String label, String content,
                           boolean charge, double money, String json, StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");
        Param paramhostid = new Param("hostid", hostid + "");
        Param paramname = new Param("name", name + "");
        Param paramaddress = new Param("address", address + "");
        Param paramstartdate = new Param("startdate", startdate + "");
        Param paramenddate = new Param("enddate", enddate + "");
        Param paramcharge = new Param("charge", charge + "");
        Param parampersonnum = new Param("personnum", personnum + "");
        Param paramthemeid = new Param("themeid", themeid + "");
        Param paramformid = new Param("formid", formid + "");
        Param paramcategoryid = new Param("categoryid", categoryid + "");
        Param paramlabel = new Param("label", label + "");
        Param paramcontent = new Param("content", content + "");
        Param parammoney = new Param("money", money + "");
        Param paramjson = new Param("ticket_list", json);

        OkHttpClientManager.getInstance();

        try {
            OkHttpClientManager.postAsyn(UrlsManager.ACTIVE_ACTION_SAVE_URL, stringCallback,
                    new File(activeurl), "headfile", paramuserid, paramhostid,
                    paramname, paramaddress, paramstartdate, paramenddate, paramcharge, parampersonnum,
                    paramthemeid, paramformid, paramcategoryid, paramlabel, paramcontent, parammoney, paramjson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 活动详细接口
     *
     * @param userid         登录用户的id
     * @param activeid       活动id
     * @param stringCallback 回调接口
     */
    @Override
    public void getActive(int userid, int activeid, StringCallback stringCallback) {
        Param paramUserid = null;
        if (userid != -1) {
            paramUserid = new Param("userid", userid + "");
        }
        Param paramActiveid = new Param("activeid", activeid + "");
        OkHttpClientManager.getInstance();
        if (userid == -1) {
            OkHttpClientManager.postAsyn(UrlsManager.ACTIVE_ACTION_GET, stringCallback, paramActiveid);
        } else {
            OkHttpClientManager.postAsyn(UrlsManager.ACTIVE_ACTION_GET, stringCallback, paramUserid, paramActiveid);
        }
    }

    @Override
    public void searchActive(String search, StringCallback stringCallback) {
        Param paramSearch = new Param("search", search + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.ACTIVE_ACTION_SEARCH, stringCallback, paramSearch);
    }

    @Override
    public void applyActive(int userid, int activeid, String paynum, int payamount, int paytype, String telphone, String weixin, String company, String position, String tickettypeid, String realname,
                            StringCallback stringCallback) {
        Param paramUserid = new Param("userid", userid + "");
        Param paramactiveid = new Param("activeid", activeid + "");
        Param paramtelphone = new Param("telphone", telphone);
        Param paramweixin = new Param("weixin", weixin);
        Param paramcompany = new Param("company", company);
        Param paramposition = new Param("position", position);
        Param paramtickettypeid = new Param("tickettypeid", tickettypeid);
        Param paramrealname = new Param("realname", realname);

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.APPLY_ACITVE, stringCallback, paramUserid, paramactiveid, paramtelphone, paramweixin, paramcompany, paramposition, paramtickettypeid, paramrealname);

    }

    @Override
    public void applychargeActive(int userid, int activeid, String paynum, double payamount, int paytype, String telphone, String weixin, String company, String position, String tickettypeid, String realname,
                                  StringCallback stringCallback) {
        Param paramUserid = new Param("userid", userid + "");
        Param paramactiveid = new Param("activeid", activeid + "");
        Param paramtelphone = new Param("telphone", telphone);
        Param paramweixin = new Param("weixin", weixin);
        Param paramcompany = new Param("company", company);
        Param paramposition = new Param("position", position);
        Param paramtickettypeid = new Param("tickettypeid", tickettypeid);
        Param paramrealname = new Param("realname", realname);
        Param parampaynum = new Param("paynum", paynum + "");
        Param parampayamount = new Param("payamount", payamount + "");
        Param parampaytype = new Param("paytype", paytype + "");


        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.APPLY_ACITVE, stringCallback, paramUserid, paramactiveid, parampaynum, parampayamount, parampaytype, paramtelphone, paramweixin, paramcompany, paramposition, paramtickettypeid, paramrealname);

    }

    @Override
    public void Ticketinfo(int userid, int activeid, StringCallback stringCallback) {
        Param paramuserid = new Param("userid", userid + "");
        Param paramactiveid = new Param("activeid", activeid + "");

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_TICKETINFO, stringCallback, paramuserid, paramactiveid);
    }

    @Override
    public void updateTicketNumInfo(int activeid, int num, float payamount, String ticket, StringCallback stringCallback) {

        Param paramnum = new Param("num", num + "");
        Param parampayamount = new Param("payamount", payamount + "");
        Param paramticket = new Param("ticket", ticket);

        OkHttpClientManager.getInstance();
        OkHttpClientManager.postAsyn(UrlsManager.URL_UPDATA_TICKETINFO, stringCallback, paramnum, parampayamount, paramticket);

    }

    @Override
    public void updateImage(List<String> strings, StringCallback stringCallback) {
        OkHttpClientManager.getInstance();
        try {

            Utiles.log("strings:"+strings.toString());
            String[] key =new String[strings.size()];
            File[] files =new File[key.length];
            for (int i = 0; i < key.length; i++) {
                key[i]= strings.get(i);
                File file = new File(key[i]);
                files[i]= file;
                Utiles.log("key::"+key[i].toString());
            }

            OkHttpClientManager.postAsyn(UrlsManager.URL_EDITOR_POHOT_CHANGE, stringCallback, files,key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择验票活动
     * @param userid   当前登录用户id
     * @param stringCallback 回调
     */
    @Override
    public void selectTicketAction(int userid, StringCallback stringCallback) {
        OkHttpClientManager.getInstance();
        OkHttpClientManager.Param userid1  = new OkHttpClientManager.Param("userid",userid+"");
        OkHttpClientManager.postAsyn(UrlsManager.URL_SELECT_TICKET_ACTION,stringCallback,userid1);
    }

    /**
     * 刷脸验票接口
     * @param activeid    活动ID
     * @param group_uid    报名活动_组成员的用户Id
     * @param stringCallback   回调
     */
    @Override
    public void checkFace(int activeid, String group_uid, StringCallback stringCallback) {
        OkHttpClientManager.getInstance();
        OkHttpClientManager.Param activeid1  = new OkHttpClientManager.Param("activeid",activeid+"");
        OkHttpClientManager.Param group_uid1  = new OkHttpClientManager.Param("group_uid",group_uid+"");
        OkHttpClientManager.postAsyn(UrlsManager.URL_CHECK_FACE,stringCallback,activeid1,group_uid1);
    }
}
