package cn.org.eshow.demo.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.bangqu.base.activity.BaseActivity;
import com.bangqu.bean.AccessTokenBean;
import com.bangqu.utils.Contact;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private Message msg;
	private AccessTokenBean accessTokenBean;
	public static boolean wx=false;
	public static boolean dswx=false;

	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 1:
					accessTokenBean= JSONObject.parseObject(msg.obj.toString(),AccessTokenBean.class);
					if (accessTokenBean!=null){
						Contact.Openid=accessTokenBean.getOpenid();
						Contact.access_token=accessTokenBean.getAccess_token();
						finish();
					}
					break;

			}
		}
	};
	/***
	 * 微信
	 */
	public static IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/**
		 * 微信
		*/
		api= WXAPIFactory.createWXAPI(this, "wx747d053fa471eb15",true);

		api.registerApp("wx747d053fa471eb15");
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
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();		
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}
 
	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		Bundle bundle = new Bundle();
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			resp.toBundle(bundle);
			SendAuth.Resp sp = new SendAuth.Resp(bundle);
			//发送成功
			if (sp.code != null) {
//				Constants.getcode = sp.code;
				getWxResult(sp.code);
				return;
			} else {
				result = "分享成功";
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "分享取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "分享拒绝";
			break;
		default:
			result = "分享返回";
			break;
		}
		Log.e("分享返回==>","返回成功");

		/*if (!StringUtils.isEmpty(result))
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();*/
		finish();
	}


	public void getWxResult(final String code) {
		params = new RequestParams();
		params.put("from", "android");
		params.put("appid", "wx747d053fa471eb15");
		params.put("secret", "95072c38637c0dc19256cc968165ff6a");
		params.put("code", code);
		params.put("grant_type", "authorization_code");

		getWx("oauth2/access_token", params);
	}

	private void goToGetMsg() {
		
	}
	
	private void goToShowMsg(ShowMessageFromWX.Req showReq) {

	}


	@Override
	public void setContentView() {

	}

	@Override
	public void initViews() {

	}

	@Override
	public void initDatas() {

	}

	@Override
	public void setDatas() {

	}

	@Override
	public void setListener() {

	}

	@Override
	public void ResumeDatas() {

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void OnReceive(String requestname, String response) {
		msg=new Message();
		if (requestname.equals("oauth2/access_token")){
			msg.what=1;
		}
		msg.obj=response;
		handler.sendMessage(msg);
	}
}