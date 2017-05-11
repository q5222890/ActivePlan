package com.xz.activeplan.ui.find.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.db.VideoCollectDao;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.find.adapter.VideoCollectAdapter;
import com.xz.activeplan.ui.live.activity.TestChatRoomActivity;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频收藏页面
 */
public class VideoCollectActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "VideoCollectActivity";
    public List<LiveTvBean> list = new ArrayList<>();
    public TextView tvSave;
    GridView gridView;
    private VideoCollectDao collectDao;  //收藏dao
    private View emptyView;
    private LinearLayout llEdit;
    private boolean flag;
    private VideoCollectAdapter adapter;
    private ArrayList<Boolean> selectItems= new ArrayList<>(); //用于存储已选中项目的位置
    private boolean isState;
    private TextView tvCheckAll;
    private TextView tvDelete;
    private CheckBox checkBox;
    private List  liveIdList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_collect);
        Utiles.setStatusBar(this);
        initView();
        initData();
    }

    private void initData() {
        collectDao = new VideoCollectDao(this);
        List<LiveTvBean> collectDaoAll = collectDao.findAll();
        Utiles.log(TAG+"--------"+collectDaoAll.toString());
        list.addAll(collectDaoAll);  // 获取所有收藏的视频集合
        if(list.size()==0)
        {
            gridView.setEmptyView(emptyView);
        }
        for (int i = 0; i < list.size(); i++) {
            selectItems.add(false);
            //liveIdList.add(list.get(i).getLiveid());
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        emptyView = (View) findViewById(R.id.iv_noCollectView);  //无数据显示页
        ((TextView) findViewById(R.id.id_TextViewHeadTitle)).setText("我的收藏");
        tvSave = (TextView) findViewById(R.id.tvLoginAndReg);
        tvSave.setText("编辑");
        tvSave.setTextSize(16);
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setOnClickListener(this);

        ((ImageView) findViewById(R.id.id_ImageHeadTitleBlack)).setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        //gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

        adapter = new VideoCollectAdapter(this,list);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(this);
        llEdit = (LinearLayout)findViewById(R.id.llEdit);
        tvCheckAll = (TextView)findViewById(R.id.tvCheckAll);
        tvDelete = (TextView)findViewById(R.id.tvDelete);
        tvCheckAll.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        llEdit.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.id_ImageHeadTitleBlack:   //返回
                finish();
                break;
            case R.id.tvLoginAndReg:   //编辑
                if(list.size()==0)
                {
                    ToastUtil.showCenterToast(this,"您尚未收藏视频！");
                    return;
                }
                if(!flag)
                {
                    flag = true;
                    tvSave.setText("取消编辑");
                    llEdit.setVisibility(View.VISIBLE);
                    adapter.setCheckBoxVisibility(true);
                    setState(true);
                    adapter.setIsState(true);
                    setSelectNum();
                }else
                {
                    flag= false;
                    tvSave.setText("编辑");
                    llEdit.setVisibility(View.GONE);
                    adapter.setCheckBoxVisibility(false);
                }

                break;
            case R.id.tvCheckAll: //全选
                if(!flag)
                {
                    flag = true;
                    tvCheckAll.setText("全不选");
                    tvDelete.setText("删除");
                    setSelectAll(true);
                }else
                {
                    flag = false;
                    tvCheckAll.setText("全选");
                    setSelectAll(false);
                }
                break;
            case R.id.tvDelete:   //删除
                delSelections();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       if(tvSave.getText().toString().equals("取消编辑"))  //编辑状态
       {
           Utiles.log("-----编辑状态---");
           checkBox = (CheckBox) view.findViewById(R.id.check_box);
           if (isState) {

               if (checkBox.isChecked()) {
                   checkBox.setChecked(false);
                   selectItems.set(position, false);
               } else {
                   checkBox.setChecked(true);
                   selectItems.set(position, true);
               }
               adapter.notifyDataSetChanged();
               setSelectNum();
           }
       }else   //非编辑状态（点击跳转到视频播放页面）
       {
           Utiles.log("-----非编辑状态---");
           Utiles.skipActivityForLiveTvBean(TestChatRoomActivity.class,list.get(position));
       }
    }
    //设置当前状态 是否在多选模式
    private void setState(boolean b) {
        isState = b;
        if (b) {

        } else {

        }
    }

    public ArrayList<Boolean> getSelectItems() {
        return selectItems;
    }
    //显示已选项数目
    private void setSelectNum() {
        int num = 0;
        for (Boolean b : selectItems) {
            if (b)
                num ++;
        }
        tvDelete.setText("删除(" + num+")" );
    }

    //全选
    private void setSelectAll(boolean b) {
        Utiles.log("----size--"+selectItems.size());
        for (int i = 0; i < selectItems.size(); i++) {
            selectItems.set(i, b);
            adapter.notifyDataSetChanged();
            setSelectNum();
        }
    }
    //删除
    private void delSelections() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_yesorno, null);
        final com.xz.activeplan.ui.live.view.AlertDialog dialog = new com.xz.activeplan.ui.live.view.AlertDialog(this, view,0.7f).builder();

        if(!selectItems.contains(true))
        {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
               Utiles.log("");
            }
            dialog.setCancelable(false);
            view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
            view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
            view.findViewById(R.id.img_line).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txt_msg)).setText("当前没有选中删除项！");
            dialog.setPositiveButton("确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

        }else
        {
            dialog.show();
            view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
            view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.txt_msg)).setText("确认删除所选项目？");
            dialog.setPositiveButton("确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < list.size(); i++) {
                        if (selectItems.get(i)) {
                            liveIdList.add(list.get(i).getLiveid());

                            collectDao.deleteData(liveIdList);

                            Utiles.log("------liveId-----000---"+liveIdList.toString());
                            list.set(i, null); //将选中的设置为空
                        }
                    }
                    while (list.contains(null)) {
                        list.remove(null);
                    }
                    selectItems = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        selectItems.add(false); //剩余的
                        tvDelete.setText("删除(0)");

                    }
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                    if (list.size() == 0) {
                        adapter.setIsState(false);
                        setState(false);
                        tvDelete.setText("删除");
                        tvSave.setText("编辑");
                        llEdit.setVisibility(View.GONE);
                        gridView.setEmptyView(emptyView);
                        ToastUtil.showCenterToast(VideoCollectActivity.this,"您尚未收藏视频！");
                        return;
                    }

                }
            });
            dialog.setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


    }
}
