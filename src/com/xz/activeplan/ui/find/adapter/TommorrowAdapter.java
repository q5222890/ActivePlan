package com.xz.activeplan.ui.find.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xz.activeplan.R;
import com.xz.activeplan.entity.LiveTvBean;
import com.xz.activeplan.entity.StatusBean;
import com.xz.activeplan.entity.UserInfoBean;
import com.xz.activeplan.json.StatusJson;
import com.xz.activeplan.network.OkHttpClientManager;
import com.xz.activeplan.network.personal.impl.UserInfoServiceImpl;
import com.xz.activeplan.ui.find.activity.TommorrowPersonActivity;
import com.xz.activeplan.ui.live.test.LiveChatRoomFragment;
import com.xz.activeplan.ui.live.test.MyRongIM;
import com.xz.activeplan.ui.personal.activity.LoginActivity;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.ToastUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.Utils;

import java.io.IOException;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 推荐活动适配器
 * 预约字体要改一下颜色
 */
public class TommorrowAdapter extends BaseAdapter implements OnClickListener {
	private Context context;
	private List<LiveTvBean> list;
	private boolean isattend = false;
	private UserInfoBean myUserInfoBean;
	private LiveTvBean bean;

	public TommorrowAdapter(Context context, List<LiveTvBean> lists) {
		this.context = context ;
		this.list = lists ;
		Resources res = this.context.getResources();
	}

	@Override
	public int getCount() {
		if(list == null){
			return 0 ;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if(list == null){
			return null;
		}
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context.getApplicationContext(), R.layout.tomorrow_list_item, null);
			new ViewHolder(convertView);
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		bean = (LiveTvBean) getItem(position);
		Picasso.with(context).load(bean.getCoverurl()).placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(holder.imageViewActivity);
		Picasso.with(context).load(bean.getHeadurl()).placeholder(R.drawable.thumb).error(R.drawable.thumb).fit().centerCrop().into(holder.imageViewHeadPhoto);
		holder.imageViewActivity.setScaleType(ImageView.ScaleType.FIT_XY);
		holder.textViewNum.setText(position + 1 + "");
		holder.textViewFlans.setText(context.getResources().getString(R.string.Flans)+bean.getFans());

		holder.textViewTommorrowContent.setText(bean.getTitle());
		if (position == 0 || position == 1) {
			holder.textViewNum.setTextColor(context.getResources().getColor(R.color.red2));
		} else {
			holder.textViewNum.setTextColor(context.getResources().getColor(R.color.black));
		}

		holder.imageViewHeadPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, TommorrowPersonActivity.class);
				intent.putExtra("data", (LiveTvBean) getItem(position));
				context.startActivity(intent);
			}
		});

		/**手机号码*/

		if (Utils.checkMobileNumber(bean.getUsername())==true)
		{
			String number = Utils.formatMobileNumber(bean.getUsername());
			holder.textViewTommorrowTitle.setText(number);
		}else
		{
			holder.textViewTommorrowTitle.setText(bean.getUsername());
		}

		/**视频*/
		 holder.llVideo.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View v) {
				  joinChatRoom((LiveTvBean) getItem(position));
			 }
		 });

		/**
		 * 关注和取消关注
		 */
		isattend = bean.isIsattend();
		if(isattend==true)
		{
			holder.btFollow.setText("已关注");
			holder.btFollow.setSelected(true);
		}else
		{
			holder.btFollow.setText("关注");
			holder.btFollow.setSelected(false);
		}
       holder.btFollow.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   if(holder.btFollow.getText().equals("关注")){   //false
				   holder.btFollow.setText("已关注");
				   holder.btFollow.setSelected(true);
				   follow(position,holder.btFollow) ;
				   isattend= true;
			   }else {
				   cancelFollow(position,holder.btFollow);
			   }
		   }
	   });
		return convertView;
	}

	private void joinChatRoom(final LiveTvBean bean) {
		UserInfoBean userInfo = SharedUtil.getUserInfo(context);
		RongIM.connect(userInfo.getToken(), new RongIMClient.ConnectCallback() {
			@Override
			public void onTokenIncorrect() {
				ToastUtil.showShort("用户未登录或用户TOKEN无效");
			}
			@Override
			public void onSuccess(String s) {

				Utiles.log("------tomorow----"+bean.toString());
				if(!TextUtils.isEmpty(bean.getUrl()))
				{
					Utiles.x=0;
					MyRongIM.getInstance().startConversation(context, Conversation.ConversationType.CHATROOM,"002");
					Message msg = new Message();
					msg.obj = bean;
					msg.what= LiveChatRoomFragment.LIVE_CHAT_ROOM_WHAT;
					LiveChatRoomFragment.LIVE_CHAT_ROOM_HANDLER.sendMessage(msg);
					Utiles.log("----------最新直播的播放地址");
				}else
				{
					ToastUtil.showShort("视频链接地址无效");
				}

			}
			@Override
			public void onError(RongIMClient.ErrorCode errorCode) {
				Utiles.log("-------连接失败");
			}
		});

	}

	/**
	 * 关注
     */
	private void follow(int position, final Button btFollow) {

		if(SharedUtil.isLogin(context)){
			myUserInfoBean = SharedUtil.getUserInfo(context);
		}else{
			Intent intent = new Intent(context,LoginActivity.class);
			context.startActivity(intent);
			return;
		}

		if(myUserInfoBean == null){
			return;
		}

		UserInfoServiceImpl.getInstance().followPerson(myUserInfoBean.getUserid(), bean.getUserid(), new OkHttpClientManager.StringCallback() {
			@Override
			public void onResponse(String response) {
				Utiles.log("-----------"+response);
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						ToastUtil.showShort("关注成功!") ;
					} else {
						ToastUtil.showShort("关注失败!") ;
					}
				}
			}
			@Override
			public void onFailure(Request request, IOException e) {
				//ToastUtil.showShort("关注失败!------shibai") ;
			}
		});
	}

	private void cancelFollow(int position, final Button btFollow) {
		if(SharedUtil.isLogin(context)){
			myUserInfoBean = SharedUtil.getUserInfo(context);
		}else{
			Intent intent = new Intent(context,LoginActivity.class);
			context.startActivity(intent);
			return;
		}

		if(myUserInfoBean == null){
			return;
		}
		UserInfoServiceImpl.getInstance().cancelFollowPerson(myUserInfoBean.getUserid(), bean.getUserid(), new OkHttpClientManager.StringCallback() {
			@Override
			public void onResponse(String response) {
				StatusJson statusJson = new StatusJson();
				Object obj = statusJson.analysisJson2Object(response);
				if (obj != null) {
					StatusBean statusBean = (StatusBean) obj;
					if (statusBean.getCode() == 0) {
						ToastUtil.showShort("取消关注成功!") ;
						btFollow.setSelected(false);
						btFollow.setText("关注");
						isattend = false;
					} else {
						//ToastUtil.showShort("取消关注失败!") ;
					}
				}
			}
			@Override
			public void onFailure(Request request, IOException e) {
				//ToastUtil.showShort("取消关注失败!") ;
			}
		});
	}

	class ViewHolder {
		ImageView imageViewHeadPhoto,imageViewActivity;
		TextView textViewFlans,textViewTommorrowTitle,textViewTommorrowContent,textViewNum,textViewAppointment;
        Button btFollow;
		LinearLayout llVideo;
		public ViewHolder(View view) {//id_TextAppointment
			imageViewHeadPhoto = (ImageView) view.findViewById(R.id.id_ImageHeadPhoto);
			imageViewActivity = (ImageView) view.findViewById(R.id.id_ImageViewActivity);
			textViewFlans = (TextView) view.findViewById(R.id.id_TextViewFlans);
			textViewTommorrowTitle = (TextView) view.findViewById(R.id.id_TextViewTommorrowTitle);
			textViewTommorrowContent = (TextView) view.findViewById(R.id.id_TextViewTommorrowContent);
			textViewAppointment = (TextView) view.findViewById(R.id.id_TextAppointment);
			textViewNum= (TextView) view.findViewById(R.id.id_TextViewNum);
			btFollow = (Button) view.findViewById(R.id.idButtonGuanZhu);
			llVideo  = (LinearLayout) view.findViewById(R.id.tomorrow_item_llVideo);
			view.setTag(this);



		}
	}
}
