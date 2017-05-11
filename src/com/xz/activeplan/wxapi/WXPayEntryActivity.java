package com.xz.activeplan.wxapi;






import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xz.activeplan.R;
import com.xz.activeplan.ui.UrlsManager;
import com.xz.activeplan.utils.Utiles;
import com.xz.activeplan.utils.observer.ClassConcrete;
import com.xz.activeplan.utils.observer.EventBean;
import com.xz.activeplan.utils.observer.EventType;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, UrlsManager.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@SuppressLint("LongLogTag")
	@Override
	public void onResp(final BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		Utiles.log("微信支付信息：resp.getType():"+resp.getType()+"===resp.errCode:"+resp.errCode);
			if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
				if(resp.errCode == BaseResp.ErrCode.ERR_OK){
					ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.WEIXIN_PAY_SUCCESS)) ;
				}else{
					ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.WEIXIN_PAY_ERROR)) ;
				}
				finish() ;
			
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errCode ==BaseResp.ErrCode.ERR_OK?"支付成功":"支付失败"));
//			builder.setCancelable(false);
//			builder.setPositiveButton("确定", new OnClickListener() {
//				@Override
//				public void onClick(DialogInterface arg0, int arg1) {
//					if(resp.errCode == BaseResp.ErrCode.ERR_OK){
//						ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.WEIXIN_PAY_SUCCESS)) ;
//					}else{
//						ClassConcrete.getInstance().postDataUpdate(new EventBean(EventType.WEIXIN_PAY_ERROR)) ;
//					}
//					finish() ;
//				}
//			});
//			builder.show();
		}else{
			finish();
		}
	}
}