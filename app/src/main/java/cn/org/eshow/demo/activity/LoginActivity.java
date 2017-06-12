package cn.org.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.base.activity.BaseActivity;
import com.bangqu.bean.AccessTokenBean;
import com.bangqu.bean.QQAccessTokenBean;
import com.bangqu.utils.Contact;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.UMShareAPI;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.ThirdPartyLoginBean;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.demo.network.ESResponseListener;
import cn.org.eshow.demo.network.NetworkInterface;
import cn.org.eshow.demo.view.LoginAutoCompleteEdit;
import cn.org.eshow.framwork.fragment.AbProgressDialogFragment;
import cn.org.eshow.framwork.util.AbDialogUtil;
import cn.org.eshow.framwork.util.AbLogUtil;
import cn.org.eshow.framwork.util.AbStrUtil;
import cn.org.eshow.framwork.util.AbToastUtil;
import cn.org.eshow.framwork.util.AbViewUtil;


/**
 * 登录页面
 * Created by daikting on 16/2/24.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements IUiListener {
    private Context mContext = LoginActivity.this;
    private TextView tvCodeLogin;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;
    @ViewById(R.id.tvSubTitle)
    TextView mTvSubTitle;
    @ViewById(R.id.etTel)
    LoginAutoCompleteEdit mEtTel;
    @ViewById(R.id.etPassword)
    LoginAutoCompleteEdit mEtPassword;
    @ViewById(R.id.btnLogin)
    Button mBtnLogin;
    @ViewById(R.id.tvForgetPW)
    TextView mTvForgetPW;
    @ViewById(R.id.llQQLogin)
    LinearLayout mLlQQLogin;
    @ViewById(R.id.llWechatLogin)
    LinearLayout mLlWechatLogin;
    //友盟分享
    UMShareAPI umShareAPI;

    AbProgressDialogFragment progressDialog;

    String userName = "";
    //第三方授权成功得到的token
    String thirdToken = "";

    private Message msg;
    private AccessTokenBean accessTokenBean;
    private QQAccessTokenBean qqAccessTokenBean;
    private ThirdPartyLoginBean thirdPartyLoginBean;
    private UserInfo mInfo;
    private String nickname;
    private String photo;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    accessTokenBean= com.alibaba.fastjson.JSONObject.parseObject(msg.obj.toString(),AccessTokenBean.class);
                    if (accessTokenBean!=null){
                        try {
                            initOpenidAndToken(new JSONObject(msg.obj.toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setQQBind();
                        updateUserInfo();

                    }
                    break;
                case 2:
                    thirdPartyLoginBean= com.alibaba.fastjson.JSONObject.parseObject(msg.obj.toString(),ThirdPartyLoginBean.class);
                    if (thirdPartyLoginBean!=null){
                        if (thirdPartyLoginBean.isBind()){

                        }else {
                            intent = new Intent(LoginActivity.this, AccountBindingActivity.class);
                            if (platform.equals("weixin")) {
                                intent.putExtra("username", username);
                                intent.putExtra("access_token", access_token);
                                intent.putExtra("platform", platform);
                            }else {
                                intent.putExtra("username", username);
                                intent.putExtra("platform", platform);
                                intent.putExtra("nickname", nickname);
                                intent.putExtra("photo", photo);
                            }
                            Jump(intent);
                        }
                    }
                    break;
                case 3:
                    qqAccessTokenBean= com.alibaba.fastjson.JSONObject.parseObject(msg.obj.toString(),QQAccessTokenBean.class);
                    if (qqAccessTokenBean!=null){
                        nickname=qqAccessTokenBean.getNickname();
                        photo=qqAccessTokenBean.getFigureurl_qq_2();
                        params=new RequestParams();
                        params.put("thirdParty.username",username);
                        LoginActivity.this.post("third-party/login",params);
                    }
                    break;
            }
        }
    };

    private String platform;
    private String username;
    private void setQQBind() {
        platform="qq";
        username=accessTokenBean.getOpenid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        AbViewUtil.scaleContentView((RelativeLayout) findViewById(R.id.rlParent));
        mTvTitle.setText(getTitle());
        mTvSubTitle.setVisibility(View.VISIBLE);
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        mMaterialBackButton.setVisibility(View.VISIBLE);
        tvCodeLogin=(TextView)findViewById(R.id.tvCodeLogin);
        tvCodeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,CodeLoginActivity.class);
                startActivity(intent);
            }
        });

        userName = getIntent().getStringExtra(InputPasswordActivity.INTENT_TEL);
        if(cn.org.eshow.framwork.util.AbStrUtil.isEmpty(userName)){
            userName = SharedPrefUtil.getTempTel(mContext);
        }
        mEtTel.setText(userName);
    }
    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    @Click(R.id.btnLogin)
    void onLogin() {

        String userName = mEtTel.getText().toString();
        final String password = mEtPassword.getText().toString();

        if (AbStrUtil.isEmpty(userName) && !AbStrUtil.isMobileNo(userName)) {
            AbToastUtil.showToast(mContext, "请输入账号！！");
            return;
        }

        if (AbStrUtil.isEmpty(password)) {
            AbToastUtil.showToast(mContext, "请输入密码！");
            return;
        }

        if (AbStrUtil.strLength(password) > 20) {
            AbToastUtil.showToast(mContext, "密码长度过长！");
            return;
        }
        ESResponseListener responseListener = new ESResponseListener(mContext) {
            @Override
            public void onBQSucess(String esMsg, JSONObject resultJson) {
                try {
                    String userStr = resultJson.getJSONObject("user").toString();
                    AbLogUtil.d(mContext, "Login  userStr:" + userStr);
                    SharedPrefUtil.setUser(mContext, userStr);
                    JSONObject tokenJson = resultJson.getJSONObject("accessToken");
                    String token = tokenJson.getString("accessToken");
                    SharedPrefUtil.setAccesstoken(mContext,token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MainActivity_.intent(mContext).start();
            }

            @Override
            public void onBQNoData() {

            }

            @Override
            public void onBQNotify(String bqMsg) {
                AbToastUtil.showToast(mContext, bqMsg);
            }

            @Override
            public void onStart() {
                progressDialog = AbDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "请求数据中...");

            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Log.i("LoginActivity", "1onFailure:statusCode = " + statusCode + ", content is " + content);
                if(error!=null){
                    error.printStackTrace();
                }
                progressDialog.dismiss();
                try {
                    JSONObject ret=new JSONObject(content);
                    int rr=ret.getInt("status");
                    Log.i("LoginActivity","status =="+rr);
                    if(rr==-5){
                        Log.i("LoginActivity", ret.getString("msg"));
                        AbToastUtil.showToast(mContext, ret.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        NetworkInterface.login(mContext, userName, password, responseListener);
    }

    @Click(R.id.tvForgetPW)
    void onForgetPW() {
        String userName = mEtTel.getText().toString();
        Intent intent=new Intent(LoginActivity.this,ForgetActivity.class);
        startActivity(intent);
//        InputTelActivity_.intent(mContext).extra(InputTelActivity_.INTENT_ISREGISTER, Enum_CodeType.FINDPASSWORD).extra(InputTelActivity_.INTENT_TEL,userName).start();
    }

    /**
     * 点击注册
     */
    @Click(R.id.tvSubTitle)
    void onRegister() {
        String userName = mEtTel.getText().toString();
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
//        InputTelActivity_.intent(mContext).extra(InputTelActivity_.INTENT_ISREGISTER, Enum_CodeType.REGISTER).extra(InputTelActivity_.INTENT_TEL,userName).start();
    }

    /**
     * 微信授权登录
     */
    @Click(R.id.llWechatLogin)
    void onWechatLogin() {
      /*  Log.i("LoginActivity", "微信授权登录");
        //AbToastUtil.showToast(mContext,"功能正在完善开发中...");
        Config.dialog = ProgressDialog.show(mContext, "提示", "正在请求跳转....");
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        umShareAPI = UMShareAPI.get(mContext);
        umShareAPI.doOauthVerify(this, platform, umAuthListener);*/
        if (Contact.isWeixinAvilible(this)) {
            progressDialog = AbDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "登录中");
            mLlWechatLogin.setEnabled(false);
            IWXAPI api = WXAPIFactory.createWXAPI(this, "wx747d053fa471eb15", true);

            api.registerApp("wx747d053fa471eb15");
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            api.sendReq(req);
        } else {
            ToastUtils.show(this, "请安装微信");
        }
    }

    /**
     * QQ授权登录
     */
    @Click(R.id.llQQLogin)
    void onQQLogin() {
       /* Log.i("LoginActivity","QQ授权登录");
        //AbToastUtil.showToast(mContext,"功能正在完善开发中...");
        Config.dialog = ProgressDialog.show(mContext, "提示", "正在请求跳转....");
        SHARE_MEDIA platform = SHARE_MEDIA.QQ;
        umShareAPI = UMShareAPI.get(mContext);
        umShareAPI.doOauthVerify(this, platform, umAuthListener);*/
        progressDialog = AbDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "登录中");
        LoginQQ();

    }

    private Tencent mTencent;
    public void LoginQQ()
    {
        mTencent = Tencent.createInstance("1105134763", this.getApplicationContext());
        if (!mTencent.isSessionValid())
        {
            mTencent.login(this, "all", this);
        }
    }


    public void work(Map<String, String> map) {
        if(map==null){
            return;
        }
        Collection<String> c = map.values();
        Iterator it = c.iterator();
        for (; it.hasNext();) {
            Log.i("LoginActivity","--------"+it.next());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,this);
        }

    }

    @Override
    public void onComplete(Object o) {
        Log.e("QQ=>",o.toString());

//        /isab
        msg=new Message();
        msg.what=1;
        msg.obj=o;
        handler.sendMessage(msg);

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }

    private String access_token;
    @Override
    protected void onResume() {
        super.onResume();
        if (!StringUtils.isEmpty(Contact.Openid)){
            if (progressDialog!=null){
                progressDialog.dismiss();
            }
            username=Contact.Openid;
            access_token=Contact.access_token;
            platform="weixin";
            Contact.Openid="";
            Contact.access_token="";
            params=new RequestParams();
            params.put("thirdParty.username",username);
            post("third-party/login",params);
//            startActivity(intent);

        }
        mLlWechatLogin.setEnabled(true);


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
        if (requestname.equals("third-party/login")){
            msg.what=2;
        }
        msg.obj=response;
        handler.sendMessage(msg);
    }
    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
                    msg=new Message();
                    msg.what=3;
                    msg.obj=response;
                    handler.sendMessage(msg);
                    if (progressDialog!=null){
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        }
    }

    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

}
