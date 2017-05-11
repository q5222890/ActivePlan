package com.xz.activeplan.ui.live.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.live.adapter.SearchAutoAdapter;
import com.xz.activeplan.ui.live.view.FlowLayout;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 直播搜索页面
 */
public class LiveSearchActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    public static final String SEARCH_HISTORY = "search_history";
    private ImageView iv_live_searchback, iv_searchmagnifier;
    private EditText edt_livesearch;
    private ListView listView;
    private SearchAutoAdapter adapter;
    private LinearLayout clearAll;
    private FlowLayout flowLayout;
    private String[] mVals = new String[]
            { "约吧互动","约吧互动","约吧互动","约吧","约吧互动","约吧互动","约吧互动" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_live_search);
        initView();
        initData();
    }

    private void initData() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        for (int i = 0; i < mVals.length; i++)
        {
            TextView tv = (TextView) mInflater.inflate(R.layout.view_textview,flowLayout, false);
            tv.setText(mVals[i]);
            flowLayout.addView(tv);
        }
    }

    private void initView() {
        iv_live_searchback = (ImageView) findViewById(R.id.id_ImageHeadTitleBlack);//返回键
        iv_searchmagnifier = (ImageView) findViewById(R.id.imgsearch); //搜索
        edt_livesearch = (EditText) findViewById(R.id.edt_search);
        listView = (ListView) findViewById(R.id.liveSearch_listView);
        clearAll = (LinearLayout) findViewById(R.id.liveSearch_llClear);
        adapter = new SearchAutoAdapter(this, 10);  //最多10条数据
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        flowLayout  = (FlowLayout) findViewById(R.id.id_flowlayout);
        iv_live_searchback.setOnClickListener(this);
        iv_searchmagnifier.setOnClickListener(this);
        edt_livesearch.setOnClickListener(this);
        clearAll.setOnClickListener(this);
        /**监听键盘的搜索键*/
        edt_livesearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 隐藏键盘
                    String search = edt_livesearch.getText().toString().trim();
                    if (TextUtils.isEmpty(search)) {
//						Toast.makeText(SearchActivity.this, "请输入关键字" , Toast.LENGTH_LONG).show();
                        ToastUtil.showShort("请输入关键字");
                    } else {
                        saveSearchHistory();
                        adapter.initSearchHistory();
                        adapter.notifyDataSetChanged();
                        Intent intent = new Intent(LiveSearchActivity.this, LiveSearchResultActivity.class);
                        intent.putExtra("searchname", search);
                        startActivity(intent);
                       // finish();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_ImageHeadTitleBlack:  //返回
                finish();
                break;
            case R.id.imgsearch:    //搜索
                String search = edt_livesearch.getText().toString().trim();
                if (TextUtils.isEmpty(search)) {
//					Toast.makeText(SearchActivity.this, "请输入关键字" , Toast.LENGTH_LONG).show();
                    ToastUtil.showShort("请输入关键字");
                } else {
                    saveSearchHistory();
                    adapter.initSearchHistory();
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent(LiveSearchActivity.this,
                            LiveSearchResultActivity.class);
                    intent.putExtra("searchname", search);
                    startActivity(intent);
                  //  finish();
                }
                break;
            case R.id.liveSearch_llClear:
                adapter.cleanHistory();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String data = (String) adapter.getItem(position);
        edt_livesearch.setText(data);
        iv_searchmagnifier.performClick();
    }

    /*
     * 保存搜索记录
	 */
    private void saveSearchHistory() {
        String text = edt_livesearch.getText().toString().trim();
        if (text.length() < 1) {
            return;
        }
        SharedPreferences sp = getSharedPreferences(SEARCH_HISTORY, 0);
        String longhistory = sp.getString(SEARCH_HISTORY, "");
        String[] tmpHistory = longhistory.split(",");
        ArrayList<String> history = new ArrayList<String>(
                Arrays.asList(tmpHistory));
        //检查历史记录是否已经存在当前输入的text，如果存在则删除
        if (history.size() > 0) {
            int i;
            for (i = 0; i < history.size(); i++) {
                if (text.equals(history.get(i))) {
                    history.remove(i);
                    break;
                }
            }
            //如果记录大于4个，则移除最后一个数据再在最前面增加一个数据
            if (history.size() > 9) {
                history.remove(history.size() - 1);
            }
            history.add(0, text);
        }

        //重新加，提交
        if (history.size() > 0) //history.size()>1和history.size()>0一样
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < history.size(); i++) {
                sb.append(history.get(i) + ",");//这句一开始添加一个数据时加了两个,	why？ 用String也是 因为""
            }
            sp.edit().putString(SEARCH_HISTORY, sb.toString()).commit();
        } else {
            sp.edit().putString(SEARCH_HISTORY, text + ",").commit();
        }
    }
}
