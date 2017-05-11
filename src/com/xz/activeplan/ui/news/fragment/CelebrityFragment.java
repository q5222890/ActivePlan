package com.xz.activeplan.ui.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.squareup.okhttp.Request;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.CelebriteAndAreaBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.TeacherBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.teacher.impl.CelebrityNewWorkImpl;
import com.xz.activeplan.ui.BaseFragment;
import com.xz.activeplan.ui.news.activity.CelebrityActivity;
import com.xz.activeplan.ui.news.activity.SearchTeacherActivity;
import com.xz.activeplan.ui.news.activity.SettledActivity;
import com.xz.activeplan.ui.news.adapter.TeacherGridViewAdapter;
import com.xz.activeplan.ui.news.adapter.TeacherListViewAdapter;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;
import com.xz.activeplan.views.CustomProgressDialog;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 名人堂——名人
 */
public class CelebrityFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private ListView listView;
    private PullToRefreshGridView gridView;
    private TeacherListViewAdapter listViewAdapter;
    private TeacherGridViewAdapter gridViewAdapter;
    private ArrayList<CelebriteAndAreaBean> listCelebtiby = new ArrayList<CelebriteAndAreaBean>();
    private ArrayList<CelebriteAndAreaBean> listField = new ArrayList<CelebriteAndAreaBean>();

    private ArrayList<CelebriteAndAreaBean> list = new ArrayList<CelebriteAndAreaBean>();
    private ArrayList<TeacherBean> datas = new ArrayList<>();
    private CustomProgressDialog mProgressDialog;
    private EditText phone, name, content;
    private Button btSubmit;
    private TextView textViewCelebrity, textViewFiele;
    private String[] arrayField, arraysCelebrity;
    private TextView textView;
    private ImageView ivTeacherSearcher;
    private int select = -1;
    private int x;
    private String code01;
    private String code02;
    private int y;
    private SimpleDateFormat formatTime;
    private Date date;
    private String timeUpStr;
    private String timeDolweStr;
    private int page = 0;
    private boolean pullResh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_celebrity, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        mProgressDialog = new CustomProgressDialog(getActivity());
        listView = (ListView) view.findViewById(R.id.id_XListview);
        gridView = (PullToRefreshGridView) view.findViewById(R.id.ptr_gridview);
        getTime("onPullDownToRefresh");
        getTime("onPullUpToRefresh");
        textViewCelebrity = (TextView) view.findViewById(R.id.id_TextViewCelebrity);//名人按键
        textViewFiele = (TextView) view.findViewById(R.id.id_TextViewField);//场地按键
        ivTeacherSearcher = (ImageView) view.findViewById(R.id.ivTeacherSearcher);
        view.findViewById(R.id.id_ImageSettled).setOnClickListener(this);
        textViewCelebrity.setOnClickListener(this);
        textViewFiele.setOnClickListener(this);
        ivTeacherSearcher.setOnClickListener(this);
        listCelebtiby.clear();
        listField.clear();
        /***
         * 得到类型 例如：互联网 户外 汽车 等等
         */
        arraysCelebrity = getResources().getStringArray(R.array.Arrays_celebrity);//名人
        arrayField = getResources().getStringArray(R.array.Arrays_field);//场地
        //加载网络名人
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setNull();
                datas.clear();
                listViewAdapter.setSelectIndex(position);
                if (select == 1) {
                    x = position;
                } else {
                    y = position;
                }

                loadData(list.get(position).getCode());
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),
                        CelebrityActivity.class);

                intent.putExtra("data", gridViewAdapter.getItem(position));
                getActivity().startActivity(intent);
            }
        });
        gridViewAdapter = new TeacherGridViewAdapter(getActivity(), datas);
        list.addAll(listCelebtiby);
        listViewAdapter = new TeacherListViewAdapter(getActivity(), list);
        listView.setAdapter(listViewAdapter);
        gridView.setAdapter(gridViewAdapter);
        onClick(textViewCelebrity);
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                gridView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeDolweStr);
                gridView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                int time = getTime("onPullDownToRefresh");
                    if (select == 1) {
                        datas.clear();
                        if (listCelebtiby != null) {
                            loadData(listCelebtiby.get(0).getCode());
                        }
                    } else {
                        datas.clear();
                        if (listField != null) {
                            loadData(listField.get(0).getCode());
                        }
                    }
                    setNull();
                gridView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                // 刷新label的设置
                gridView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间:" + timeUpStr);
                gridView.getLoadingLayoutProxy().setPullLabel("上拉刷新");
                gridView.onRefreshComplete();
                getTime("onPullUpToRefresh");
                if (select == 1 && pullResh == true) {
                    loadData(code01);
                } else if (select == 2 && pullResh == true) {
                    loadData(code02);
                }

                gridView.onRefreshComplete();
            }
        });
    }

    private void setNull() {
        page = 0;
        formatTime = null;
        date = null;
        timeUpStr = "";
        timeDolweStr = "";
        pullResh = true;
    }

    /***
     * 获取名人清单
     */
    private void getCelebriList() {
//        mProgressDialog.show();
        CelebrityNewWorkImpl.getInstance().getCelebriteList(new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
//                Utiles.loadFailed();
//                mProgressDialog.dismiss();
                ToastUtil.showShort("获取数据失败");
            }

            @Override
            public void onResponse(String response) {

//                mProgressDialog.dismiss();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean.getCode() == 0) {
                    if (!TextUtils.isEmpty(statusBean.getData())) {
                        List<CelebriteAndAreaBean> celebriteAndAreaBeen = JSON.parseArray(statusBean.getData(), CelebriteAndAreaBean.class);
                        if (celebriteAndAreaBeen.size() != 0) ;
                        listCelebtiby.clear();
                        listCelebtiby.addAll(celebriteAndAreaBeen);
                        list.clear();
                        list.addAll(listCelebtiby);
                        listViewAdapter.notifyDataSetChanged();
                        if (list.size() != 0)
                            loadData(list.get(0).getCode());
                    }
                } else {
                    Utiles.loadFailed();
                }

            }
        });
    }

    /***
     * 场地
     */
    private void getAreaList() {
//        mProgressDialog.show();
        CelebrityNewWorkImpl.getInstance().getAreaList(new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Utiles.loadFailed();
//                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(String response) {
//                mProgressDialog.dismiss();
                Utiles.log("获取场地response：" + response);
                //这里报json解析异常 无法获取回应
                if (!TextUtils.isEmpty(response)) {
                    StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                    if (statusBean.getCode() == 0) {
                        Utiles.log("获取场地statusbean.getdata：" + statusBean.getData());
                        if (!TextUtils.isEmpty(statusBean.getData())) {
                            List<CelebriteAndAreaBean> celebriteAndAreaBeen = JSON.parseArray(statusBean.getData(), CelebriteAndAreaBean.class);
                            if (celebriteAndAreaBeen.size() != 0) {
                                listField.addAll(celebriteAndAreaBeen);
                                list.clear();
                                list.addAll(listField);
                                loadData(list.get(0).getCode());
                                listViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } else {
                    Utiles.loadFailed();
                }
            }
        });
    }

    /**
     * 访问网络
     *
     * @param categoryid
     */
    private void loadData(String categoryid) {
        if (select == 1) {
            code01 = categoryid;
        } else {
            code02 = categoryid;
        }
//        mProgressDialog.show();
        //类别Id ，当前页数 ，返回数量，回调接口OkHttp客户端管理，String回调
        CelebrityNewWorkImpl.getInstance().getCelebrity(categoryid, page, 20, new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
//                mProgressDialog.dismiss();
                StatusBean statusBean = JSON.parseObject(response, StatusBean.class);
                if (statusBean != null && statusBean.getCode() == 0) {
                    Utiles.log(" date:aaaa" + statusBean.getData());
                    List<TeacherBean> teacherBeans = JSON.parseArray(statusBean.getData(), TeacherBean.class);
                    if (teacherBeans.size() >= 20) {
                        page++;
                        pullResh = true;
                    } else {
                        pullResh = false;
                    }
                    datas.addAll(teacherBeans);
                    gridViewAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showShort("数据获取失败，请刷新重试!");
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
//                mProgressDialog.dismiss();
                ToastUtil.showShort("数据获取失败，请刷新重试!");
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_TextViewCelebrity://点击名师
                datas.clear();
                gridViewAdapter.notifyDataSetChanged();
                select = 1;
                changeTextColor(textViewCelebrity);
                list.clear();
                if (listCelebtiby.size() == 0) {
                    Utiles.log("-------01>" + listCelebtiby.size());
                    getCelebriList();
                    return;
                } else {
                    Utiles.log("-------02>" + listCelebtiby.size());
                }
                setNull();
                list.addAll(listCelebtiby);
                listViewAdapter.setSelectIndex(Integer.parseInt(x + ""));
                listViewAdapter.notifyDataSetChanged();
                loadData(code01);

                break;
            case R.id.id_TextViewField://点击场地
                datas.clear();
                gridViewAdapter.notifyDataSetChanged();
                select = 2;
                changeTextColor(textViewFiele);
                if (listField.size() == 0) {
                    getAreaList();
                    return;
                }
                setNull();
                list.clear();
                list.addAll(listField);
                listViewAdapter.setSelectIndex(Integer.parseInt(y + ""));
                listViewAdapter.notifyDataSetChanged();
                loadData(code02);

                break;
            case R.id.id_ImageSettled://入住
                ToastUtil.showShort(" In development...");
                if (0 != 0) {
                    Utiles.skipNoData(SettledActivity.class);
                }
                break;

            case R.id.ivTeacherSearcher:
                startActivity(new Intent(getActivity(), SearchTeacherActivity.class));
                break;
        }

    }

    /**
     * 改变字体颜色
     *
     * @param tv
     */
    private void changeTextColor(TextView tv) {
        if (textView != tv) {
            tv.setSelected(true);
            tv.setTextColor(getResources().getColor(R.color.header_blue));
            if (textView != null) {
                textView.setSelected(false);
                textView.setTextColor(getResources().getColor(R.color.white));
            }
            textView = tv;
        }
    }


    /**
     * 检查入驻信息
     */
    private void checkEnterInfo() {
        String name1 = name.getText().toString().trim();
        String phone1 = phone.getText().toString().trim();
        if (name1.length() == 0) {
            ToastUtil.showShort("姓名不能为空");
            return;
        }
        if (phone1.length() == 0) {
            ToastUtil.showShort("手机号不能为空！");
            return;
        }
        if (Utils.checkMobileNumber(phone1) == false) {
            ToastUtil.showShort("手机号码格式错误！");
            return;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 获取刷新时间
     */
    private int getTime(String RefreshTime) {
        long time = System.currentTimeMillis();
        Date d1 = new Date(time);
        if (formatTime == null) {
            formatTime = new SimpleDateFormat("HH:mm:ss");
            date = d1;
        }
        String timeStr = formatTime.format(d1);
        if (RefreshTime.equals("onPullUpToRefresh")) {
            timeUpStr = timeStr;
        } else {
            long startTime = date.getTime();
            int i = (int) ((time - startTime) / 1000);
            if (i > 30) {
                date = d1;
            }
            Utiles.log(" Tiem:" + i);
            timeDolweStr = timeStr;
            return i;
        }
        return 0;
    }

}
