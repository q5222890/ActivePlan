package com.xz.activeplan.ui.news.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.activeplan.R;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.utils.Utiles;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;


@SuppressLint("HandlerLeak")
public class RushBuyCountDownTimerView extends LinearLayout {

    private  TextView idTextViewBrackets;
    private TextView idTextViewStata;
    private TextView idTextViewDaty;
    // 小时，十位
    private TextView tv_hour_decade;
    // 小时，个位
    private TextView tv_hour_unit;
    // 分钟，十位
    private TextView tv_min_decade;
    // 分钟，个位
    private TextView tv_min_unit;
    // 秒，十位
    private TextView tv_sec_decade;
    // 秒，个位
    private TextView tv_sec_unit;

    private Context context;

    private int hour_decade;
    private int hour_unit;
    private int min_decade;
    private int min_unit;
    private int sec_decade;
    private int sec_unit;
    private int day;
    private int stata;
    // 计时器
    private Timer timer;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        }

        ;
    };


    public RushBuyCountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_countdowntimer, this);
        tv_hour_decade = (TextView) view.findViewById(R.id.tv_hour_decade);
        tv_hour_unit = (TextView) view.findViewById(R.id.tv_hour_unit);
        tv_min_decade = (TextView) view.findViewById(R.id.tv_min_decade);
        tv_min_unit = (TextView) view.findViewById(R.id.tv_min_unit);
        tv_sec_decade = (TextView) view.findViewById(R.id.tv_sec_decade);
        tv_sec_unit = (TextView) view.findViewById(R.id.tv_sec_unit);
        idTextViewStata = (TextView) view.findViewById(R.id.id_TextViewStata);
        idTextViewDaty = (TextView) view.findViewById(R.id.id_TextViewDaty);
        idTextViewBrackets = (TextView) view.findViewById(R.id.id_TextViewBrackets);
        idTextViewBrackets.setText(")");
        Utiles.setGone(idTextViewBrackets);

    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 开始计时
     */
    public void start() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 停止计时
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * @param
     * @return void
     * @throws Exception
     * @throws
     * @Description: 设置倒计时的时长
     */
    public void setTime(int stat,int day, int hour, int min, int sec) {
        this.stata=stat;
        this.day=day;
        if (stat == 1) {
            Utiles.intRes(R.string.string_RefundTime);
            Utiles.setTextSrc(idTextViewStata);
            Utiles.setGone(idTextViewDaty);
        }else {

            Utiles.intRes(R.string.string_RefundTimeing);
            Utiles.setTextSrc(idTextViewStata);
            Utiles.setVisibility(idTextViewDaty,idTextViewBrackets);
            if (day==0){
                Utiles.setGone(idTextViewDaty);
            }
            idTextViewDaty.setText(day+"天");
        }
        if (hour >= 60 || min >= 60 || sec >= 60 || hour < 0 || min < 0
                || sec < 0) {
        }

        hour_decade = hour / 10;
        hour_unit = hour - hour_decade * 10;

        min_decade = min / 10;
        min_unit = min - min_decade * 10;

        sec_decade = sec / 10;
        sec_unit = sec - sec_decade * 10;

        tv_hour_decade.setText(hour_decade + "");
        tv_hour_unit.setText(hour_unit + "");
        tv_min_decade.setText(min_decade + "");
        tv_min_unit.setText(min_unit + "");
        tv_sec_decade.setText(sec_decade + "");
        tv_sec_unit.setText(sec_unit + "");

    }

    /**
     * @param
     * @return boolean
     * @throws
     * @Description: 倒计时
     */
    private void countDown() {

        if (isCarry4Unit(tv_sec_unit) == true) {//秒个
            if (isCarry4Decade(tv_sec_decade) == true) {//秒，十位
                if (isCarry4Unit(tv_min_unit) == true) { // 分钟，个位
                    if (isCarry4Decade(tv_min_decade) == true) { // 分钟，十位
                        if (isCarry4Unit(tv_hour_unit) == true) { // 小时，个位
                            if (isCarry4Decade(tv_hour_decade) == true) {  // 小时，十位
                                if (stata==0) {
                                    Intent mIntent = new Intent(UrlsManager.END_ONE_DAY);
                                    context.sendBroadcast(mIntent);
                                    stop();
                                }
                                if (stata==7){
                                    if (day==0){
                                        Intent mIntent = new Intent(UrlsManager.END_SEVEN_DAY);
                                        context.sendBroadcast(mIntent);
                                        stop();
                                    }else{
                                        setTime(7,day-1,23,59,59);
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param
     * @return boolean
     * @throws
     * @Description: 变化十位，并判断是否需要进位
     */
    private boolean isCarry4Decade(TextView tv) {
        int time = Integer.valueOf(tv.getText().toString());

        time = time - 1;
        if (time < 0) {
            time = 5;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    /**
     * @param
     * @return boolean
     * @throws
     * @Description: 变化个位，并判断是否需要进位
     */
    private boolean isCarry4Unit(TextView tv) {
        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 9;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }
}
