package com.xz.activeplan.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xz.activeplan.R;


/**
 * @author developer
 */
public class AddAndSubView extends LinearLayout {
    Context context;
    OnNumChangeListener onNumChangeListener;
    int num;          //editText中的数值
    int editTextLayoutWidth;  //editText视图的宽度
    int editTextLayoutHeight;  //editText视图的宽度
    int editTextMinimumWidth;  //editText视图的最小宽度
    int editTextMinimumHeight;  //editText视图的最小高度
    int editTextMinHeight;  //editText文本区域的最小高度
    int editTextHeight;  //editText文本区域的高度
    private Button add;  //加号
    private Button sub;  //减号
    private EditText editText;  //输入框
    private int position;


    public AddAndSubView(Context context) {
        super(context);
        this.context = context;
        num = 1;
        control();
    }

    /**
     * 带初始数据实例化
     *
     * @param context 上下文
     * @param num     初始数据
     */
    public AddAndSubView(Context context, int num) {
        super(context);
        this.context = context;
        this.num = num;
        control();
    }


    public AddAndSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        num = 1;
        control();
    }


    public void initView(){

        View view =View.inflate(context,R.layout.inflate_addandsub,null);
        add = (Button) view.findViewById(R.id.bt_plus);
        sub = (Button) view.findViewById(R.id.bt_minus);
        editText = (EditText) view.findViewById(R.id.et_num);
        add.setTag("+");
        sub.setTag("-");
        setTextWidthHeight();
        editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        editText.setText(String.valueOf(num));
        position =editText.length();
        editText.setSelection(position);
        add.setOnClickListener(new OnButtonClickListener());
        sub.setOnClickListener(new OnButtonClickListener());
        editText.addTextChangedListener(new OnTextChangeListener());
        this.addView(view);
    }

    private void control() {
        initView();
        initTextWithHeight();
        setViewListener();
    }


    /**
     * 初始化EditText宽高参数
     */
    private void initTextWithHeight() {
        editTextLayoutWidth = -1;
        editTextLayoutHeight = -1;
        editTextMinimumWidth = -1;
        editTextMinimumHeight = -1;
        editTextMinHeight = -1;
        editTextHeight = -1;
    }


    /**
     * 设置EditText视图和文本区域宽高
     */
    private void setTextWidthHeight() {
        float fPx;

        //设置视图最小宽度
        if (editTextMinimumWidth < 0) {
            // 将数据从dip(即dp)转换到px，第一参数为数据原单位（此为DIP），第二参数为要转换的数据值
            fPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    35f, context.getResources().getDisplayMetrics());
            editTextMinimumWidth = Math.round(fPx);
        }
        editText.setMinimumWidth(editTextMinimumWidth);

        //设置文本区域高度
        if (editTextHeight > 0) {
            if (editTextMinHeight >= 0 && editTextMinHeight > editTextHeight) {
                editTextHeight = editTextMinHeight;
            }
            editText.setHeight(editTextHeight);
        }

        //设置视图高度
        if (editTextLayoutHeight > 0) {
            if (editTextMinimumHeight > 0 &&
                    editTextMinimumHeight > editTextLayoutHeight) {
                editTextLayoutHeight = editTextMinimumHeight;
            }

            LayoutParams layoutParams = (LayoutParams) editText.getLayoutParams();
            layoutParams.height = editTextLayoutHeight;
            editText.setLayoutParams(layoutParams);
        }

        //设置视图宽度
        if (editTextLayoutWidth > 0) {
            if (editTextMinimumWidth > 0 &&
                    editTextMinimumWidth > editTextLayoutWidth) {
                editTextLayoutWidth = editTextMinimumWidth;
            }

            LayoutParams layoutParams = (LayoutParams) editText.getLayoutParams();
            layoutParams.width = editTextLayoutWidth;
            editText.setLayoutParams(layoutParams);
        }
    }


    /**
     * 设置editText中的值
     *
     * @param num
     */
    public void setNum(int num) {
        this.num = num;
        editText.setText(String.valueOf(num));
    }

    /**
     * 获取editText中的值
     *
     * @return
     */
    public int getNum() {
        if (editText.getText().toString() != null) {
            return Integer.parseInt(editText.getText().toString());
        } else {
            return 1;
        }
    }

    /**
     * 设置EditText文本变化监听
     *
     * @param onNumChangeListener
     */
    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        this.onNumChangeListener = onNumChangeListener;
    }


    /**
     * 设置文本变化相关监听事件
     */
    private void setViewListener() {
        add.setOnClickListener(new OnButtonClickListener());
        sub.setOnClickListener(new OnButtonClickListener());
        editText.addTextChangedListener(new OnTextChangeListener());
    }


    /**
     * 加减按钮事件监听器
     *
     * @author noone
     */
    class OnButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            String numString = editText.getText().toString();
            if (numString == null || numString.equals("")) {
                num = 1;
                editText.setText("1");
            } else {
                if (v.getTag().equals("+")) {
                    if (++num < 1)  //先加，再判断
                    {
                        num--;
                    } else {
                        editText.setText(String.valueOf(num));

                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(AddAndSubView.this, num);
                        }
                    }
                } else if (v.getTag().equals("-")) {
                    if (--num < 1)  //先减，再判断
                    {
                        num++;
                    } else {
                        editText.setText(String.valueOf(num));
                        if (onNumChangeListener != null) {
                            onNumChangeListener.onNumChange(AddAndSubView.this, num);
                        }
                    }
                }
            }
        }
    }


    /**
     * EditText输入变化事件监听器
     *
     * @author noone
     */
    class OnTextChangeListener implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {

            String numString = s.toString();
            int len = s.toString().length();
            if (len == 1 && numString.equals("0")) {
                s.clear();
            }
            if (numString == null || numString.equals("")) {
                num = 1;
                if (onNumChangeListener != null) {
                    onNumChangeListener.onNumChange(AddAndSubView.this, num);
                }
            } else {
                int numInt = Integer.parseInt(numString);
                if (numInt < 0) {
                    Toast.makeText(context, "请输入一个大于0的数字",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //设置EditText光标位置 为文本末端
                    editText.setSelection(editText.getText().toString().length());
                    num = numInt;
                    if (onNumChangeListener != null) {
                        onNumChangeListener.onNumChange(AddAndSubView.this, num);
                    }
                }
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    }


    public interface OnNumChangeListener {
        /**
         * 输入框中的数值改变事件
         *
         * @param view 整个AddAndSubView
         * @param num  输入框的数值
         */
        public void onNumChange(View view, int num);
    }

}
