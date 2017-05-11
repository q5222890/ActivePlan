package com.xz.activeplan.ui.live.view;


import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.activeplan.R;

/**
 * 直播人数的Dialog
 */
@SuppressWarnings(value = { "all" })
public class LivePersonAlertDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg;
	/*private Button btn_neg;
	private Button btn_pos;*/
	private ImageView img_line;
	private Display display;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;
    private View view;
	public LivePersonAlertDialog(Context context, View view) {
		this.context = context;
		this.view= view;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public LivePersonAlertDialog builder() {

		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		// 调整dialog背景大小
		/*FrameLayout.LayoutParams  layoutParams= new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.45), LinearLayout.LayoutParams.WRAP_CONTENT);
		lLayout_bg.setLayoutParams(layoutParams);  //设置宽 高*/
		//dialog.getWindow().setGravity(Gravity.TOP);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		dialog.getWindow().setGravity(Gravity.LEFT|Gravity.TOP);
		lp.y= -150;
		dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
		dialog.getWindow().setAttributes(lp);
		return this;
	}

	public LivePersonAlertDialog setTitle(String title) {
		return this;
	}

	public LivePersonAlertDialog setMsg(String msg) {
		return this;
	}

	public LivePersonAlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public void show() {
		dialog.show();
	}
	/**
	 * 关闭
	 */
	public void cancel()
	{
		dialog.cancel();
	}
	/**
	 *
	 */
	public void dismiss()
	{
		dialog.dismiss();
	}

}
