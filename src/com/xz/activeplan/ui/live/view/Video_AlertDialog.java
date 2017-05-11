package com.xz.activeplan.ui.live.view;


import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.xz.activeplan.R;

/**
 * 打赏的Dialog
 */
@SuppressWarnings(value = { "all" })
public class Video_AlertDialog {
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
	private float size;
	public Video_AlertDialog(Context context,View view) {
		this.context = context;
		this.view= view;
		this.size= 0.85f;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}
	public Video_AlertDialog(Context context,View view,float size) {
		this.context = context;
		this.view= view;
		this.size = size;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}
	public Video_AlertDialog builder() {
		// 获取Dialog布局
		/*View view = LayoutInflater.from(context).inflate(
				R.layout.video_alert, null);*/

		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		/*txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		txt_msg.setVisibility(View.GONE);
		*//*btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_neg.setVisibility(View.GONE);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);*//*
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.VISIBLE);*/

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * size), LayoutParams.WRAP_CONTENT));

		return this;
	}

	public Video_AlertDialog setTitle(String title) {
		return this;
	}

	public Video_AlertDialog setMsg(String msg) {
		return this;
	}

	public Video_AlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	/*public Video_AlertDialog setPositiveButton(String text,
			final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_pos.setText("确定");
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public Video_AlertDialog setNegativeButton(String text,
			final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			//btn_neg.setText("取消");
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}*/

	private void setLayout() {
		if (!showTitle && !showMsg) {
			//txt_title.setText("提示");
			//txt_title.setVisibility(View.GONE);
		}

		if (showTitle) {
			//txt_title.setVisibility(View.GONE);
		}

		if (showMsg) {
			//txt_msg.setVisibility(View.GONE);
		}

		/*if (!showPosBtn && !showNegBtn) {
			//btn_pos.setText("确定");
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			btn_pos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}

		if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}

		if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (!showPosBtn && showNegBtn) {
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}*/
	}

	public void show() {
		setLayout();
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
