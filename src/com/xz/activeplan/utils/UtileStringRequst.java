package com.xz.activeplan.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**访问网络返回对象
 */
public class UtileStringRequst<T_C> {
    private static UtileStringRequst instance = null;
    public  List<T_C>list;
    public T_C beanItem;
    public UtileStringRequst () {
    }

    public static UtileStringRequst getInstance(){
        if (instance==null){
            instance = new UtileStringRequst();
        }
        return instance;
    }

    /**
     * 返回集合
     * @param context
     * @param url
     * @param t_c
     * @param handler
     * @param i
     */
    public  void UtileStringRequst(final Context context, String url, final Class<T_C> t_c, final Handler handler, final int i) {
        this.list=new ArrayList<T_C>();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (TextUtils.isEmpty(s)){
                    Utiles.toast(context,"数据为空！");
                    return;
                }
                Utiles.log("视频信息："+s);
                RealizationBean.getInstance();
                RealizationBean realizationBean = JSON.parseObject(s, RealizationBean.class);
                if (realizationBean.getData()!=null) {
                    list = (List<T_C>) JSON.parseArray(realizationBean.getData(), t_c);
                    handler.sendEmptyMessage(i);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"加载失败！",Toast.LENGTH_LONG).show();
            }
        });
        if (context!=null) {
            Volley.newRequestQueue(context).add(stringRequest);
        }
    }


    public  void UtileStringRequstBean(final Context context, String url, final Class<T_C> t_c, final Handler handler, final int i) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (TextUtils.isEmpty(s)) {
                    Utiles.toast(context, "数据为空！");
                    return;
                }
                RealizationBean.getInstance();
                RealizationBean realizationBean = JSON.parseObject(s, RealizationBean.class);
                if (realizationBean.getData() != null) {
                    beanItem = JSON.parseObject(realizationBean.getData(), t_c);
                    handler.sendEmptyMessage(i);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort("加载失败");
            }
        });
        if (context != null) {
            Volley.newRequestQueue(context).add(stringRequest);
        }

    }
    private static class RealizationBean implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private static RealizationBean realizationBean = null;
        private int code;
        private String msg;
        private String data;
        public RealizationBean() {

        }
        public RealizationBean(int code, String msg, String data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

        public static RealizationBean getInstance(){
            if (realizationBean==null){
                realizationBean = new RealizationBean();
            }
            return realizationBean;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String
        toString() {
            return "StatusBean{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }
    }
}
