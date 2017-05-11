package com.xz.activeplan.ui.shaolian;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.isnc.facesdk.analytics.ExperientialAnalytics;
import com.isnc.facesdk.common.BtnEnableListener;
import com.isnc.facesdk.common.MResource;
import com.isnc.facesdk.common.SuperIDUtils;
import com.isnc.facesdk.viewmodel.GetCountryData;

/**
 * Created by Administrator on 2016/8/23.
 */
public class MyPopLoginView extends RelativeLayout {
    private TextView hH;
    private TextView hI;
    private TextView hJ;
    private EditText hK;
    private Button hL;
    private Button hM;
    private Context mContext;
    private int hN;
    BtnEnableListener hO;

    public MyPopLoginView(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public MyPopLoginView(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.hN = -100;
        this.mContext = var1;
        LayoutInflater.from(var1).inflate(MResource.getIdByName(var1, "layout", "superid_pop_facelogin"), this, true);
        this.hH = (TextView)this.findViewById(MResource.getIdByName(var1, "id", "lcountrytitle"));
        this.hI = (TextView)this.findViewById(MResource.getIdByName(var1, "id", "lcountry"));
        this.hJ = (TextView)this.findViewById(MResource.getIdByName(var1, "id", "lcountrycode"));
        this.hK = (EditText)this.findViewById(MResource.getIdByName(var1, "id", "ed_lphone"));
        this.hL = (Button)this.findViewById(MResource.getIdByName(var1, "id", "edit_lclear"));
        this.hM = (Button)this.findViewById(MResource.getIdByName(var1, "id", "btn_llogin"));
        this.Q();
    }

    private void Q() {
   // this.hK.addTextChangedListener(new e(this));
    }

    public void initShow() {
        this.wigetEnable();
        this.hK.setFocusable(true);
        this.hK.setFocusableInTouchMode(true);
        this.hK.requestFocus();
    }

    public void wigetEnable() {
        this.hK.setEnabled(true);
        this.hM.setEnabled(true);
        this.hL.setEnabled(true);
        if(this.hO != null) {
            this.hO.setEnable(true);
        }

    }

    public void wigetUnable() {
        this.hK.setEnabled(false);
        this.hM.setEnabled(false);
        this.hL.setEnabled(false);
        if(this.hO != null) {
            this.hO.setEnable(false);
        }

    }

    public void countryCode() {
        if(!SuperIDUtils.appActionRight(this.mContext, "internationalSMS")) {
            this.hI.setVisibility(8);
            this.hH.setVisibility(8);
            this.hJ.setText("+86");
        } else if(GetCountryData.getCountryByMCC(this.mContext, SuperIDUtils.getMCC(this.mContext)) != null) {
            this.hI.setText(GetCountryData.getCountryByMCC(this.mContext, SuperIDUtils.getMCC(this.mContext))[1]);
            this.hJ.setText("+" + GetCountryData.getCountryByMCC(this.mContext, SuperIDUtils.getMCC(this.mContext))[3]);
        } else {
            this.hI.setText(MResource.getIdByName(this.mContext, "string", "superid_china"));
            this.hJ.setText("+86");
        }

        if(this.hJ.getText().equals("+86")) {
            this.hK.setText(SuperIDUtils.getPhoneFromSMS(this.mContext));
            if(!SuperIDUtils.getPhoneFromSMS(this.mContext).equals("")) {
                ExperientialAnalytics.getAnalyticsInstance(this.mContext).addIsPhoneFromSim(1);
            }
        }

    }

    public void showKeyBoard() {
        SuperIDUtils.showKeyBoard(true, this.hK, this.mContext);
    }

    public void hideKeyBoard() {
        SuperIDUtils.showKeyBoard(false, this.hK, this.mContext);
    }

    public void edphoneSetText(String var1) {
        this.hK.setText(var1);
        this.hK.setSelection(var1.length());
    }

    public String formatPhone() {
        return SuperIDUtils.subStringPhone(this.hJ.getText().toString(), this.hK.getText().toString());
    }

    public void btnSetText(int var1) {
        this.hM.setText(var1);
    }

    public void setCountryCode(String var1, String var2) {
        this.hI.setText(var1);
        this.hJ.setText("+" + var2);
    }

    public void setListener(BtnEnableListener var1) {
        this.hO = var1;
    }
}