package com.xz.activeplan.ui.personal.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.BaseFragmentActivity;
import com.xz.activeplan.ui.personal.text.mConversationListAdapter02;
import com.xz.activeplan.ui.personal.text.mConversationListFragment;
import com.xz.activeplan.utils.SharedUtil;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.EventType;

import io.rong.imkit.RongContext;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.model.Conversation;

/**
 * 会话列表页面
 * 
 * @author johnny
 * 
 */
public class ConversationListActivity extends BaseFragmentActivity implements OnClickListener {
/**private ListView list;
	private mConversationListAdapter adapter;
	private List<Conversation> mList;
	private SharedPreferences spLogin;
	private SharedPreferences.Editor edLogin;
	private UserCenterRecivey myReceiver;

	LayoutInflater inflater = null;
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_conversation_list_layout);
		initView();
			Utiles.toast(this," 自定义会话列表 ");
		spLogin = ConversationListActivity.this.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		edLogin = spLogin.edit();
		ArrayList<ActiveinfosBean> listActiveinfosBean = RecommendFragment.listActiveinfosBean;

		mList = RongIM.getInstance().getRongIMClient().getConversationList();

		// Log.i("--会话列表-->", "会话内容：" + mList);
		// mConversationListFragment fragment = new mConversationListFragment();
		// Uri uri = Uri.parse("rong://" +
		// this.getApplicationInfo().packageName).buildUpon().appendPath("conversationlist")
		// .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),"false")
		// //设置私聊会话是否聚合显示
		// .appendQueryParameter(Conversation.ConversationType.GROUP.getName(),"true")//群组
		// .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(),"false")//讨论组
		// .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(),
		// "false")//公共服务号
		// .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(),"false")//订阅号
		// .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(),"true")//系统
		// .build();
		// fragment.setUri(uri);
		//
		// FragmentTransaction transaction =
		// getSupportFragmentManager().beginTransaction();
		// //rong_content 为你要加载的 id
		// transaction.add(R.id.conversationlist, fragment);
		// transaction.commit();

		list = (ListView) findViewById(R.id.list);
		adapter = new mConversationListAdapter(this, mList,RecommendFragment.listActiveinfosBean);
		list.setAdapter(adapter);
		list.setOnItemClickListener(listener);
		list.setOnItemLongClickListener(longListener);

		myReceiver = new UserCenterRecivey();
		IntentFilter sendFilter = new IntentFilter(
				BroadcastConstant.SEND_OUT_ACTION); // 发送数据广播
		ConversationListActivity.this.registerReceiver(myReceiver, sendFilter);

		IntentFilter Shuttle = new IntentFilter(
				BroadcastConstant.SHUTTLE_ACTION); // 接收数据广播
		ConversationListActivity.this.registerReceiver(myReceiver, Shuttle);
	}

	private void initView() {
			 findViewById(R.id.id_ImageHeadTitleBlack).setOnClickListener(this);
	}
	// 点击item
	AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			Conversation.ConversationType conversationType = mList.get(position)
					.getConversationType();

			if (conversationType.name().equals("PRIVATE")) { // 私聊
				if (RongIM.getInstance() != null) {
					edLogin.putString("click_userid", mList.get(position)
							.getTargetId());
					edLogin.commit();
					RongIM.getInstance().startPrivateChat(
							ConversationListActivity.this,
							mList.get(position).getTargetId(),
							mList.get(position).getConversationTitle());
				}
			} else if (conversationType.name().equals("GROUP")) { // 群组

			} else if (conversationType.name().equals("DISCUSSION")) { // 讨论组

			} else if (conversationType.name().equals("SYSTEM")) { // 系统
					Utiles.log(" ========> 系统聊天：");
			}
			// 清除点击后的userid未读消息
			RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(conversationType,mList.get(position).getTargetId());
			adapter.notifyDataSetChanged();
		}
	};
	// 长按item
	AdapterView.OnItemLongClickListener longListener = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
									   int position, long id) {
			Log.i("--->", "长按");

			showPopupWindow(view, position);
			return true;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myReceiver != null) {
			ConversationListActivity.this.unregisterReceiver(myReceiver);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.id_ImageHeadTitleBlack:
				finish();
				break;
		}
	}

	//使用广播来更新数据
	public class UserCenterRecivey extends BroadcastReceiver {

		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			// 刷新listview的列表
			if (intent.getAction().equals(BroadcastConstant.SEND_OUT_ACTION)) {
				LoadContent();
			} else if (intent.getAction().equals(
					BroadcastConstant.SHUTTLE_ACTION)) {
				LoadContent();
			}
		}
	}


	// 加载mlist数据同时更新adapter
	 	public void LoadContent() {
		mList = RongIM.getInstance().getRongIMClient().getConversationList();
		adapter = new mConversationListAdapter(ConversationListActivity.this,
				mList, RecommendFragment.listActiveinfosBean);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}


	 // 设置一个类型的PopupWindow的弹框


	private void showPopupWindow(View view, final int position) {
		final Conversation.ConversationType conversationType = mList.get(position)
				.getConversationType();
		final String targetId = mList.get(position).getTargetId();
		final boolean top = mList.get(position).isTop();
		// 一个自定义的布局，作为显示的内容
		View popView = LayoutInflater.from(ConversationListActivity.this)
				.inflate(R.layout.list_item_pop, null);
		popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		// 设置popwindow出现和消失动画
		popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
		// 获取到控件的宽度
		int popwidth =getResources().getDimensionPixelSize(R.dimen.hundred_dip);
		// 获取显示器的宽度
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		int windowWith = display.getWidth();

		int[] arrayOfInt = new int[2];
		// 获取点击按钮的坐标
		view.getLocationOnScreen(arrayOfInt);
		int x = (windowWith / 2)-(popwidth/2);
		int y = arrayOfInt[1];

		// 设置popwindow显示位置
		popupWindow.showAtLocation(view, 0, x, y);
		// 获取popwindow焦点
		popupWindow.setFocusable(true);
		// 设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		TextView set_top = (TextView) popView.findViewById(R.id.set_top);
		if (top) {
			set_top.setText("取消置顶");
		}else {
			set_top.setText("置顶");
		}
		// 设置会话列表置顶
		set_top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设置置顶
				RongIM.getInstance()
						.getRongIMClient()
						.setConversationToTop(conversationType, targetId,
								!top);
				LoadContent();
				popupWindow.dismiss();
			}
		});
		// 设置删除会话列表内容
		TextView delete = (TextView) popView.findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设置删除会话列表内容itm
				RongIM.getInstance().getRongIMClient()
						.removeConversation(conversationType, targetId);
				// 删除会话列表消息内容
				// RongIM.getInstance().getRongIMClient().clearMessages(conversationType,targetId);
				LoadContent();
				popupWindow.dismiss();
			}
		});
	}

}
	 */



	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Utiles.setStatusBar(this);
		setContentView(R.layout.activity_conversation_list_layout);
		if (EventType.CONVERSATION_TYPE== Conversation.ConversationType.GROUP){
			Utiles.headManager(this,R.string.Group_Message);
		}
		if (EventType.CONVERSATION_TYPE== Conversation.ConversationType.PRIVATE){
			Utiles.headManager(this,R.string.string_private_chat);
		}
		enterFragment();
	}

	// 加载 会话列表 ConversationListFragment
	private void enterFragment() {
		mConversationListFragment fragment = (mConversationListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.conversationlist);
		fragment.setAdapter(new mConversationListAdapter02(RongContext.getInstance()));
		Uri uri = Uri
				.parse("rong://" + getApplicationInfo().packageName)
				.buildUpon()
				.appendPath("conversationlist")
				.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
				.appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话聚合显示
				.appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
				.appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
				.build();
		fragment.setUri(uri);
	}

	 //列表适配器

@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_ImageHeadTitleBlack:
			finish();
			break;
		}
	}

	class MyConversationListAdapter extends ConversationListAdapter {

		public MyConversationListAdapter(Context context) {
			super(context);
		}

		@Override
		protected View newView(Context context, int position, ViewGroup group) {
			return super.newView(context, position, group);
		}

		@Override
		protected void bindView(View v, int position, UIConversation data) {
			if (data.getConversationType().equals(
					Conversation.ConversationType.DISCUSSION))
				data.setUnreadType(UIConversation.UnreadRemindType.REMIND_ONLY);

			if(data.getConversationType().equals(
					Conversation.ConversationType.GROUP)){
				data.setUIConversationTitle(SharedUtil.get(ConversationListActivity.this, data.getConversationTargetId() + "-GROUP" ));
			}else if(data.getConversationType().equals(
					Conversation.ConversationType.PRIVATE)){
				data.setUIConversationTitle(SharedUtil.get(ConversationListActivity.this, data.getConversationTargetId() + "-PRIVATE"));
			}
		data.setIconUrl(Uri.parse(SharedUtil.getUserInfo(ConversationListActivity.this).getHeadurl())) ;
			super.bindView(v, position, data);
		}

	}
	}



