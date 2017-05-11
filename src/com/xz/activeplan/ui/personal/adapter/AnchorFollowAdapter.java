package com.xz.activeplan.ui.personal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.XZApplication;
import com.xz.activeplan.entity.FollowAnchorBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.live.view.AlertDialog;
import com.xz.activeplan.utils.NetworkInfoUtil;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.views.CircleImageView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class AnchorFollowAdapter extends BaseAdapter {
    private List<FollowAnchorBean> followList;
    private Context context;

    public AnchorFollowAdapter(Context context, List<FollowAnchorBean> followList) {
        this.context = context;
        this.followList = followList;
    }

    @Override
    public int getCount() {
        return followList.size();
    }

    @Override
    public Object getItem(int position) {
        return followList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.listviewitem_followanchor, null);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FollowAnchorBean bean = (FollowAnchorBean) getItem(position);
        if (!TextUtils.isEmpty(bean.getHeadurl())) {
            Picasso.with(context).load(bean.getHeadurl())
                    .error(R.drawable.default_head_bg)
                    .placeholder(R.drawable.default_head_bg).fit().centerCrop()
                    .into(holder.circleImageView);
        } else {
            Picasso.with(context).load(R.drawable.default_head_bg)
                    .error(R.drawable.default_head_bg)
                    .placeholder(R.drawable.default_head_bg).fit().centerCrop()
                    .into(holder.circleImageView);
        }
        if (!TextUtils.isEmpty(bean.getUsername())) {
            holder.username.setText(bean.getUsername());
        }
        if (!TextUtils.isEmpty(bean.getLivecount() + "")) {
            holder.videonum.setText(bean.getLivecount() + "");
        }
        if (!TextUtils.isEmpty(bean.getFanscount() + "")) {
            holder.follownum.setText(bean.getFanscount() + "");
        }
        holder.tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utiles.log("点击的对象："+bean.toString());
                showDialog(bean);
            }
        });
        return convertView;
    }

    /**
     * 提示是否取消关注
     * @param
     */
    private void showDialog(final FollowAnchorBean bean) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_yesorno, null);
        final AlertDialog dialog = new AlertDialog(context, view, 0.7f).builder();
        try {
            dialog.show();

            dialog.setCancelable(false);
            view.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
            view.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.txt_msg)).setText("您确定要取消关注吗?");
            dialog.setPositiveButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            dialog.setNegativeButton("确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancleFollow(bean);
                    dialog.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消关注
     * @param bean
     */
    private void cancleFollow(final FollowAnchorBean bean) {

        if (!NetworkInfoUtil.checkNetwork(XZApplication.getInstance())) {
            ToastUtil.showShort("网络无连接，请检查网络!");
            return;
        }
        int userid = SharedUtil.getUserInfo(context).getUserid();
        UserInfoServiceImpl.getInstance().cancelFollowPerson(userid, bean.getUserid(), new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                Utiles.log("取消关注： "+response);
                StatusBean statusBean1 = JSON.parseObject(response, StatusBean.class);
                if (statusBean1.getCode() == 0) {

                    ToastUtil.showCenterToast(context,"取消关注成功");
                    followList.remove(bean);
                    notifyDataSetChanged();
                } else {
                    Utiles.loadFailed();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                //ToastUtil.showShort("取消关注失败!") ;
            }
        });
    }

}

class ViewHolder {
    CircleImageView circleImageView;
    TextView username, videonum, follownum, tvFollow;

    public ViewHolder(View view) {
        circleImageView = (CircleImageView) view.findViewById(R.id.circleimgview_followanchor);
        username = (TextView) view.findViewById(R.id.tv_username);
        videonum = (TextView) view.findViewById(R.id.tv_videonum);
        follownum = (TextView) view.findViewById(R.id.tv_followingnum);
        tvFollow = (TextView) view.findViewById(R.id.tv_follow);
        view.setTag(this);
    }
}

