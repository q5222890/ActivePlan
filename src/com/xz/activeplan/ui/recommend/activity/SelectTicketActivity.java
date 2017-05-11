package com.xz.activeplan.ui.recommend.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.ActiveinfosBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.MipcaActivityCapture;
import com.xz.activeplan.ui.recommend.adapter.RecyclingPagerAdapter;
import com.xz.activeplan.ui.recommend.adapter.ScalePageTransformer;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.TimeUtils;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.ClipViewPager;
import com.xz.activeplan.views.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 *选择验票活动
 */
public class SelectTicketActivity extends BaseFragmentActivity {

    private static final String TAG = "SelectTicketActivity";
    private ClipViewPager mViewPager;
    private MyAdapter mPagerAdapter;
    private int userid;
    private List<ActiveinfosBean> activeinfosBeanList= new ArrayList<>();
    private CustomProgressDialog mProgressDialog;
    private LinearLayout llNone;  //没有数据页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utiles.setStatusBar(this);
        setContentView(R.layout.activity_select_ticket);
        initView();
        if(SharedUtil.isLogin(this)){
            Utiles.log(TAG +"     "+SharedUtil.isLogin(this));
            userid = SharedUtil.getUserInfo(this).getUserid();
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mProgressDialog  = new CustomProgressDialog(this);
        Utiles.headManager(this,"选择验票活动");
        mViewPager = (ClipViewPager) findViewById(R.id.select_viewPager);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        findViewById(R.id.page_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
        llNone= (LinearLayout) findViewById(R.id.select_llNone);
        mPagerAdapter = new MyAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);

        initData();
    }
    private void initData() {

        if (!NetworkInfoUtil.checkNetwork(this)) {
            ToastUtil.showShort("网络无连接，请检查网络!");
            return;
    }
        activeinfosBeanList = (List<ActiveinfosBean>) getIntent().getSerializableExtra("data");
        Utiles.log(TAG+"集合数据："+activeinfosBeanList.toString());
        if(activeinfosBeanList!=null && activeinfosBeanList.size()==0)
        {
            llNone.setVisibility(View.VISIBLE);
            return;
        }else
        {
            mViewPager.setOffscreenPageLimit(activeinfosBeanList.size());
            mPagerAdapter.addAll(activeinfosBeanList);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(activeinfosBeanList.size()>0)
        {
            activeinfosBeanList.clear();
        }
        if(mProgressDialog!=null)
        mProgressDialog.dismiss();
    }

    /**
     * viewPager 适配器
     */
    public static class MyAdapter extends RecyclingPagerAdapter {

        private  List<ActiveinfosBean> mList;
        private  Context mContext;

        public MyAdapter(Context context) {
            mList = new ArrayList<>();
            mContext = context;
        }

        public void addAll(List<ActiveinfosBean> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            ActiveinfosBean bean = mList.get(position);
            Utiles.log(TAG+"---"+bean.toString());
            ViewHolder viewHolder= null;
            if(convertView==null)
            {
                viewHolder =new ViewHolder();
                convertView= LayoutInflater.from(mContext).inflate(R.layout.activity_select_ticket_viewpager_item,null);
                viewHolder.btnGo= (Button) convertView.findViewById(R.id.select_item_btnGo);
                viewHolder.ivCover= (ImageView)convertView.findViewById(R.id.select_item_ivCover);
                viewHolder.tvAddress= (TextView)convertView.findViewById(R.id.select_item_tvAddress);
                viewHolder.tvMoney= (TextView) convertView.findViewById(R.id.select_item_tvMoney);
                viewHolder.tvTime= (TextView)convertView.findViewById(R.id.select_item_tvTime);
                viewHolder.tvTitle= (TextView) convertView.findViewById(R.id.select_item_tvTitle);
                container.setTag(convertView);
            }else
            {
               viewHolder = (ViewHolder) convertView.getTag();
            }
            //封面
            Picasso.with(mContext).load(bean.getActiveurl()).centerCrop().fit().error(R.drawable.default_details_image)
                    .placeholder(R.drawable.default_details_image).into(viewHolder.ivCover);
            //标题
            viewHolder.tvTitle.setText(bean.getName());
            //开始时间
            String time = TimeUtils.formatTime1(bean.getStartdate()*1000);
            Utiles.log(TAG +"   时间："+time);
            viewHolder.tvTime.setText(time+" 开始");
            //地址
            viewHolder.tvAddress.setText(bean.getAddress());
            //金额(免费和收费)
           double money =  bean.getMoney();
            if(money==0.0)  //显示免费
                viewHolder.tvMoney.setText("免费");
            else
                viewHolder.tvMoney.setText("¥"+bean.getMoney()+"");
            //去验票
            viewHolder.btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ScanActivity.class);
//                    intent.putExtra("data",mList.get(position));
//                    mContext.startActivity(intent);
                    Intent intent = new Intent(mContext, MipcaActivityCapture.class);
                    intent.putExtra("data",mList.get(position));
                    mContext.startActivity(intent);
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }

    private static class  ViewHolder
    {
        Button btnGo;
        ImageView ivCover;
        TextView tvAddress,tvMoney,tvTime,tvTitle;
    }

}
