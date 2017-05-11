package com.xz.activeplan.ui.personal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.ui.find.adapter.commonAdapter.CommonAdapter;
import com.xz.activeplan.ui.find.adapter.commonAdapter.ViewHolder;
import com.xz.activeplan.ui.recommend.fragment.RecommendFragment;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.ClassObserver;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

import java.util.ArrayList;

public class ListMessageActivity extends Activity implements ClassObserver, View.OnClickListener {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_message);
        ClassConcrete.getInstance().addObserver(this) ;
        initView();
    }

    private void initView() {
        findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(this);
        ((TextView)findViewById(R.id.id_TextViewHeadTitle)).setText("活动消息");
        listView = (ListView) findViewById(R.id.id_ListViewListMessageActivity);
        ArrayList<ActiveinfosBean> listActiveinfosBean = RecommendFragment.listActiveinfosBean;
        new CommonAdapter<ActiveinfosBean>(this,listActiveinfosBean,R.layout.conversation_list_item) {
            @Override
            public void convert(ViewHolder holder, ActiveinfosBean activeinfosBean, int position) {

            }
        };
        Utiles.log("list.size:"+listActiveinfosBean.size());



    }
        @Override
    protected void onDestroy() {
        ClassConcrete.getInstance().removeObserver(this) ;
        super.onDestroy();
    }

    @Override
    public boolean onDataUpdate(Object data) {
        EventBean event = (EventBean)data ;
        if(event.getType() == EventType.QUITE_CHART_TYPE){
            finish() ;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_ImageHeadTitleBlack:
                this.finish();
                break;
        }
    }
}
